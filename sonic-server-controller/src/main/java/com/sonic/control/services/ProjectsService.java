package com.sonic.control.services;

import com.sonic.control.models.Projects;

/**
 * @author ZhouYiXun
 * @des 项目逻辑层
 * @date 2021/8/20 20:51
 */
public interface ProjectsService {
    Projects findById(int id);
}
