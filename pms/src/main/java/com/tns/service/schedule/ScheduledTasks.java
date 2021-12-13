package com.tns.service.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tns.service.PromoCodeService;

@Component
public class ScheduledTasks {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	@Autowired
	PromoCodeService promoCodeService;

	@Scheduled(cron = "${promocode.cron.expression}")
	public void promoCodeGeneration() {
		logger.info("Start Promo Code Generation Schedule.");
		promoCodeService.generate();
		logger.info("End Promo Code Generation Schedule.");
	}
}
