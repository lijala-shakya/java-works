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


public class ParseCookies {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = null;
        int port = 3075;
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
    private static final String LOGIN_PAGE = "login.html";
    private static final String COLOR_PAGE = "color.html";

    private BufferedReader br;
    private OutputStream os;
    private Socket soc;
    private String savedColor;

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
            String colorValue = null;
            String value = null;
            

            while ((str = br.readLine()) != null) {
                System.out.println(str);
                if (str.startsWith("Host:")) {
                    host = str.split(" ")[1];
                }
                if (str.startsWith("GET")) {
                    String[] parts = str.split(" ");
                    if (parts.length > 1) {
                        requestedFile = parts[1].substring(1);
                        String question = "";
                        
                        if(requestedFile.contains("?")){
                            String[] parse = requestedFile.split("\\?");
                            requestedFile = parse[0];//color
                            question = parse[1];//Color=blue
                        }
                        if (requestedFile.isEmpty()) {
                            requestedFile = "index.html";
                        }
                        if(question.isEmpty()){
                            String[] keyValue = question.split("=");
                            if(keyValue.length == 2){
                                String key = keyValue[0];
                                value = keyValue[1]; 
                            }
                        }
                        
                    }
                    
                }
                if (str.isEmpty()) {
                    break;
                }
            }

            if (requestedFile.equals("login")) {
                serveFile("", LOGIN_PAGE);
            } else {
                serveColorPage(value);
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

    private void serveColorPage(String userSelectedColor) throws IOException {
        os.write("HTTP/1.1 200 OK\r\n".getBytes());
        os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
        os.write("\r\n".getBytes());
        
        if(userSelectedColor != null && !userSelectedColor.isEmpty()){
            String cookie = "Set-Cookie: SelectedColor =" + userSelectedColor + ";Path=/\r\n";
            os.write(cookie.getBytes());
        }
        // HTML form for color selection
        String response = "<html><body>";
        response += "<form>";
        response += "<h2><td>Select Your Favorite Color</td></h2>";
        response += "<h2><td>Color</td></h2>";
        response += "<td><input type=\"radio\" name=\"Color\" value=\"red\">red </input>";
        response += "<td><input type=\"radio\" name=\"Color\" value=\"blue\">blue</input>";
        response += "<td><input type=\"radio\" name=\"Color\" value=\"green\">green</input></td>";
        response += "<input type=\"submit\" value=\"Submit\">";
        response += "</form>";
        response += "</body></html>";

        os.write(response.getBytes());
        os.flush();
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
