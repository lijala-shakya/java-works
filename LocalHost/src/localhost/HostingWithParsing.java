/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package localhost;

/**
 *
 * @author DELL
 */
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HostingWithParsing {

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        ServerSocket ss = null;
        int port = 3078;
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
        }finally {
            if (ss != null && !ss.isClosed()) {
                ss.close();
            }
        }
    }
    
}

class ClientHandler implements Runnable {
    private BufferedReader br;
    private InputStreamReader isr;
    private OutputStream os;
    private Socket soc;
    
    public ClientHandler(Socket soc) {
        this.soc = soc;
        try {
            isr = new InputStreamReader(soc.getInputStream());
            br = new BufferedReader(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        
        try {
             String requestLine = br.readLine();
            System.out.println("Request Line: " + requestLine);
            
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

            }
            try (FileReader reader = new FileReader("server.txt")) {
                os = soc.getOutputStream();
                os.write("HTTP/1.1 200 OK\r\n".getBytes());
                os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                os.write("\r\n".getBytes());
                
                int character;
                while ((character = reader.read()) != -1) {
                    os.write(character);
                }
                os.write("\r\n".getBytes());
                os.flush();
            } catch (Exception e) {
               os = soc.getOutputStream();
                os.write("HTTP/1.1 404 Not Found\r\n".getBytes());
                os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                os.write("\r\n".getBytes());
                os.write("<html><body><h1>404 Not Found</h1></body></html>".getBytes());
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
                if (os != null) os.close();
                soc.close();
                System.err.println("Client connection closed!");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

