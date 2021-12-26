package com.sonic.task.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sonic.common.exception.SonicException;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.task.mapper.JobsMapper;
import com.sonic.task.models.domain.Jobs;
import com.sonic.task.models.interfaces.JobStatus;
import com.sonic.task.quartz.QuartzHandler;
import com.sonic.task.service.JobsService;
import com.sonic.task.service.impl.base.SonicServiceImpl;
import org.quartz.CronTrigger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            return new RespModel<>(RespEnum.DELETE_ERROR);
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
