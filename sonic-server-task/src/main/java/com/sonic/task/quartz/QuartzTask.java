package com.sonic.task.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sonic.task.models.Tasks;
import com.sonic.task.models.interfaces.TaskType;
import com.sonic.task.service.TasksService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author ZhouYiXun
 * @des 任务实现类
 * @date 2021/8/21 17:44
 */
@Component
public class QuartzTask extends QuartzJobBean implements Job {
    private final Logger logger = LoggerFactory.getLogger(QuartzTask.class);
    @Autowired
    private TasksService tasksService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Tasks tasks = tasksService.findById(dataMap.getInt("id"));
        if (tasks != null) {
            JSONObject data = JSON.parseObject(tasks.getContent());
            switch (tasks.getType()) {
                case TaskType.TEST_TASK:
                    int suiteId = data.getInteger("suiteId");
            }
        }
    }
}
