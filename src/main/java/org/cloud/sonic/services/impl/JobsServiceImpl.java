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
package org.cloud.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.mapper.JobsMapper;
import org.cloud.sonic.controller.models.domain.Jobs;
import org.cloud.sonic.controller.models.interfaces.JobStatus;
import org.cloud.sonic.controller.quartz.QuartzHandler;
import org.cloud.sonic.controller.services.JobsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.quartz.CronTrigger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 定时任务逻辑层实现
 * @date 2021/8/22 11:22
 */
@Service
public class JobsServiceImpl extends SonicServiceImpl<JobsMapper, Jobs> implements JobsService {

    @Autowired
    private QuartzHandler quartzHandler;
    @Autowired
    private JobsMapper jobsMapper;
    private static final String TEST_JOB = "testJob";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespModel<String> saveJobs(Jobs jobs) throws SonicException {
        jobs.setStatus(JobStatus.ENABLE);
        jobs.setType(TEST_JOB);
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
            throw new SonicException("error.cron");
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
                        return new RespModel<>(2000, "job.disable");
                    case JobStatus.ENABLE:
                        quartzHandler.resumeScheduleJob(jobs);
                        jobs.setStatus(JobStatus.ENABLE);
                        save(jobs);
                        return new RespModel<>(2000, "job.enable");
                    case JobStatus.ONCE:
                        quartzHandler.runScheduleJob(jobs);
                        return new RespModel<>(2000, "job.start");
                    default:
                        return new RespModel<>(3000, "job.params.invalid");
                }
            } catch (RuntimeException | SchedulerException e) {
                e.printStackTrace();
                return new RespModel<>(3000, "job.handle.fail");
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
                .eq(Jobs::getType, TEST_JOB)
                .orderByDesc(Jobs::getId)
                .page(pageable);
    }

    @Override
    public Jobs findById(int id) {
        return lambdaQuery()
                .eq(Jobs::getId, id)
                .eq(Jobs::getType, TEST_JOB)
                .one();
    }

    @Override
    public Jobs findByType(String type) {
        return lambdaQuery()
                .eq(Jobs::getType, type)
                .one();
    }


    @Override
    public void updateSysJob(String type, String cron) {
        Jobs jobs = lambdaQuery()
                .eq(Jobs::getType, type)
                .one();
        if (jobs != null) {
            jobs.setCronExpression(cron);
            save(jobs);
        }

        quartzHandler.updateSysScheduleJob(type, cron);
    }

    @Override
    public List<JSONObject> findSysJobs() {
        return quartzHandler.findSysJobs();
    }
}
