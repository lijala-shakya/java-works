/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minnumber;

/**
 *
 * @author DELL
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class MyServer {
    public static void main(String[] args) {
         
        try {
            ServerSocket ss = new ServerSocket(3000);
            ss.accept();
            } catch (IOException ex) {
            Logger.getLogger(MyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //chatting syatem
    }
}
