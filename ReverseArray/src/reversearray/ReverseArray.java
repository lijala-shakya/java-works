/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package reversearray;

/**
 *
 * @author DELL
 */
public class ReverseArray {
    public static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
    public static void swapFirstLast(int[] array) {
        if (array.length >= 2) {
            int temp = array[0];
            array[0] = array[array.length - 1];
            array[array.length - 1] = temp;
        }
    }
    public static void sortArray(int[] array) {
        int temp;
        for(int i=0; i<array.length-1; i++) {
            for(int j=0; j<array.length-i-1; j++) {
                if(array[j] > array[j + 1]){
                    temp = array [j];
                    array[j] = array[j+1];
                    array [j+1] = temp;
                }
            }
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        int[] array = {2,1,8,9,4,10,15,0,5,6,25};
        int length = array.length;
        
        int temp;
        
        System.out.println("Original Array :");
        printArray(array);
        
        swapFirstLast( array);
        System.out.println("Swapped Array:");
        printArray(array);
        
        System.out.println("Reversed Array:");
        for(int i=0; i<length/2; i++) {
            temp = array[i];
            array[i] = array[array.length-1-i];
            array[array.length-1-i] = temp;   
        }
        printArray(array);
        
        System.out.println("Sorted Array:");
        sortArray(array);
        printArray(array);
        
    }
    
}
