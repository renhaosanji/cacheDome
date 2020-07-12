package com.example.demo.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.cache.LRU;

@Configuration
public class CacheConfig {
	
	@Value("${cache.config.LRUMax}")
	private int LRUMax;
	
	@Bean
	public LRU lru(){
		return new LRU(LRUMax);
	}

}
