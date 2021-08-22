package com.sonic.task.service.impl;

import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.task.dao.TasksRepository;
import com.sonic.task.models.Tasks;
import com.sonic.task.models.interfaces.TaskStatus;
import com.sonic.task.quartz.QuartzHandler;
import com.sonic.task.service.TasksService;
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
public class TasksServiceImpl implements TasksService {
    @Autowired
    private QuartzHandler quartzHandler;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private TasksRepository tasksRepository;

    @Override
    public RespModel save(Tasks tasks) {
        tasks.setStatus(TaskStatus.ENABLE);
        if (tasksRepository.existsById(tasks.getId())) {
            try {
                quartzHandler.updateScheduleJob(scheduler, tasks);
            } catch (Exception e) {
                return new RespModel(3000, "操作失败!请检查cron表达式是否无误！");
            }
            tasksRepository.save(tasks);
            return new RespModel(RespEnum.UPDATE_OK);
        } else {
            try {
                quartzHandler.createScheduleJob(scheduler, tasks);
            } catch (Exception e) {
                return new RespModel(3000, "操作失败!请检查cron表达式是否无误！");
            }
            tasksRepository.save(tasks);
            return new RespModel(RespEnum.HANDLE_OK);
        }
    }

    @Override
    public RespModel updateJob(int id, String type) {
        if (tasksRepository.existsById(id)) {
            Tasks tasks = tasksRepository.findById(id).get();
            switch (type) {
                case "pauseJob":
                    try {
                        quartzHandler.pauseScheduleJob(scheduler, tasks);
                    } catch (Exception e) {
                        return new RespModel(3000, "关闭失败！");
                    }
                    tasks.setStatus(TaskStatus.DISABLE);
                    tasksRepository.save(tasks);
                    return new RespModel(2000, "关闭成功！");
                case "resumeJob":
                    try {
                        quartzHandler.resumeScheduleJob(scheduler, tasks);
                    } catch (Exception e) {
                        return new RespModel(3000, "开启失败！");
                    }
                    tasks.setStatus(TaskStatus.ENABLE);
                    tasksRepository.save(tasks);
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
        if (tasksRepository.existsById(id)) {
            Tasks tasks = tasksRepository.findById(id).get();
            try {
                quartzHandler.deleteScheduleJob(scheduler, tasks);
            } catch (Exception e) {
                return new RespModel(RespEnum.DELETE_ERROR);
            }
            tasksRepository.deleteById(id);
            return new RespModel(RespEnum.DELETE_OK);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }

    @Override
    public List<Tasks> findByProjectId(int projectId) {
        return tasksRepository.findByProjectId(projectId);
    }

    @Override
    public Tasks findById(int id) {
        if (tasksRepository.existsById(id)) {
            return tasksRepository.findById(id).get();
        } else {
            return null;
        }
    }
}
