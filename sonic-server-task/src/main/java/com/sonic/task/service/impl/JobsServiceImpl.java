package com.sonic.task.service.impl;

import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.task.dao.JobsRepository;
import com.sonic.task.models.Jobs;
import com.sonic.task.models.interfaces.JobStatus;
import com.sonic.task.quartz.QuartzHandler;
import com.sonic.task.service.JobsService;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private Scheduler scheduler;
    @Autowired
    private JobsRepository jobsRepository;

    @Override
    public RespModel save(Jobs jobs) {
        jobs.setStatus(JobStatus.ENABLE);
        if (jobsRepository.existsById(jobs.getId())) {
            try {
                quartzHandler.updateScheduleJob(scheduler, jobs);
            } catch (Exception e) {
                e.printStackTrace();
                return new RespModel(3000, "操作失败!请检查cron表达式是否无误！");
            }
            jobsRepository.save(jobs);
            return new RespModel(RespEnum.UPDATE_OK);
        } else {
            jobsRepository.save(jobs);
            try {
                quartzHandler.createScheduleJob(scheduler, jobs);
            } catch (Exception e) {
                delete(jobs.getId());
                e.printStackTrace();
                return new RespModel(3000, "操作失败!请检查cron表达式是否无误！");
            }
            return new RespModel(RespEnum.HANDLE_OK);
        }
    }

    @Override
    public RespModel updateJob(int id, int type) {
        if (jobsRepository.existsById(id)) {
            Jobs jobs = jobsRepository.findById(id).get();
            switch (type) {
                case JobStatus.DISABLE:
                    try {
                        quartzHandler.pauseScheduleJob(scheduler, jobs);
                    } catch (Exception e) {
                        return new RespModel(3000, "关闭失败！");
                    }
                    jobs.setStatus(JobStatus.DISABLE);
                    jobsRepository.save(jobs);
                    return new RespModel(2000, "关闭成功！");
                case JobStatus.ENABLE:
                    try {
                        quartzHandler.resumeScheduleJob(scheduler, jobs);
                    } catch (Exception e) {
                        return new RespModel(3000, "开启失败！");
                    }
                    jobs.setStatus(JobStatus.ENABLE);
                    jobsRepository.save(jobs);
                    return new RespModel(2000, "开启成功！");
                default:
                    return new RespModel(3000, "参数有误！");
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
                quartzHandler.deleteScheduleJob(scheduler, jobs);
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
