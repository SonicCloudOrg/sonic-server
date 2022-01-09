package org.cloud.sonic.task.quartz;

import org.cloud.sonic.task.models.domain.Jobs;
import org.cloud.sonic.task.models.interfaces.JobType;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ZhouYiXun
 * @des quartz处理类
 * @date 2021/8/21 17:08
 */
@Component
public class QuartzHandler {
    private final Logger logger = LoggerFactory.getLogger(QuartzHandler.class);
    @Autowired
    private Scheduler scheduler;

    /**
     * @param jobs
     * @return void
     * @author ZhouYiXun
     * @des 创建定时任务
     * @date 2021/8/21 17:40
     */
    public void createScheduleJob(Jobs jobs) throws SchedulerException {
        try {
            Class<? extends Job> jobClass = QuartzJob.class;
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobs.getId() + "").build();
            jobDetail.getJobDataMap().put("id", jobs.getId());
            jobDetail.getJobDataMap().put("type", JobType.TEST_JOB);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobs.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobs.getId() + "").withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("创建定时任务成功");
        } catch (SchedulerException e) {
            logger.error("创建定时任务出错：" + e.getMessage());
            throw e;
        }
    }

    /**
     * @param jobs
     * @return void
     * @author ZhouYiXun
     * @des 暂停定时任务
     * @date 2021/8/21 17:42
     */
    public void pauseScheduleJob(Jobs jobs) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobs.getId() + "");
        try {
            scheduler.pauseJob(jobKey);
            logger.info("暂停定时任务成功");
        } catch (SchedulerException e) {
            logger.error("暂停定时任务出错：" + e.getMessage());
            throw e;
        }
    }

    /**
     * @param jobs
     * @return void
     * @author ZhouYiXun
     * @des 重启定时任务
     * @date 2021/8/21 17:43
     */
    public void resumeScheduleJob(Jobs jobs) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobs.getId() + "");
        try {
            scheduler.resumeJob(jobKey);
            logger.info("重启定时任务成功");
        } catch (SchedulerException e) {
            logger.error("重启定时任务出错：" + e.getMessage());
            throw e;
        }
    }

    /**
     * @param jobs
     * @return void
     * @author ZhouYiXun
     * @des 执行定时任务
     * @date 2021/10/10 12:43
     */
    public void runScheduleJob(Jobs jobs) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobs.getId() + "");
        try {
            scheduler.triggerJob(jobKey);
            logger.info("运行一次定时任务成功");
        } catch (SchedulerException e) {
            logger.error("运行一次定时任务出错：" + e.getMessage());
            throw e;
        }
    }

    /**
     * @param jobs
     * @return void
     * @author ZhouYiXun
     * @des 更新定时任务
     * @date 2021/8/21 17:43
     */
    public void updateScheduleJob(Jobs jobs) throws SchedulerException {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobs.getId() + "");
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobs.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
            logger.info("更新定时任务成功");
        } catch (SchedulerException e) {
            logger.error("更新定时任务出错：" + e.getMessage());
            throw e;
        }
    }

    /**
     * @param jobs
     * @return void
     * @author ZhouYiXun
     * @des 删除定时任务
     * @date 2021/8/21 17:44
     */
    public void deleteScheduleJob(Jobs jobs) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobs.getId() + "");
        TriggerKey triggerKey = TriggerKey.triggerKey(jobs.getId() + "");
        try {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
            logger.info("删除定时任务成功");
        } catch (SchedulerException e) {
            logger.error("删除定时任务出错：" + e.getMessage());
            throw e;
        }
    }

    public CronTrigger getTrigger(Jobs jobs) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobs.getId() + "");
        CronTrigger trigger = null;
        try {
            trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return trigger;
    }

    public void createTrigger(String type, int typeCode, String cron) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(type);
            CronTrigger hasTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (hasTrigger == null) {
                Class<? extends Job> jobClass = QuartzJob.class;
                JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(type).build();
                jobDetail.getJobDataMap().put("type", typeCode);
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron)
                        .withMisfireHandlingInstructionDoNothing();
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(type).withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
                logger.info("创建" + type + "系统定时任务成功");
            } else {
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron)
                        .withMisfireHandlingInstructionDoNothing();
                hasTrigger = hasTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                scheduler.rescheduleJob(triggerKey, hasTrigger);
                logger.info(type + "系统定时任务已存在");
            }
        } catch (SchedulerException e) {
            logger.error("创建" + type + "定时任务出错：" + e.getMessage());
        }
    }
}