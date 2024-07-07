

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

public class HttpColorSelectWithCookie {

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
            String cookieHeader = null;

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
                if (str.startsWith("Cookie:")) {
                    cookieHeader = str;
                }
                if (str.isEmpty()) {
                    break;
                }
            }

            parseCookies(cookieHeader);

            if (requestedFile.equals("login")) {
                serveFile("", LOGIN_PAGE);
            } else if (requestedFile.equals("color")) {
                serveColorPage();
            } else {
                if (savedColor == null) {
                    serveColorPage();
                } else {
                    serveSavedColorPage(savedColor);
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

    private void parseCookies(String cookieHeader) {
        if (cookieHeader != null) {
            String[] cookiePairs = cookieHeader.substring("Cookie: ".length()).split("; ");
            for (String cookiePair : cookiePairs) {
                String[] parts = cookiePair.split("=");
                if (parts.length == 2 && parts[0].trim().equals("color")) {
                    savedColor = parts[1].trim();
                    break;
                }
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

    private void serveColorPage() throws IOException {
        os.write("HTTP/1.1 200 OK\r\n".getBytes());
        os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
        os.write("\r\n".getBytes());

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

    private void serveSavedColorPage(String savedColor) throws IOException {
        os.write("HTTP/1.1 200 OK\r\n".getBytes());
        os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
        os.write(("Set-Cookie: color=" + savedColor + "\r\n").getBytes());
        os.write("\r\n".getBytes());

        // HTML page showing the saved color
        String response = "<html><body>";
        response += "<h2>Your Favorite Color:</h2>";
        response += "<p style=\"color:" + savedColor + ";\">" + savedColor + "</p>";
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
