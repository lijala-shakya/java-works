/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minnumber;

/**
 *
 * @author DELL
 */
public class SumOfValues {
     public static void main(String[] args) {
         int[] arr = {1,2,3,4,5,6,7,8,9};
         int ans = 16;
         for(int i=0; i<arr.length; i++){
             for(int j=i+1; j<arr.length; j++){
                 if(arr[i] + arr[j] == ans){
                     System.out.println("Numbers that gives "+ans+" when added are "+arr[i]+" and "+arr[j]);
                 }
                 
             }
         }
     }
}
