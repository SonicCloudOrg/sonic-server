package com.sonic.task.quartz;

import com.sonic.task.models.Jobs;
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
     * @param jobs
     * @return void
     * @author ZhouYiXun
     * @des 创建定时任务
     * @date 2021/8/21 17:40
     */
    public void createScheduleJob(Scheduler scheduler, Jobs jobs) {
        try {
            Class<? extends Job> jobClass = QuartzJob.class;
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobs.getId() + "").build();
            jobDetail.getJobDataMap().put("id", jobs.getId());
            //withMisfireHandlingInstructionDoNothing不触发立即执行
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobs.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobs.getId() + "").withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("创建定时任务成功");
        } catch (SchedulerException e) {
            logger.error("创建定时任务出错：" + e.getMessage());
        }
    }

    /**
     * @param scheduler
     * @param jobs
     * @return void
     * @author ZhouYiXun
     * @des 暂停定时任务
     * @date 2021/8/21 17:42
     */
    public void pauseScheduleJob(Scheduler scheduler, Jobs jobs) {
        JobKey jobKey = JobKey.jobKey(jobs.getId() + "");
        try {
            scheduler.pauseJob(jobKey);
            logger.info("暂停定时任务成功");
        } catch (SchedulerException e) {
            logger.error("暂停定时任务出错：" + e.getMessage());
        }
    }

    /**
     * @param scheduler
     * @param jobs
     * @return void
     * @author ZhouYiXun
     * @des 重启定时任务
     * @date 2021/8/21 17:43
     */
    public void resumeScheduleJob(Scheduler scheduler, Jobs jobs) {
        JobKey jobKey = JobKey.jobKey(jobs.getId() + "");
        try {
            scheduler.resumeJob(jobKey);
            logger.info("重启定时任务成功");
        } catch (SchedulerException e) {
            logger.error("重启定时任务出错：" + e.getMessage());
        }
    }

    /**
     * @param scheduler
     * @param jobs
     * @return void
     * @author ZhouYiXun
     * @des 更新定时任务
     * @date 2021/8/21 17:43
     */
    public void updateScheduleJob(Scheduler scheduler, Jobs jobs) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobs.getId() + "");
            //withMisfireHandlingInstructionDoNothing不触发立即执行
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobs.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
            logger.info("更新定时任务成功");
        } catch (SchedulerException e) {
            logger.error("更新定时任务出错：" + e.getMessage());
        }
    }

    /**
     * @param scheduler
     * @param jobs
     * @return void
     * @author ZhouYiXun
     * @des 删除定时任务
     * @date 2021/8/21 17:44
     */
    public void deleteScheduleJob(Scheduler scheduler, Jobs jobs) {
        JobKey jobKey = JobKey.jobKey(jobs.getId() + "");
        TriggerKey triggerKey = TriggerKey.triggerKey(jobs.getId() + "");
        try {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
            logger.info("删除定时任务成功");
        } catch (SchedulerException e) {
            logger.error("删除定时任务出错：" + e.getMessage());
        }
    }
}