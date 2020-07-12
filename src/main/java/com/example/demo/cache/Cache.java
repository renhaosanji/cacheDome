package com.example.demo.cache;

public class Cache {

    private String key;//캐시ID
    private Object value;//캐시데이타
    private long timeOut;//업데이트 시간
    private boolean expired; //사용중지여부
    public Cache() {
    	super();
    }

    public Cache(String key, Object value, long timeOut, boolean expired) {
    		this.key = key;
    		this.value = value;
    		this.timeOut = timeOut;
    		this.expired = expired;
    }

    public String getKey() {
    	   return key;
    }

    public long getTimeOut() {
    	   return timeOut;
    }

    public Object getValue() {
    	   return value;
    }

    public void setKey(String string) {
           key = string;
    }

    public void setTimeOut(long l) {
           timeOut = l;
    }

    public void setValue(Object object) {
           value = object;
    }

    public boolean isExpired() {
           return expired;
    }

    public void setExpired(boolean b) {
           expired = b;
    }

}
