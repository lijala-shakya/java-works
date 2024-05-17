/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minnumber;

/**
 *
 * @author DELL
 */
public class UnsortedWithN {
    
     public static void main(String[] args) {
         int[] arr = {9,1,5,7,3,2,6,8,4};
         int ans = 5;
         int left = 0;
         int right =arr.length-1;
         while(left < right ){
             int complement = ans - arr[left];
             if(complement == arr[right]){
                 System.out.println(arr[right]+","+arr[left]);
                 right --;
                 left ++;
             }
             else if(arr[right] < complement){
                 right --;
             }else{
                 left ++;
             }
        }
           
         
    }

}
