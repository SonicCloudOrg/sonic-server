package com.sonic.controller.services.impl;

import com.sonic.controller.dao.ProjectsRepository;
import com.sonic.controller.models.Projects;
import com.sonic.controller.services.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 项目逻辑实现
 * @date 2021/8/21 20:57
 */
@Service
public class ProjectsServiceImpl implements ProjectsService {
    @Autowired
    private ProjectsRepository projectsRepository;

    @Override
    public void save(Projects projects) {
        projectsRepository.save(projects);
    }

    @Override
    public Projects findById(int id) {
        if (projectsRepository.existsById(id)) {
            return projectsRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public List<Projects> findAll() {
        return projectsRepository.findAll();
    }
}
