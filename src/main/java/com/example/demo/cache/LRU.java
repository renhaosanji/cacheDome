package com.example.demo.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class LRU<K, V> {
	
	 private final int MAX_CACHE_SIZE;
     private final float DEFAULT_LOAD_FACTORY = 0.75f;
     LinkedHashMap<K, V> map;
     
     public LRU(int cacheSize) {
             MAX_CACHE_SIZE = cacheSize;
             int capacity = (int) (Math.ceil(MAX_CACHE_SIZE/DEFAULT_LOAD_FACTORY)+1);
             /**
              * @see java.util.LinkedHashMap
              */
             // LinkedHashMap 생성할 떄 cacheSize 때라서 사이즈 정함, 데이터 추가한 후에 eldest된 데이터 삭제함
             map = new LinkedHashMap<K, V>(capacity, DEFAULT_LOAD_FACTORY, true){
                     protected boolean removeEldestEntry(java.util.Map.Entry<K,V> eldest) {
                             return size() > MAX_CACHE_SIZE;
                     };
             };
     }
     
     public synchronized void put(K key, V value){
             map.put(key, value);
     }
     
     public synchronized V get(K key){
             return map.get(key);
     }
     
     public synchronized void remove(K key){
             map.remove(key);
     }
     
     public synchronized boolean containsKey(K key){
         return map.containsKey(key);
     }     
     
     
     public synchronized void clear(){
         map.clear();
     }    
     
     public synchronized Set<Entry<K, V>> entrySet(){
         return map.entrySet();
     }    
     
     public synchronized int size(){
         return map.size();
     }    
     
     
     public synchronized Set<Map.Entry<K, V>> getAll(){
             return map.entrySet();
     }
     
     @Override
     public String toString() {
             StringBuilder sb = new StringBuilder();
             for(Map.Entry<K, V> entry : map.entrySet())
                     sb.append(String.format("%s:%s ", entry.getKey(), entry.getValue()));
             return sb.toString();
     }

}
