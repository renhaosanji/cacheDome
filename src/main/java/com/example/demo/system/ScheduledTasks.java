package com.example.demo.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import com.example.demo.cache.CacheManager;
import com.example.demo.restfulController.RestfulController;




@Component
public class ScheduledTasks implements SchedulingConfigurer {
	
	private static final Logger LOG = LoggerFactory.getLogger(RestfulController.class);
	
	@Value("${cache.config.clearRandom.scheduledExpression}")
	private String scheduledExpression;
	@Value("${cache.config.scan.range}")
	private int range;
	

	/**
	 * Eviction  TTL（Time To Live ） 기업 사용 , 일정 시간에 기간 지난 캐시 삭제 
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		// TODO Auto-generated method stub
		
		String cron = scheduledExpression;
		
		taskRegistrar.addCronTask(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LOG.info("Cache clearRandom start");
		        CacheManager.clearRandom(range);
		        LOG.info("Cache clearRandom finished");
			}
		},cron);
		
	}
		
}
