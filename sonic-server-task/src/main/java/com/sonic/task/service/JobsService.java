package com.sonic.task.service;

import com.sonic.common.exception.SonicCronException;
import com.sonic.common.http.RespModel;
import com.sonic.task.models.Jobs;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 定时任务逻辑层
 * @date 2021/8/22 11:20
 */
public interface JobsService {
    RespModel save(Jobs jobs) throws SonicCronException;

    RespModel updateStatus(int id, int type);

    RespModel delete(int id);

    List<Jobs> findByProjectId(int projectId);

    Jobs findById(int id);
}
