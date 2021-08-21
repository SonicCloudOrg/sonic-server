package com.sonic.task.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ZhouYiXun
 * @des quartz处理类
 * @date 2021/8/21 17:08
 */
@Component
public class QuartzHandler {
    private final Logger logger = LoggerFactory.getLogger(QuartzHandler.class);

    /**
     * @param scheduler
     * @param className
     * @param id
     * @param cron
     * @return void
     * @author ZhouYiXun
     * @des 创建定时任务
     * @date 2021/8/21 17:40
     */
    public void createScheduleJob(Scheduler scheduler, String className, int id, String cron) {
        try {
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(className);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(className + "-" + id).build();
            jobDetail.getJobDataMap().put("id", id);
            //withMisfireHandlingInstructionDoNothing不触发立即执行
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(className + "-" + id).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("创建定时任务成功");
        } catch (SchedulerException | ClassNotFoundException e) {
            logger.info("创建定时任务出错：" + e.getMessage());
        }
    }

    /**
     * @param scheduler
     * @param className
     * @param id
     * @return void
     * @author ZhouYiXun
     * @des 暂停定时任务
     * @date 2021/8/21 17:42
     */
    public void pauseScheduleJob(Scheduler scheduler, String className, int id) {
        JobKey jobKey = JobKey.jobKey(className + "-" + id);
        try {
            scheduler.pauseJob(jobKey);
            logger.info("暂停定时任务成功");
        } catch (SchedulerException e) {
            logger.info("暂停定时任务出错：" + e.getMessage());
        }
    }

    /**
     * @param scheduler
     * @param className
     * @param id
     * @return void
     * @author ZhouYiXun
     * @des 重启定时任务
     * @date 2021/8/21 17:43
     */
    public void resumeScheduleJob(Scheduler scheduler, String className, int id) {
        JobKey jobKey = JobKey.jobKey(className + "-" + id);
        try {
            scheduler.resumeJob(jobKey);
            logger.info("重启定时任务成功");
        } catch (SchedulerException e) {
            logger.info("重启定时任务出错：" + e.getMessage());
        }
    }

    /**
     * @param scheduler
     * @param className
     * @param id
     * @param cron
     * @return void
     * @author ZhouYiXun
     * @des 更新定时任务
     * @date 2021/8/21 17:43
     */
    public void updateScheduleJob(Scheduler scheduler, String className, int id, String cron) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(className + "-" + id);
            //withMisfireHandlingInstructionDoNothing不触发立即执行
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
            logger.info("更新定时任务成功");
        } catch (SchedulerException e) {
            logger.info("更新定时任务出错：" + e.getMessage());
        }
    }

    /**
     * @param scheduler
     * @param className
     * @param id
     * @return void
     * @author ZhouYiXun
     * @des 删除定时任务
     * @date 2021/8/21 17:44
     */
    public void deleteScheduleJob(Scheduler scheduler, String className, int id) {
        JobKey jobKey = JobKey.jobKey(className + "-" + id);
        TriggerKey triggerKey = TriggerKey.triggerKey(className + "-" + id);
        try {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
            logger.info("删除定时任务成功");
        } catch (SchedulerException e) {
            logger.info("删除定时任务出错：" + e.getMessage());
        }
    }
}