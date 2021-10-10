package com.sonic.task.service.impl;

import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.task.dao.JobsRepository;
import com.sonic.common.exception.SonicCronException;
import com.sonic.task.models.Jobs;
import com.sonic.task.models.interfaces.JobStatus;
import com.sonic.task.quartz.QuartzHandler;
import com.sonic.task.service.JobsService;
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
public class JobsServiceImpl implements JobsService {
    @Autowired
    private QuartzHandler quartzHandler;
    @Autowired
    private JobsRepository jobsRepository;

    @Override
    @Transactional(rollbackFor = SonicCronException.class)
    public RespModel save(Jobs jobs) throws SonicCronException {
        jobs.setStatus(JobStatus.ENABLE);
        jobsRepository.save(jobs);
        CronTrigger trigger = quartzHandler.getTrigger(jobs);
        try {
            if (trigger != null) {
                quartzHandler.updateScheduleJob(jobs);
            } else {
                quartzHandler.createScheduleJob(jobs);
            }
            return new RespModel(RespEnum.HANDLE_OK);
        } catch (RuntimeException | SchedulerException e) {
            e.printStackTrace();
            throw new SonicCronException("操作失败！请检查cron表达式是否无误！");
        }
    }

    @Override
    public RespModel updateStatus(int id, int type) {
        if (jobsRepository.existsById(id)) {
            try {
                Jobs jobs = jobsRepository.findById(id).get();
                switch (type) {
                    case JobStatus.DISABLE:
                        quartzHandler.pauseScheduleJob(jobs);
                        jobs.setStatus(JobStatus.DISABLE);
                        jobsRepository.save(jobs);
                        return new RespModel(2000, "关闭成功！");
                    case JobStatus.ENABLE:
                        quartzHandler.resumeScheduleJob(jobs);
                        jobs.setStatus(JobStatus.ENABLE);
                        jobsRepository.save(jobs);
                        return new RespModel(2000, "开启成功！");
                    case JobStatus.ONCE:
                        quartzHandler.resumeScheduleJob(jobs);
                        return new RespModel(2000, "开始运行！");
                    default:
                        return new RespModel(3000, "参数有误！");
                }
            } catch (RuntimeException | SchedulerException e) {
                e.printStackTrace();
                return new RespModel(3000, "操作失败！");
            }
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }

    @Override
    public RespModel delete(int id) {
        if (jobsRepository.existsById(id)) {
            Jobs jobs = jobsRepository.findById(id).get();
            try {
                quartzHandler.deleteScheduleJob(jobs);
            } catch (Exception e) {
                return new RespModel(RespEnum.DELETE_ERROR);
            }
            jobsRepository.deleteById(id);
            return new RespModel(RespEnum.DELETE_OK);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }

    @Override
    public List<Jobs> findByProjectId(int projectId) {
        return jobsRepository.findByProjectId(projectId);
    }

    @Override
    public Jobs findById(int id) {
        if (jobsRepository.existsById(id)) {
            return jobsRepository.findById(id).get();
        } else {
            return null;
        }
    }
}
