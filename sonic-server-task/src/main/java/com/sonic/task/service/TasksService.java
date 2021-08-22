package com.sonic.task.service;

import com.sonic.common.http.RespModel;
import com.sonic.task.models.Tasks;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 定时任务逻辑层
 * @date 2021/8/22 11:20
 */
public interface TasksService {
    RespModel save(Tasks tasks);

    RespModel updateJob(int id, String type);

    RespModel delete(int id);

    List<Tasks> findByProjectId(int projectId);

    Tasks findById(int id);
}
