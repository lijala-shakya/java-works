/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package minnumber;

/**
 *
 * @author DELL
 */
public class MinNumber {

    public static void main(String[] args) {
        int[] numbers = {7,9,5,3,4,2,1,6,8};
        int right = 0;
        int value = 0;
        for(int left=0; left<numbers.length; left++){
            if(numbers[left]<numbers[right]){
                right = left;
                value = numbers[left];
            }
        }
        System.out.println(right);
        System.out.println(value);
    }
    
}
