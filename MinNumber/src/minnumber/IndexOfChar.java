/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minnumber;

/**
 *
 * @author DELL
 */
public class IndexOfChar {

    private static int findIndex(String num, char value) {
        for (int i = 0; i < num.length(); i++) {
            if (num.charAt(i) == value) {
                return i;
            }
        }
        return -1;
    }
//implementation of bubble sort ascending/decending order
    //selection sort
    public static void main(String[] args) {
        String num = "1,0,1,2,3,4,5,6,7,8,9";
        char value = '3';
        int index = IndexOfChar.findIndex(num, value);
        System.out.println(index);
    }
}
