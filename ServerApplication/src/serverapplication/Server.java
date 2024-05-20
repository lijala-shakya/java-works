/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverapplication;

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
        Socket soc = null;
        ServerSocket ss = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            System.out.println("Waiting for the client....");
            ss = new ServerSocket(3000);

            soc = ss.accept();
            System.out.println("Connection established...");

            inputStreamReader = new InputStreamReader(soc.getInputStream());
            outputStreamWriter = new OutputStreamWriter(soc.getOutputStream());
            br = new BufferedReader(inputStreamReader);
            bw = new BufferedWriter(outputStreamWriter);

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

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
