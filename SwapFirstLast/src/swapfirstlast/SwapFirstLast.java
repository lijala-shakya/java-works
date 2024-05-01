/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package swapfirstlast;

/**
 *
 * @author DELL
 */
public class SwapFirstLast {
    public static void swap(char[] str) {
        int first = 0;
        
        int count = 0;
        for(int i=0; i<str.length; i++){
            
            if(str[i] == ' '){
                int last = i-1;
                char temp = str[first];
                str[first] = str[last];
                str[last] = temp;
                
                
                first = i+1;
                
                count++;
                System.out.println(first);
            }
            else if (i == str.length - 1) {
                int last = i;
                char temp = str[first];
                str[first] = str[last];
                str[last] = temp;
                  
            }
        }
        System.out.println(count);
        System.out.println(str);
        
    }
    public static boolean isDgit(char[] str) {
        int first = 0;
       
        boolean digit = false;
        
        for(int i=0; i<str.length; i++){
            if(Character.isDigit(str[i])){
                digit = true;
            }
            if (str[i] == ' ') {
                if (!digit) {
                    for (int j = first; j < i; j++) {
                        if (!Character.isDigit(str[j])) {
                            int last = i - 1;
                            char temp = str[first];
                            str[first] = str[last];
                            str[last] = temp;
                            break;
                        }
                    }
                }
                first = i + 1;
                digit = false; 
            }
            
            else if (i == str.length - 1) {
                if (!digit) {
                    for(int j = first; j <= i; j++) {
                        if (!Character.isDigit(str[j])) {
                            int last = i;
                            char temp = str[first];
                            str[first] = str[last];
                            str[last] = temp;
                            break;
                        }
                    }
                }
            }
            
        }
        return digit;    
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        String originalString = "hello w0rld I am Lijala";
         swap(originalString.toCharArray());
        if(!isDgit(originalString.toCharArray())){
            System.out.println(originalString);
        }
        
    }
    
}
//char array bata matra euta parageaph dida sequence of letter xa ki xaina detect garne kunai euta sequence xa bhane false

