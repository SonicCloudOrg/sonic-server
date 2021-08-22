package com.sonic.controller.services;

import com.sonic.controller.models.Projects;

/**
 * @author ZhouYiXun
 * @des 项目逻辑层
 * @date 2021/8/20 20:51
 */
public interface ProjectsService {
    Projects findById(int id);
}
