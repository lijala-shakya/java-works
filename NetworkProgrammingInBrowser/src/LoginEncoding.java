

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DELL
 */
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

public class LoginEncoding {

    private static final Map<String, SessionInfo> sessions = new HashMap<>();
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    public static void main(String[] args) throws IOException {
        ServerSocket ss = null;
        int port = 3070;
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

    static class ClientHandler implements Runnable {
        private BufferedReader br;
        private OutputStream os;
        private Socket soc;
        private boolean authenticated = false;
        private String sessionId;

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
                String host = null;
                String requestedFile = "index.html";

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
                                requestedFile = "index.html";
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
                                    SessionInfo sessionInfo = sessions.get(sessionId);
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
                        String folder = "";
                        if (host.startsWith("127.0.0.1")) {
                            folder = "127.0.0.1";
                        } else if (host.startsWith("192.168.1.109")) {
                            folder = "192.168.1.109";
                        }
                        serveFile(folder, requestedFile);
                    } else {
                        error();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close();
                    if (os != null) os.close();
                    if (soc != null && !soc.isClosed()) soc.close();
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
            serveFileContent("index.html");
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

        private void serveFileContent(String fileName) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    os.write(line.getBytes());
                    os.write("\r\n".getBytes());
                }
            } catch (FileNotFoundException e) {
                error();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean authenticate(String username, String password) {
            return username.equals(USERNAME) && password.equals(PASSWORD);
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
            sessions.put(sessionId, sessionInfo);

            return sessionId;
        }

        private boolean validateSession(String sessionId) {
            return sessions.containsKey(sessionId);
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
}




//map<key=sessionid,value=newclass>
        //add logout only if loggedin 
//        chatbox banaune 2 jana user ko login garera message ppathauna euta form halne message ani send button mathi already sent messages ani username time ani send gareko message
