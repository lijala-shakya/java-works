/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minnumber;

/**
 *
 * @author DELL
 */
public class ThreadTest extends Thread{
    static int count = 0;
    public void run(){
//        System.out.println("Hello Universe");
        count ++;
    }
    public static void main(String[] args) {
        
        ThreadTest t = new ThreadTest();
        t.start();
        System.out.println(count);
        count ++;
        System.out.println(count);
    }
}
