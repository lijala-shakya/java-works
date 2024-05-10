/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minnumber;

/**
 *
 * @author DELL
 */
public class IndexOf {
    public static int findIndex(int[] numbers, int value){
        for(int i=0; i<numbers.length; i++){
            if(numbers[i] == value){
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        String num = "1,0,1,2,3,4";
        int index = IndexOf.findIndex(new int[] {1, 3, 5, 7, 9, 8, 6, 4, 2} , 7);
        System.out.println("The index of value 7 is: "+index);
        index = IndexOf.findIndex(new int[]{1, 3, 5, 7, 9, 8, 6, 4, 2} , 21);
        System.out.println("The index of value 7 is: "+index);
        System.out.println(num.charAt(2));
        System.out.println(num.charAt(4));
        System.out.println(num.charAt(6));
        System.out.println(num.charAt(1));//string ra char given char ko string ma index  eg hello world ma h ko index 0 return hunu pryo ani e ko index 1
        //2ta sorted array deko xa bhane dubailai merge garera sort garne eg{1 5 8 7} {2 3 4 6} ={1 2 3 4 5 6 7 8}
    }
}
