/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package servermultipleclient;

/**
 *
 * @author DELL
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    public static void main(String[] args) {
        ServerSocket ss = null;

        try {
            System.out.println("Waiting for the client....");
            ss = new ServerSocket(3000);

            while (true) {
                Socket soc = ss.accept();
                System.out.println("Connection established...");
                ClientHandler clientHandler = new ClientHandler(soc);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class ClientHandler implements Runnable {
    private Socket soc;
    private BufferedReader br;
    private BufferedWriter bw;
    private InputStreamReader inputStreamReader;
    private OutputStreamWriter outputStreamWriter;

    public ClientHandler(Socket soc) {
        this.soc = soc;
        try {
            inputStreamReader = new InputStreamReader(soc.getInputStream());
            outputStreamWriter = new OutputStreamWriter(soc.getOutputStream());
            br = new BufferedReader(inputStreamReader);
            bw = new BufferedWriter(outputStreamWriter);
        } catch (IOException e) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String clientMessage = br.readLine();
                System.out.println("Client: " + clientMessage);

                System.out.print("Server: ");
                String serverMessage = scanner.nextLine();
                bw.write(serverMessage);
                bw.newLine();
                bw.flush();

                if (serverMessage.equalsIgnoreCase("Bye")) {
                    break;
                }
            }
        } catch (IOException e) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (soc != null) {
                    soc.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (br != null) {
                    br.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
//reference using youtube and google