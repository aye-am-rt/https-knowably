package com.stackroute.quartz.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Component
public class CronJobProducer extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(CronJobProducer.class);

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        KafkaTemplate<String,String> kt=this.kafkaTemplate;
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String subject = jobDataMap.getString("flagForScheduler");
        kt.send("SchedulerFlag",subject);
    }
}
