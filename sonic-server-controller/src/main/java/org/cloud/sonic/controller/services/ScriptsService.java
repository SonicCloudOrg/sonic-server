package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.domain.Scripts;

public interface ScriptsService extends IService<Scripts> {
    boolean delete(int id);

    Page<Scripts> findByProjectId(Integer projectId, String name, Page<Scripts> pageable);

    Scripts findById(int id);

    boolean deleteByProjectId(int projectId);
}
