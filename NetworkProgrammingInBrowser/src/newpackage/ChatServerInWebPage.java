/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newpackage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatServerInWebPage {

    static final Map<String, SessionInfo> sessions = new HashMap<>();
    static final Map<String, StringBuilder> messages = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket ss = null;
        int port = 3066;
        System.err.println("Server is running on port: " + port);
        try {
            ss = new ServerSocket(port, 0, InetAddress.getByName("0.0.0.0"));
            while (true) {
                Socket soc = ss.accept();
                System.err.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(soc);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ss != null && !ss.isClosed()) {
                ss.close();
            }
        }
    }
}

class ClientHandler implements Runnable {

    private static final String USERNAME1 = "user1";
    private static final String PASSWORD1 = "password1";
    private static final String USERNAME2 = "user2";
    private static final String PASSWORD2 = "password2";
    private boolean authenticated = false;
    private String sessionId;

    private BufferedReader br;
    private OutputStream os;
    private Socket soc;

    public ClientHandler(Socket soc) {
        this.soc = soc;
        try {
            br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            os = soc.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   

    @Override
    public void run() {
        try {
            String str;
            String requestedFile = "chat.html";
            String host = null;

            while ((str = br.readLine()) != null) {
                System.out.println(str);
                if (str.startsWith("Host:")) {
                    host = str.split(" ")[1];
                }
                if (str.startsWith("GET")) {
                    String[] parts = str.split(" ");
                    if (parts.length > 1) {
                        requestedFile = parts[1].substring(1);
                        if (requestedFile.isEmpty()) {
                            requestedFile = "chat.html";
                        }
                    }
                }
                if (str.startsWith("Authorization: Basic ")) {
                    String base64Credentials = str.substring("Authorization: Basic ".length()).trim();
                    String credentials = new String(Base64.getDecoder().decode(base64Credentials));
                    String[] userPass = credentials.split(":");
                    authenticated = authenticate(userPass[0], userPass[1]);
                    if (authenticated) {
                        String loginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        sessionId = createSession(userPass[0], loginTime);
                        sendAuthenticatedResponse(sessionId, loginTime);
                    } else {
                        sendLoginPage();
                        return;
                    }
                }
                if (str.startsWith("Cookie:")) {
                    String cookieHeader = str.substring("Cookie: ".length());
                    String[] cookies = cookieHeader.split(";");
                    for (String cookie : cookies) {
                        String[] cookiePair = cookie.split("=");
                        if (cookiePair.length == 2 && cookiePair[0].trim().equals("sessionId")) {
                            sessionId = cookiePair[1].trim();
                            authenticated = validateSession(sessionId);
                            if (authenticated) {
                                SessionInfo sessionInfo = ChatServerInWebPage.sessions.get(sessionId);
                                sendAuthenticatedResponse(sessionId, sessionInfo.getLoginTime());
                            } else {
                                sendLoginPage();
                                return;
                            }
                        }
                    }
                }
                if (str.isEmpty()) {
                    break;
                }
            }

            if (!authenticated) {
                sendLoginPage();
            } else {
                if (host != null) {
                    if (requestedFile.equals("chat.html")) {
                        serveChatContent();
                    }else if (requestedFile.equals("logout")) {
                        handleLogout(); // Handle logout request
                    } else {
                        serveFile("chat", requestedFile);
                    }
                } else {
                    error();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (os != null) {
                    os.close();
                }
                if (soc != null && !soc.isClosed()) {
                    soc.close();
                }
                System.err.println("Client connection closed!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendLoginPage() throws IOException {
        os.write("HTTP/1.1 401 Unauthorized\r\n".getBytes());
        os.write("WWW-Authenticate: Basic realm=\"Access to the site\"\r\n".getBytes());
        os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
        os.write("\r\n".getBytes());
        os.write("<html><body><h1>401 Unauthorized</h1></body></html>".getBytes());
        os.flush();
    }

    private void sendAuthenticatedResponse(String sessionId, String loginTime) throws IOException {
        os.write("HTTP/1.1 200 OK\r\n".getBytes());
        os.write(("Set-Cookie: sessionId=" + sessionId + "; Path=/; HttpOnly\r\n").getBytes());
        os.write(("Set-Cookie: loginTime=" + loginTime + "; Path=/; HttpOnly\r\n").getBytes());
        os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
        os.write("\r\n".getBytes());
        serveChatContent();
        os.flush();
    }

    private void serveFile(String folder, String fileName) {
        try (FileReader fileReader = new FileReader(folder + "/" + fileName)) {
            os.write("HTTP/1.1 200 OK\r\n".getBytes());
            os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
            os.write("\r\n".getBytes());

            int character;
            while ((character = fileReader.read()) != -1) {
                os.write(character);
            }
            os.write("\r\n".getBytes());
            os.flush();
        } catch (FileNotFoundException e) {
            error();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void handleLogout() throws IOException {
        if (sessionId != null && ChatServerInWebPage.sessions.containsKey(sessionId)) {
            ChatServerInWebPage.sessions.remove(sessionId);
            os.write("HTTP/1.1 200 OK\r\n".getBytes());
            os.write("Set-Cookie: sessionId=deleted; Path=/; Max-Age=0\r\n".getBytes());
            os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
            os.write("\r\n".getBytes());
            os.write("<html><body><h1>You have been logged out.</h1></body></html>".getBytes());
            os.flush();
        } else {
            sendLoginPage(); 
        }
    }

    private void serveChatContent() throws IOException {
        StringBuilder content = ChatServerInWebPage.messages.getOrDefault("chat", new StringBuilder());
        String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html; charset=UTF-8\r\n"
                + "\r\n"
                + "<html><body>"
                + "<div id='chat-box'>" + content + "</div>"
                + "<form method='POST' id='chat-form'>"
                + "<input type='text' name='message'>"
                + "<button type='submit'>Send</button>"
                + "</form>"
                + "</body></html>";
        os.write(response.getBytes());
        os.flush();
    }

    private boolean authenticate(String username, String password) {
        return (username.equals(USERNAME1) && password.equals(PASSWORD1))
                || (username.equals(USERNAME2) && password.equals(PASSWORD2));
    }

    private String createSession(String username, String loginTime) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sessionIdBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sessionIdBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        String sessionId = sessionIdBuilder.toString();

        SessionInfo sessionInfo = new SessionInfo(username, loginTime);
        ChatServerInWebPage.sessions.put(sessionId, sessionInfo);

        return sessionId;
    }

    private boolean validateSession(String sessionId) {
        return ChatServerInWebPage.sessions.containsKey(sessionId);
    }

    private void error() {
        try {
            os.write("HTTP/1.1 404 Not Found\r\n".getBytes());
            os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
            os.write("\r\n".getBytes());
            os.write("<html><body><h1>404 Not Found</h1></body></html>".getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SessionInfo {

    private String username;
    private String loginTime;

    public SessionInfo(String username, String loginTime) {
        this.username = username;
        this.loginTime = loginTime;
    }

    public String getUsername() {
        return username;
    }

    public String getLoginTime() {
        return loginTime;
    }
}
