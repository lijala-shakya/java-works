/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverapplication;

/**
 *
 * @author DELL
 */
import java.util.*;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Client {
    public static void main(String[] args) {
        // TODO code application logic here
        Socket soc =null;
        InputStreamReader inputStreamReader =null;
        
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader br =null;
        BufferedWriter bw = null;
        try{
            System.out.println("Client started...");
            soc = new Socket("localhost", 3000);
            inputStreamReader = new InputStreamReader(soc.getInputStream());
            outputStreamWriter = new OutputStreamWriter(soc.getOutputStream());
            br = new BufferedReader(inputStreamReader);
            bw = new BufferedWriter(outputStreamWriter);
            
            Scanner scan =new Scanner(System.in);
            
            while(true){
                String clientMessage = scan.nextLine();
                bw.write(clientMessage);
                bw.newLine();
                bw.flush();
                System.out.println("server: "+br.readLine());
                
                if(clientMessage.equalsIgnoreCase("Bye")){
                    break;
                }
                
            }
        }catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try{
                if(soc!=null){
                    soc.close();
                }
                if(inputStreamReader !=null){
                    inputStreamReader.close();
                }
                if(outputStreamWriter != null){
                    outputStreamWriter.close();
                }
                if(br != null){
                    br.close();
                }
                if(bw != null){
                    bw.close();
                }
            }catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex); 
            }
        }
    }
}
