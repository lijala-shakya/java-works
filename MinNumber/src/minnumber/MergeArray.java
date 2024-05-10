/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minnumber;

/**
 *
 * @author DELL
 */
public class MergeArray {
    public static int[] merge(int[] arr1, int[] arr2){
        int[] ans = new int[arr1.length + arr2.length];
        int i = 0;
        int j = 0;
        int k = 0;
        while(i<arr1.length && j<arr2.length){
            if(arr1[i] < arr2[j]){
                ans[k++] = arr1[i++];
            }
            else{
                ans[k++] = arr2[j++];
            }
        }
        return ans;
    }
     public static void main(String[] args) {
         int[] arr1 = {2,4,6,8};
         int[] arr2 = {1,3,5,7,9};
         
         int[] arr3 = merge(arr1, arr2);
         for(int i=1; i<=arr3.length; i++){
             System.out.print(i + ",");
             
         }
     }
}
