package org.cloud.sonic.controller.tools;

import org.cloud.sonic.controller.models.interfaces.JobType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.cloud.sonic.controller.quartz.QuartzHandler;

@Component
public class LaunchTool implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(LaunchTool.class);
    @Autowired
    private QuartzHandler quartzHandler;

    @Value("${sonic.jobs.fileCron}")
    private String fileCron;
    @Value("${sonic.jobs.resultCron}")
    private String resultCron;
    @Value("${sonic.jobs.dayReportCron}")
    private String dayReportCron;
    @Value("${sonic.jobs.weekReportCron}")
    private String weekReportCron;

    @Override
    public void run(ApplicationArguments args) {
        quartzHandler.createTrigger("cleanFile", JobType.CLEAN_FILE_JOB, fileCron);
        quartzHandler.createTrigger("cleanResult", JobType.CLEAN_RESULT_JOB, resultCron);
        quartzHandler.createTrigger("sendDayReport", JobType.SEND_DAY_REPORT, dayReportCron);
        quartzHandler.createTrigger("sendWeekReport", JobType.SEND_WEEK_REPORT, weekReportCron);
    }
}
