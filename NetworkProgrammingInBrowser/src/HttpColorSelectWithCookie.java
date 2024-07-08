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

public class HTTPColorSelectWithCookie {
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
    private static final String LOGIN_PAGE = "index.html";
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
            String requestedFile = "index.html";
            String colorValue = null;
            String value = null;
            String cookieValue = null;

            while ((str = br.readLine()) != null) {
                System.out.println(str);
                if (str.startsWith("GET")) {
                    String[] parts = str.split(" ");
                    if (parts.length > 1) {
                        requestedFile = parts[1].substring(1);
                        String question = "";

                        if (requestedFile.contains("?")) {
                            String[] parse = requestedFile.split("\\?");
                            requestedFile = parse[0];
                            question = parse[1];
                        }
                        if (requestedFile.isEmpty()) {
                            requestedFile = "index.html";
                        }
                        if (!question.isEmpty()) {
                            String[] keyValue = question.split("=");
                            if (keyValue.length == 2) {
                                value = keyValue[1];
                            }
                        }
                    }
                }
                if (str.startsWith("Cookie:")) {
                    String[] cookies = str.split(";");
                    for (String cookie : cookies) {
                        if (cookie.trim().startsWith("SelectedColor=")) {
                            cookieValue = cookie.split("=")[1];
                        }
                    }
                }
                if (str.isEmpty()) {
                    break;
                }
            }

            if (requestedFile.equals("index.html")) {
                serveColorPage(value, cookieValue);
            } else {
                serveFile("", requestedFile);
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

    private void serveColorPage(String userSelectedColor, String savedColor) throws IOException {
    os.write("HTTP/1.1 200 OK\r\n".getBytes());
    os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());

    if (userSelectedColor != null && !userSelectedColor.isEmpty()) {
        String cookie = "Set-Cookie: SelectedColor=" + userSelectedColor + "; Path=/\r\n";
        os.write(cookie.getBytes());
        System.out.println("Set cookie for selected color: " + userSelectedColor);
    }

    os.write("\r\n".getBytes()); // End of headers

    String backgroundColor = (userSelectedColor != null && !userSelectedColor.isEmpty()) ? userSelectedColor
            : (savedColor != null ? savedColor : "white");

    System.out.println("Serving page with background color: " + backgroundColor);

    // HTML form for color selection
    String response = "<html><body style='background-color:" + backgroundColor + ";'>";
    response += "<form action=\"index.html\" method=\"GET\">";
    response += "<h2>Select Your Favorite Color</h2>";
    response += "<input type=\"radio\" name=\"Color\" value=\"red\">Red</input>";
    response += "<input type=\"radio\" name=\"Color\" value=\"blue\">Blue</input>";
    response += "<input type=\"radio\" name=\"Color\" value=\"green\">Green</input>";
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
