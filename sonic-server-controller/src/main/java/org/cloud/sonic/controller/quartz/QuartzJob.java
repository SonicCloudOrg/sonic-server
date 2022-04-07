/**
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.controller.quartz;

import org.cloud.sonic.common.feign.FolderFeignClient;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.models.domain.Jobs;
import org.cloud.sonic.common.models.interfaces.JobType;
import org.cloud.sonic.common.services.JobsService;
import org.cloud.sonic.common.services.ResultsService;
import org.cloud.sonic.common.services.TestSuitesService;
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
    @Autowired private JobsService jobsService;
    @Autowired private TestSuitesService testSuitesService;
    @Autowired private ResultsService resultsService;
    @Autowired private FolderFeignClient folderFeignClient;
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
                    RespModel<String> r = testSuitesService.runSuite(jobs.getSuiteId(), "SYSTEM");
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
                resultsService.clean(resultsKeepDay);
                logger.info("清理测试结果任务开始");
                break;
            }
            case JobType.SEND_DAY_REPORT: {
                resultsService.sendDayReport();
                logger.info("发送日报任务开始");
                break;
            }
            case JobType.SEND_WEEK_REPORT: {
                resultsService.sendWeekReport();
                logger.info("发送周报任务开始");
                break;
            }
        }
    }
}
