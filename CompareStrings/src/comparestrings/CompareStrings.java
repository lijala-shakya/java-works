/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package comparestrings;

/**
 *
 * @author DELL
 */
public class CompareStrings {

    public static void main(String[] args) {
        // TODO code application logic here
        String strA = "rch";
        String strB = "Searching through the string to find words containing 'ing' like searching, finding, and anything";

        if (find(strA, strB)) {
            System.out.println("string A is in atring B");
        } else {
            System.out.println("stirng A is not in string B");
        }
    }

    public static boolean find(String strA, String strB) {
        if (strA.length() > strB.length()) {
            System.out.println("length of string strA should be lesser than string strB.");
            return false;
        }

        for (int i = 0; i <= strB.length() - strA.length(); i++) {

            boolean match = true;

            for (int j = 0; j < strA.length(); j++) {
                if (strA.charAt(j) != strB.charAt(i + j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return true;
            }

        }
        return false;

    }

}
