package com.sonic.task.service;

import com.sonic.common.exception.SonicException;
import com.sonic.common.http.RespModel;
import com.sonic.task.models.Jobs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ZhouYiXun
 * @des 定时任务逻辑层
 * @date 2021/8/22 11:20
 */
public interface JobsService {
    RespModel save(Jobs jobs) throws SonicException;

    RespModel updateStatus(int id, int type);

    RespModel delete(int id);

    Page<Jobs> findByProjectId(int projectId, Pageable pageable);

    Jobs findById(int id);
}
