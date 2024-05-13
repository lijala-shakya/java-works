/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package consecutivenumbers;

/**
 *
 * @author DELL
 */
public class BubbleSort {
    
    public static void main(String[] args) {
        int[] num = {9,3,13,7,10,5,6,0,8};
        for (int i=0; i<num.length-1; i++){
           for(int j=0; j<num.length-i-1; j++){
               if(num[j] > num[j+1]){
                  int temp = num[j];
                  num[j] = num[j+1];
                  num[j+1] = temp;
               }
           }
        }
        for(int arr:num){
            System.out.print(arr+" ");
        }
        
    }
}
