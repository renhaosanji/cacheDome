package com.example.demo.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CacheManager {


    private static LRU cacheMap; // 캐시는 hashmap로 구성하여 LRU를 준수합니다

    public static LRU getCacheMap() {
		return cacheMap;
	}

    /**  
     * @Title: setCacheMap
     * @Description: TODO(설명) cache config에서 만들어진 LRU를 주입
     * @param cacheMap
     * @author renhs
     * @date 2020-07-12 03:00:14 
     */  
    @Autowired  
	public void setCacheMap(LRU cacheMap) {
		CacheManager.cacheMap = cacheMap;
	}
    
    // 시글돈, 호출시  메모리에 cacheMap생성 후 유지
    private CacheManager() {
    	super();
    }
    
 
    /**  
     * @Title: getSimpleFlag
     * @Description: TODO(설명) java 기분 type boolean 캐시로 취득할  때
     * @param key
     * @return
     * @author renhs
     * @date 2020-07-12 02:59:44 
     */  
    public static boolean getSimpleFlag(String key){
        try{
            return (Boolean) cacheMap.get(key);
        }catch(NullPointerException e){
            return false;
        }
    }
    
    /**  
     * @Title: getServerStartdt
     * @Description: TODO(설명) java 기분 type long 캐시로 취득할 때
     * @param key
     * @return
     * @author renhs
     * @date 2020-07-12 02:58:24 
     */  
    public static long getServerStartdt(String key){
    	try {
    		return (Long)cacheMap.get(key);
    	} catch (Exception ex) {
    		return 0;
    	}
    }

   
    /**  
     * @Title: setSimpleFlag
     * @Description: TODO(설명) java 기분 type boolean 캐시로 저장할 때
     * @param key
     * @param flag
     * @return
     * @author renhs
     * @date 2020-07-12 03:02:04 
     */  
    public synchronized static boolean setSimpleFlag(String key,boolean flag){

        if (flag && getSimpleFlag(key)) {// 존재하면 덮어쓰지 않음
        	return false;
        }else{
        	cacheMap.put(key, flag);
        	return true;
        }

    }

    /**  
     * @Title: setSimpleFlag
     * @Description: TODO(설명) java 기분 type long 캐시로 저장할 때
     * @param key
     * @param serverbegrundt
     * @return
     * @author renhs
     * @date 2020-07-12 03:06:43 
     */  
    public synchronized static boolean setSimpleFlag(String key,long serverbegrundt){
    	if (cacheMap.get(key) == null) {
    		cacheMap.put(key,serverbegrundt);
    		return true;
    	}else{
    		return false;
    	}
    }

  

  

    /**  
     * @Title: getCache
     * @Description: TODO(설명) 캐시 취득
     * @param key
     * @return
     * @author renhs
     * @date 2020-07-12 03:07:52 
     */  
    private synchronized static Cache getCache(String key) {
    	return (Cache) cacheMap.get(key);
    }

  


    /**  
     * @Title: hasCache
     * @Description: TODO(설명) 캐시 존재 여부
     * @param key
     * @return
     * @author renhs
     * @date 2020-07-12 03:08:34 
     */  
    private synchronized static boolean hasCache(String key) {
    	return cacheMap.containsKey(key);
    }


    /**  
     * @Title: clearAll
     * @Description: TODO(설명) 캐시 삭제
     * @author renhs
     * @date 2020-07-12 03:09:05 
     */  
    public synchronized static void clearAll() {
    	cacheMap.clear();
    }

  
    /**  
     * @Title: clearAll
     * @Description: TODO(설명) string 조회후 삭제
     * @param type
     * @author renhs
     * @date 2020-07-12 03:09:35 
     */  
    public synchronized static void clearAll(String type) {
        Iterator i = cacheMap.entrySet().iterator();
        String key;
        ArrayList arr = new ArrayList();
        try {
            while (i.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
                key = (String) entry.getKey();
                if (key.startsWith(type)) { //조회 조건이랑 맞으면 삭제
                    arr.add(key);
                }
            }
            for (int k = 0; k < arr.size(); k++) {
                clearOnly((String)arr.get(k));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**  
     * @Title: clearRandom
     * @Description: TODO(설명) 랜덤으로 삭제
     * @param maxCount
     * @author renhs
     * @date 2020-07-12 03:10:30 
     */  
    public synchronized static void clearRandom(int maxCount) {
    	
    	Random random = new Random();
    	ArrayList arr = new ArrayList();
    	String key;
    	int count = 0;
    	// 모든 케시 조회
    	for (Iterator iterator = cacheMap.entrySet().iterator(); iterator.hasNext();) {
			java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
    		key = (String) entry.getKey();
     		if (random.nextBoolean()) { // 조회된 캐시 랜뎀으로 뽑기
    			arr.add(key);
    		}    		
    		if (count>2*maxCount) { // 한번에 삭제할 개수되면 정지
				break;
			}   		
    		count++;
		}
        for (int k = 0; k < arr.size(); k++) {
            String key2 = (String)arr.get(k);
        	if (cacheExpired((Cache)cacheMap.get(key2))) { // 캐시 생명주기 확인
        		clearOnly(key2); // 생명주기 만룓한 캐시 삭제
			}
        }

    }


    /**  
     * @Title: clearOnly
     * @Description: TODO(설명) 지정된 캐시를 삭제
     * @param key
     * @author renhs
     * @date 2020-07-12 03:14:00 
     */  
    public synchronized static void clearOnly(String key) {
    	cacheMap.remove(key);
    }

    /**  
     * @Title: putCache
     * @Description: TODO(설명) 캐시 로딩
     * @param key
     * @param obj
     * @author renhs
     * @date 2020-07-12 03:14:53 
     */  
    public synchronized static void putCache(String key, Cache obj) {
    	cacheMap.put(key, obj);
    }

  
    /**  
     * @Title: getCacheInfo
     * @Description: TODO(설명) 캐시정보 취득
     * @param key
     * @return
     * @author renhs
     * @date 2020-07-12 03:15:33 
     */  
    public static Cache getCacheInfo(String key) {
    if (hasCache(key)) {
        Cache cache = getCache(key);
        if (cacheExpired(cache)) { // 사용중지인지 체크
            cache.setExpired(true);
            }
            return cache;
        }else
         return null;
    }


    /**  
     * @Title: putCacheInfo
     * @Description: TODO(설명) 캐시정보 저장
     * @param key
     * @param obj
     * @param dt 생존시간
     * @param expired 중지여부
     * @author renhs
     * @date 2020-07-12 03:18:33 
     */  
    public static void putCacheInfo(String key, Object obj, long dt,boolean expired) {
        Cache cache = new Cache();
        cache.setKey(key);
        cache.setTimeOut(dt + System.currentTimeMillis()); //생존시간
        cache.setValue(obj);
        cache.setExpired(expired); 
        cacheMap.put(key, cache);
    }


    /**  
     * @Title: putCacheInfo
     * @Description: TODO(설명) putCacheInfo 오버라이트. Expired false
     * @param key
     * @param obj
     * @param dt
     * @author renhs
     * @date 2020-07-12 03:20:28 
     */  
    public static void putCacheInfo(String key,Cache obj,long dt){

    	Cache cache = new Cache();
        cache.setKey(key);
        cache.setTimeOut(dt+System.currentTimeMillis());
        cache.setValue(obj);
        cache.setExpired(false);
        cacheMap.put(key,cache);

    }



    /**  
     * @Title: cacheExpired
     * @Description: TODO(설명) 사용중지 여부 확인
     * @param cache
     * @return
     * @author renhs
     * @date 2020-07-12 03:21:23 
     */  
    public static boolean cacheExpired(Cache cache) {
    	if (null == cache) { // 캐시 없을때
    		return false;
    	}
    	long nowDt = System.currentTimeMillis(); //지금 시각
    	long cacheDt = cache.getTimeOut(); //캐시 생명시간 
    	if (cacheDt <= 0||cacheDt>nowDt) { //생존기간 0보다 적을때,무료화 기간 지금보다 큰때 -->FALSE
    		return false;
    	} else { //반대하면 기간지난, 무효화 됨
    		return true;
    	}
    }



    /**  
     * @Title: getCacheSize
     * @Description: TODO(설명) 캐시 사이즈
     * @return
     * @author renhs
     * @date 2020-07-12 03:24:43 
     */  
    public static int getCacheSize() {

        return cacheMap.size();

    }

    //获取指定的类型的大小

    /**  
     * @Title: getCacheSize
     * @Description: TODO(설명) 조회된 결과 사이즈
     * @param type 
     * @return
     * @author renhs
     * @date 2020-07-12 03:24:59 
     */  
    public static int getCacheSize(String type) {

        int k = 0;
        Iterator i = cacheMap.entrySet().iterator();
        String key;
        try {
        	while (i.hasNext()) {
        		java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
        		key = (String) entry.getKey();
        		if (key.indexOf(type) != -1) { //맞으면 카운딩 처리
        			k++;
        		}
        	}
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return k;

    }


    /**  
     * @Title: getCacheAllkey
     * @Description: TODO(설명) key 이름 가져오기
     * @return
     * @author renhs
     * @date 2020-07-12 03:26:55 
     */  
    public static ArrayList getCacheAllkey() {
    	ArrayList a = new ArrayList();
    	try {
    		Iterator i = cacheMap.entrySet().iterator();
    		while (i.hasNext()) {
    			java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
    			a.add((String) entry.getKey());
    		}
    	} catch (Exception ex) 
    	  {} 
    	  finally {
    		return a;
    	}
    }



    /**  
     * @Title: getCacheListkey
     * @Description: TODO(설명) 조회된 결과 key 가져오기
     * @param type
     * @return
     * @author renhs
     * @date 2020-07-12 03:28:32 
     */  
    public static ArrayList getCacheListkey(String type) {
    	ArrayList a = new ArrayList();
    	String key;
    	try {
    		Iterator i = cacheMap.entrySet().iterator();
    		while (i.hasNext()) {
    			java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
    			key = (String) entry.getKey();
    			if (key.indexOf(type) != -1) {
    				a.add(key);
    			}
    		}
    	} catch (Exception ex) {} finally {
    		return a;
    	}

    }

  

}