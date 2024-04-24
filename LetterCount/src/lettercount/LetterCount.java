/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lettercount;

/**
 *
 * @author DELL
 */
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class LetterCount {
    static void characterCount(String inputString) {
        HashMap<Character, Integer> charCount = new HashMap<Character, Integer>();
        char[] strArray = inputString.toCharArray();
        
        for (char c : strArray) {
            if (charCount.containsKey(c)) {
                charCount.put(c, charCount.get(c) + 1);
            }
            else {
                charCount.put(c, 1);
            }
        }
        
        for (Map.Entry entry : charCount.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
//        int MAX_LENGTH = 100;
//        char[] word = new char[MAX_LENGTH);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word: ");
        String input = scanner.next();
//        int length = input.length();
        
        characterCount(input);


    }
    
}
