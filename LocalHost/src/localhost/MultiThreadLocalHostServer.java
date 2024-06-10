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

public class MultiThreadLocalHostServer {

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        ServerSocket ss = null;
        int port = 3077;
        System.err.println("Server is running in port: "+port);
        try{
            ss = new ServerSocket(port,0,InetAddress.getByName("0.0.0.0"));
            while(true){
                Socket soc = ss.accept();
                System.err.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(soc);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

class ClientHandler implements Runnable{
    private BufferedReader br;
    private InputStreamReader isr;
    private OutputStream os;
    private Socket  soc;
    
    public ClientHandler(Socket soc){
        this.soc = soc;
        try{
            isr = new InputStreamReader(soc.getInputStream());
            br = new BufferedReader(isr);
           
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try(FileReader reader = new FileReader("server.txt")){
            
            while(true){
                 String str;
                 while((str = br.readLine()) != null){
                    System.out.println(str);
                    if(str.isEmpty()){
                        break;
                    }
                }
                os = soc.getOutputStream();
               
                os.write("HTTP/1.1 200 OK\r\n".getBytes());
                os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                os.write("\r\n".getBytes());
                int character;
                while((character = reader.read()) != -1){
                    os.write(character);
                }
                os.write("\r\n".getBytes());
                os.flush();
                
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try{
                if(br != null) br.close();
                if(os != null) os.close();
                soc.close();
                System.err.println("Client connection closed!");
               
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
} 
//reference class localhost.java and class Server.java from project ServerMultipleClient