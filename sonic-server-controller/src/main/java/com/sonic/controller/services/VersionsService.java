package com.sonic.controller.services;


import com.sonic.controller.models.Versions;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 迭代逻辑层
 * @date 2021/8/16 22:54
 */
public interface VersionsService {
    void save(Versions versions);

    boolean delete(int id);

    List<Versions> findByProjectId(int projectId);

    Versions findById(int id);
}
