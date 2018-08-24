package com.taobao.brand.bear.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.taobao.brand.bear.job.ExampleJob;
import com.taobao.brand.bear.utils.ThreadUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;

/**
 * @author jinshuan.li 2018/8/24 18:47
 */
@EnableScheduling
@Configuration
public class BeansConfig {

    public static SchedulerFactoryBean scheduler;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        scheduler = schedulerFactoryBean;
        return schedulerFactoryBean;
    }

    @PostConstruct
    public void init() throws SchedulerException {

        SimpleTriggerImpl simpleTrigger = new SimpleTriggerImpl();
        simpleTrigger.setStartTime(DateUtils.addSeconds(new Date(), -10));
        simpleTrigger.setName("triggetName1");
        JSONObject data = new JSONObject();
        data.put("name", "jinshuan.li");
        simpleTrigger.setJobDataMap(new JobDataMap(data));
        simpleTrigger.setTimesTriggered(1);

        JobDetailImpl jobDetail = new JobDetailImpl("jobName2", ExampleJob.class);

        schedulerFactoryBean().getScheduler().scheduleJob(jobDetail, simpleTrigger);

        ThreadUtils.runAsync(() -> {

            while (true) {

                System.out.println("aaa");
                ThreadUtils.sleep(2000);
                return 1L;
            }
        });
    }
}
