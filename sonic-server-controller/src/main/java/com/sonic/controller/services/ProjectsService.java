package com.sonic.controller.services;

import com.sonic.common.exception.SonicException;
import com.sonic.controller.models.Projects;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 项目逻辑层
 * @date 2021/8/20 20:51
 */
public interface ProjectsService {
    void save(Projects projects);

    Projects findById(int id);

    List<Projects> findAll();

    void delete(int id) throws SonicException;
}
