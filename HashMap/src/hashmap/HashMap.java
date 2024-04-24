/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hashmap;

/**
 *
 * @author DELL

 */
public class HashMap<K, V> {
    public int size = 5;
   private Entry<K, V> table[];
   
    public class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;
    
        public Entry (K key, V value) {
            this.value = value;
            this.key = key;
        }
        public K getKey() {
            return this.key;
        }   
        public V getValue() {
            return this.value;
        }
        public void setValue(V value) {
            this.value = value;
        }
    }
     public HashMap() {
         table = new Entry[size];
     }
     public int hash(Object obj) {
        int index = Math.abs(obj.hashCode() % size);
        return index;
    }
     public void put(K key, V value) {
         int hashCode = hash(key);
         Entry<K, V> data = table[hashCode];
         if(data == null){
            table[hashCode]= new Entry<K, V>(key, value);
             size++;
         }else{
             while(data.next != null){
                 if(data.getKey() == null){
                     data.setValue(value);
                     return;
                 } 
                     data = data.next;
             }
             if(data.getKey() == null){
                     data.setValue(value);
                     return;
             }
             data.next = new Entry<>(key, value);
         }
     }
     public V get(K key) {
         int hashCode = hash(key);
         Entry<K, V> data = table[hashCode];
         if(data ==null){
             return null;
         }
         while(data != null){
             if(data.getKey() == key){
                 return data.getValue();
             }
             data = data.next;
         }
         return null;
     }
     
    
    public static void main(String[] args) {
      
    }
    
}
