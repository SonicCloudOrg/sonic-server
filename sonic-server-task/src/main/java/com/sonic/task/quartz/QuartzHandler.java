package com.sonic.task.quartz;

import com.sonic.task.models.Tasks;
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
     * @param tasks
     * @return void
     * @author ZhouYiXun
     * @des 创建定时任务
     * @date 2021/8/21 17:40
     */
    public void createScheduleJob(Scheduler scheduler, Tasks tasks) {
        try {
            Class<? extends Job> jobClass = QuartzTask.class;
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(tasks.getId() + "").build();
            jobDetail.getJobDataMap().put("id", tasks.getId());
            //withMisfireHandlingInstructionDoNothing不触发立即执行
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(tasks.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(tasks.getId() + "").withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("创建定时任务成功");
        } catch (SchedulerException e) {
            logger.error("创建定时任务出错：" + e.getMessage());
        }
    }

    /**
     * @param scheduler
     * @param tasks
     * @return void
     * @author ZhouYiXun
     * @des 暂停定时任务
     * @date 2021/8/21 17:42
     */
    public void pauseScheduleJob(Scheduler scheduler, Tasks tasks) {
        JobKey jobKey = JobKey.jobKey(tasks.getId() + "");
        try {
            scheduler.pauseJob(jobKey);
            logger.info("暂停定时任务成功");
        } catch (SchedulerException e) {
            logger.error("暂停定时任务出错：" + e.getMessage());
        }
    }

    /**
     * @param scheduler
     * @param tasks
     * @return void
     * @author ZhouYiXun
     * @des 重启定时任务
     * @date 2021/8/21 17:43
     */
    public void resumeScheduleJob(Scheduler scheduler, Tasks tasks) {
        JobKey jobKey = JobKey.jobKey(tasks.getId() + "");
        try {
            scheduler.resumeJob(jobKey);
            logger.info("重启定时任务成功");
        } catch (SchedulerException e) {
            logger.error("重启定时任务出错：" + e.getMessage());
        }
    }

    /**
     * @param scheduler
     * @param tasks
     * @return void
     * @author ZhouYiXun
     * @des 更新定时任务
     * @date 2021/8/21 17:43
     */
    public void updateScheduleJob(Scheduler scheduler, Tasks tasks) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(tasks.getId() + "");
            //withMisfireHandlingInstructionDoNothing不触发立即执行
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(tasks.getCronExpression())
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
     * @param tasks
     * @return void
     * @author ZhouYiXun
     * @des 删除定时任务
     * @date 2021/8/21 17:44
     */
    public void deleteScheduleJob(Scheduler scheduler, Tasks tasks) {
        JobKey jobKey = JobKey.jobKey(tasks.getId() + "");
        TriggerKey triggerKey = TriggerKey.triggerKey(tasks.getId() + "");
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