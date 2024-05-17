/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minnumber;

/**
 *
 * @author DELL
 */
public class SumOfValuesWithN {
     public static void main(String[] args) {
         int[] arr = {1,2,3,4,5,6,7,8,9};
         int ans = 16;
         int left = 0;
         int right =arr.length-1;
         while(left < right){
             int sum = arr[right] + arr[left];
             if(sum == ans){
                 System.out.println("The values in array that give the answer are "+arr[left]+","+arr[right]);
                 left ++;
                 right --;
             }else if(sum<ans){
                 left ++;
             }else {
                 right --;
             }
         }
             
         
     }
}
