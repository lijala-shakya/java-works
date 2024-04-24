/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package linkedlist;

/**
 *
 * @author DELL
 */
class Node {
    int data;
    Node next;
    
}
public class LinkedList {
    private Node head;
    private int size;
    
    public void add (int data) {
        Node newNode = new Node();
        newNode.data = data;
        newNode.next = null;
        if(head == null){
            head = newNode;
        }
        else{
            Node currentNode = head;
            while (currentNode.next != null){
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size ++;
    }    
    public int size(){
         return size;
     }
//    
    
     public void removeAtFront(){
         if(head == null){
             System.out.println("Empty list.");
         }
         head = head.next;
         size--;
     }
     public void removeAtEnd() {
         if(head == null){
             System.out.println("Empty list.");
         }
         if (head.next == null) {
            head = null;
         }
     
            Node currentNode = head;
            while (currentNode.next.next != null){
                currentNode = currentNode.next;
            }
            currentNode.next = null;
         size--;
     }
     public void removeAt(int index){
         if(head == null ||  index<0  ){
             System.out.println("Empty list");
         }
        
        Node currentNode = head;
        for(int count=0; count<index-1; count++){
            
            currentNode = currentNode.next;
        }
        if (index == 0) {
            head = head.next; 
            size--;
            return;
         }else {
        currentNode.next = currentNode.next.next;
        size --;
        }
     }
     
     
     public void display (){
         Node currentNode = head;
         while (currentNode != null){
            System.out.println(currentNode.data + "");
            currentNode = currentNode.next;
         }
     }
     
     public void reverse(){
         if(head == null || head.next == null){
             return;
         }
         Node prevNode = null;
         Node currentNode = head;
         
         while(currentNode != null){
             Node nextNode = currentNode.next;
             currentNode.next = prevNode;
            
             prevNode = currentNode;
             currentNode = nextNode;
         }
         head = prevNode;
     }
     

    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
//        list.display();
//        list.reverse();
        list.display();
        System.out.println(" "+list.size());
//        list.removeAtFront();
//        list.removeAtEnd();
        list.removeAt(0);
        list.display();
        System.out.println(" "+list.size()); 
        
        
    }
}
