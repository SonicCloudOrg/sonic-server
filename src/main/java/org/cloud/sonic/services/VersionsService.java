package org.cloud.sonic.controller.services;


import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.domain.Versions;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 迭代逻辑层
 * @date 2021/8/16 22:54
 */
public interface VersionsService extends IService<Versions> {

    boolean delete(int id);

    List<Versions> findByProjectId(int projectId);

    Versions findById(int id);

    void deleteByProjectId(int projectId);
}
