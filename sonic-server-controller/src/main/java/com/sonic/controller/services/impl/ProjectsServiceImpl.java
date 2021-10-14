package com.sonic.controller.services.impl;

import com.sonic.common.exception.SonicException;
import com.sonic.controller.dao.*;
import com.sonic.controller.models.Projects;
import com.sonic.controller.models.Results;
import com.sonic.controller.services.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private ElementsRepository elementsRepository;
    @Autowired
    private GlobalParamsRepository globalParamsRepository;
    @Autowired
    private ModulesRepository modulesRepository;
    @Autowired
    private VersionsRepository versionsRepository;
    @Autowired
    private PublicStepsRepository publicStepsRepository;
    @Autowired
    private ResultsRepository resultsRepository;
    @Autowired
    private ResultDetailRepository resultDetailRepository;
    @Autowired
    private StepsRepository stepsRepository;
    @Autowired
    private TestSuitesRepository testSuitesRepository;
    @Autowired
    private TestCasesRepository testCasesRepository;

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

    @Override
    @Transactional(rollbackFor = SonicException.class)
    public void delete(int id) throws SonicException {
        try {
            testSuitesRepository.deleteByProjectId(id);
            publicStepsRepository.deleteByProjectId(id);
            testCasesRepository.deleteByProjectId(id);
            stepsRepository.deleteByProjectId(id);
            elementsRepository.deleteByProjectId(id);
            modulesRepository.deleteByProjectId(id);
            globalParamsRepository.deleteByProjectId(id);
            List<Results> resultsList = resultsRepository.findByProjectId(id);
            for (Results results : resultsList) {
                resultDetailRepository.deleteByResultId(results.getId());
            }
            resultsRepository.deleteByProjectId(id);
            versionsRepository.deleteByProjectId(id);
            projectsRepository.deleteById(id);
        } catch (Exception e) {
            throw new SonicException("删除出错！请联系管理员！");
        }
    }
}
