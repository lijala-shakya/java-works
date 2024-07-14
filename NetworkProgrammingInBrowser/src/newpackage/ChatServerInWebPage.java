/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newpackage;

/**
 *
 * @author DELL
 */
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
public class ChatServerInWebPage {
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
//                System.out.println(str);
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
                
                } if (str.isEmpty()) {
                    break;
                }
            }
            if (!authenticated) {
                sendAuthenticationRequired();
            } else {
                if (host != null) {
                    String folder = "chat";
                    
                    serveFile(folder, requestedFile);
                } else {
                    error();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    private void sendAuthenticationRequired() throws IOException {
        os.write("HTTP/1.1 401 Unauthorized\r\n".getBytes());
        os.write("WWW-Authenticate: Basic realm=\"Access to the site\"\r\n".getBytes());
        os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
        os.write("\r\n".getBytes());
        os.write("<html><body><h1>401 Unauthorized</h1></body></html>".getBytes());
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
    private boolean authenticate(String username, String password) {
        return (username.equals(USERNAME1) && password.equals(PASSWORD1)) ||
               (username.equals(USERNAME2) && password.equals(PASSWORD2));
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
