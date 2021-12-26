package com.sonic.task.quartz;

import com.sonic.common.http.RespModel;
import com.sonic.task.feign.ControllerFeignClient;
import com.sonic.task.feign.FolderFeignClient;
import com.sonic.task.models.domain.Jobs;
import com.sonic.task.models.interfaces.JobType;
import com.sonic.task.service.JobsService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @author ZhouYiXun
 * @des 任务实现类
 * @date 2021/8/21 17:44
 */
@Component
public class QuartzJob extends QuartzJobBean implements Job {
    private final Logger logger = LoggerFactory.getLogger(QuartzJob.class);
    @Autowired
    private JobsService jobsService;
    @Autowired
    private ControllerFeignClient controllerFeignClient;
    @Autowired
    private FolderFeignClient folderFeignClient;
    @Value("${sonic.jobs.filesKeepDay}")
    private int filesKeepDay;
    @Value("${sonic.jobs.resultsKeepDay}")
    private int resultsKeepDay;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int type = dataMap.getInt("type");
        switch (type) {
            case JobType.TEST_JOB: {
                Jobs jobs = jobsService.findById(dataMap.getInt("id"));
                if (jobs != null) {
                    RespModel r = controllerFeignClient.runSuite(jobs.getSuiteId());
                    if (r.getCode() == 3001) {
                        logger.info("测试套件" + jobs.getSuiteId() + " 已删除" + r);
                        jobsService.delete(dataMap.getInt("id"));
                    } else {
                        logger.info("定时任务开始执行：测试套件" + jobs.getSuiteId() + " " + r);
                    }
                } else {
                    logger.info("定时任务id:" + dataMap.getInt("id") + "不存在！");
                }
                break;
            }
            case JobType.CLEAN_FILE_JOB: {
                RespModel r = folderFeignClient.delete(filesKeepDay);
                logger.info("清理文件任务开始：" + r);
                break;
            }
            case JobType.CLEAN_RESULT_JOB: {
                RespModel r = controllerFeignClient.clean(resultsKeepDay);
                logger.info("清理测试结果任务开始：" + r);
                break;
            }
            case JobType.SEND_DAY_REPORT: {
                RespModel r = controllerFeignClient.sendDayReport();
                logger.info("发送日报任务开始：" + r);
                break;
            }
            case JobType.SEND_WEEK_REPORT: {
                RespModel r = controllerFeignClient.sendWeekReport();
                logger.info("发送周报任务开始：" + r);
                break;
            }
        }
    }
}
