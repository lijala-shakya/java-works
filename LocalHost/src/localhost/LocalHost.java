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

public class LocalHost {

    public static void main(String[] args) throws IOException{
        int port = 3071;
        ServerSocket ss = null;
        BufferedReader br = null;
        Socket  soc = null;
        System.err.println("Server is running in port: "+port);
        try(FileReader reader = new FileReader("server.txt")){
            ss = new ServerSocket(port,0,InetAddress.getByName("0.0.0.0"));
            while(true){
                soc = ss.accept();
                System.err.println("Client connected");
                InputStreamReader isr = new InputStreamReader(soc.getInputStream());
                br = new BufferedReader(isr);
                String str;
                while((str = br.readLine()) != null){
                    System.out.println(str);
                    if(str.isEmpty()){
                        break;
                    }
                }
                OutputStream os = soc.getOutputStream();
                try{
                    os.write("HTTP/1.1 200 OK\r\n".getBytes());
                    os.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                    //UTF bhane ASCII jastai UTF-32
                    os.write("\r\n".getBytes());
                    int character;
                    while((character = reader.read()) != -1){
                        os.write(character);
                    }
                    os.write("\r\n".getBytes());
                    os.flush();
                }catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    System.err.println("Client connection closed!");
                    br.close();
                    os.close();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }  
}
//references: youtube:-https://www.youtube.com/watch?v=lCNUsi4Qfuw
//github:- https://github.com/malinashakya/Java/blob/main/src/javacodes/localhost/SimpleHttpServer.java
