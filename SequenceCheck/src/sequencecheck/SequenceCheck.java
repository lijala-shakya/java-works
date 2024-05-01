/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sequencecheck;

/**
 *
 * @author DELL
 */

public class SequenceCheck {
   
    public static void main(String[] args) {
        String inputString = "Searching through the string to find words containing 'ing' like searching, finding, and anything.";
                int start = -1;

        boolean found = false;

        for (int i = 0; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            
            if (Character.isLetter(currentChar) ) {
                if (start == -1) {
                    start = i;
                }
            } else {
                if (start != -1) {
                    if (inputString.substring(start, i).endsWith("ing")) {
                        found = true;
                        break;
                    }
                    start = -1; 
                }
            }
        }
//        ona a string 1 and 2. string 2 should be greater than string 1  then check if string 2 contains the string of string 1 if found true else false (2<1) 1 ma 2 xa ki xaina without using contain()
        

        if (found) {
            System.out.println("Input string contains a word ending with 'ing'.");
        } else {
            System.out.println("Input string does not contain a word ending with 'ing'.");
        }
    }
}

    
    

    

