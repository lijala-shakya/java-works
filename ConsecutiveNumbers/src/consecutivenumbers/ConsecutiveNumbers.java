/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package consecutivenumbers;

/**
 *
 * @author DELL
 */
public class ConsecutiveNumbers {

    public static void main(String[] args) {
        // TODO code application logic here
        String num = "112255555553334";
        
        int maxCount = 0;
        int currentCount = 1;
        int lastIndex = 0;
        int firstIndex = 0;
        char mostFrequentChar ='\0' ;
        for(int i=0; i<num.length()-1; i++){
            if(num.charAt(i) == num.charAt(i+1)){
                currentCount++;
            }else{
                currentCount = 1;
            }
            if(currentCount > maxCount){
                maxCount = currentCount;
                mostFrequentChar = num.charAt(i);
                lastIndex = i + 2;
                firstIndex = i-maxCount+2;
            }
            
        }     
        System.out.println("Most frequently occuring character is: " + mostFrequentChar);
        System.out.println("Maximum length of consecutivrly occouring character is: " + maxCount);
        System.out.println("first index of consecutively occuring character is: "+firstIndex);
        System.out.println("last index of consecutively occuring character is: "+lastIndex);
        
    }
    
    
}
