/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.quartz;

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.feign.FolderFeignClient;
import org.cloud.sonic.controller.models.domain.Jobs;
import org.cloud.sonic.controller.models.interfaces.JobType;
import org.cloud.sonic.controller.services.JobsService;
import org.cloud.sonic.controller.services.ResultsService;
import org.cloud.sonic.controller.services.TestSuitesService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ZhouYiXun
 * @des 任务实现类
 * @date 2021/8/21 17:44
 */
@Component
@Slf4j
public class QuartzJob extends QuartzJobBean implements Job {
    @Autowired
    private JobsService jobsService;
    @Autowired
    private TestSuitesService testSuitesService;
    @Autowired
    private ResultsService resultsService;
    @Autowired
    private FolderFeignClient folderFeignClient;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int type = dataMap.getInt("type");
        switch (type) {
            case JobType.TEST_JOB -> {
                Jobs jobs = jobsService.findById(dataMap.getInt("id"));
                if (jobs != null) {
                    RespModel<Integer> r;
                    try {
                        r = testSuitesService.runSuite(jobs.getSuiteId(), "SYSTEM");
                    } catch (Throwable e) {
                        log.info(e.fillInStackTrace().toString());
                        return;
                    }
                    if (r.getCode() == 3001) {
                        log.info("Test suite " + jobs.getSuiteId() + " deleted. " + r);
                        jobsService.delete(dataMap.getInt("id"));
                    } else {
                        log.info("Job start: Test suite " + jobs.getSuiteId() + " " + r);
                    }
                } else {
                    log.info("Job id :" + dataMap.getInt("id") + " not found! ");
                }
            }
            case JobType.CLEAN_FILE_JOB -> {
                int day = Math.abs((int) ((jobExecutionContext.getNextFireTime().getTime() -
                        new Date().getTime()) / (1000 * 3600 * 24))) + 1;
                RespModel<String> r = folderFeignClient.delete(day);
                log.info("Clear file job..." + r);
            }
            case JobType.CLEAN_RESULT_JOB -> {
                int day = Math.abs((int) ((jobExecutionContext.getNextFireTime().getTime() -
                        new Date().getTime()) / (1000 * 3600 * 24))) + 1;
                resultsService.clean(day);
                log.info("Clean result job...");
            }
            case JobType.SEND_DAY_REPORT -> {
                resultsService.sendDayReport();
                log.info("Send day report...");
            }
            case JobType.SEND_WEEK_REPORT -> {
                resultsService.sendWeekReport();
                log.info("Send week report...");
            }
        }
    }
}
