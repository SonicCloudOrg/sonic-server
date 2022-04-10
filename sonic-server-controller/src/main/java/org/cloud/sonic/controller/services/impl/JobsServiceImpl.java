/*
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
package org.cloud.sonic.controller.services.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.models.domain.Jobs;
import org.cloud.sonic.common.models.interfaces.JobStatus;
import org.cloud.sonic.common.services.JobsService;
import org.cloud.sonic.controller.mapper.JobsMapper;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.quartz.CronTrigger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.cloud.sonic.controller.quartz.QuartzHandler;

/**
 * @author ZhouYiXun
 * @des 定时任务逻辑层实现
 * @date 2021/8/22 11:22
 */
@Service
public class JobsServiceImpl extends SonicServiceImpl<JobsMapper, Jobs> implements JobsService {

    @Autowired private QuartzHandler quartzHandler;
    @Autowired private JobsMapper jobsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespModel<String> saveJobs(Jobs jobs) throws SonicException {
        jobs.setStatus(JobStatus.ENABLE);
        save(jobs);
        CronTrigger trigger = quartzHandler.getTrigger(jobs);
        try {
            if (trigger != null) {
                quartzHandler.updateScheduleJob(jobs);
            } else {
                quartzHandler.createScheduleJob(jobs);
            }
            return new RespModel<>(RespEnum.HANDLE_OK);
        } catch (RuntimeException | SchedulerException e) {
            e.printStackTrace();
            throw new SonicException("操作失败！请检查cron表达式是否无误！");
        }
    }

    @Override
    public RespModel<String> updateStatus(int id, int type) {
        if (existsById(id)) {
            try {
                Jobs jobs = findById(id);
                switch (type) {
                    case JobStatus.DISABLE:
                        quartzHandler.pauseScheduleJob(jobs);
                        jobs.setStatus(JobStatus.DISABLE);
                        save(jobs);
                        return new RespModel<>(2000, "关闭成功！");
                    case JobStatus.ENABLE:
                        quartzHandler.resumeScheduleJob(jobs);
                        jobs.setStatus(JobStatus.ENABLE);
                        save(jobs);
                        return new RespModel<>(2000, "开启成功！");
                    case JobStatus.ONCE:
                        quartzHandler.runScheduleJob(jobs);
                        return new RespModel<>(2000, "开始运行！");
                    default:
                        return new RespModel<>(3000, "参数有误！");
                }
            } catch (RuntimeException | SchedulerException e) {
                e.printStackTrace();
                return new RespModel<>(3000, "操作失败！");
            }
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RespModel<String> delete(int id) {
        Jobs jobs = baseMapper.selectById(id);
        try {
            quartzHandler.deleteScheduleJob(jobs);
        } catch (SchedulerException e) {
            return new RespModel<>(RespEnum.DELETE_FAIL);
        }
        int count = baseMapper.deleteById(id);
        if (count > 0) {
            return new RespModel<>(RespEnum.DELETE_OK);
        }
        return new RespModel<>(RespEnum.ID_NOT_FOUND);
    }

    @Override
    public Page<Jobs> findByProjectId(int projectId, Page<Jobs> pageable) {
        return lambdaQuery()
                .eq(Jobs::getProjectId, projectId)
                .orderByDesc(Jobs::getId)
                .page(pageable);
    }

    @Override
    public Jobs findById(int id) {
        return baseMapper.selectById(id);
    }
}
