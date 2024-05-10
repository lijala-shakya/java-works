/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minnumber;

/**
 *
 * @author DELL
 */
public class SelectionSort {
    public static void main(String[] args) {
        int[] num = {3,9,1,5,6,7,4,8,2};
        for(int i=0; i<num.length-1; i++){
            int key = i;
            for(int j = i+1; j<num.length; j++){
                if(num[j] < num[key]){
                    key = j;
                }
            }
             int temp = num[i];
            num[i] = num[key];
            num[key] = temp;
        }
        for(int i=0; i<num.length; i++){
            System.out.print(num[i]+" ");
        }
    }
}
