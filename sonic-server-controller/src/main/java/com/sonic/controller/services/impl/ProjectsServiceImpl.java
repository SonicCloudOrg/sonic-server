package com.sonic.controller.services.impl;

import com.sonic.common.exception.SonicException;
import com.sonic.controller.mapper.ProjectsMapper;
import com.sonic.controller.models.domain.Projects;
import com.sonic.controller.models.domain.Results;
import com.sonic.controller.services.*;
import com.sonic.controller.services.impl.base.SonicServiceImpl;
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
public class ProjectsServiceImpl extends SonicServiceImpl<ProjectsMapper, Projects> implements ProjectsService {

    @Autowired private ElementsService elementsService;
    @Autowired private GlobalParamsService globalParamsService;
    @Autowired private ModulesService modulesService;
    @Autowired private VersionsService versionsService;
    @Autowired private PublicStepsService publicStepsService;
    @Autowired private ResultsService resultsService;
    @Autowired private ResultDetailService resultDetailService;
    @Autowired private StepsService stepsService;
    @Autowired private TestSuitesService testSuitesService;
    @Autowired private TestCasesService testCasesService;

    @Override
    public Projects findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<Projects> findAll() {
        return list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(int id) throws SonicException {
        try {
            testSuitesService.deleteByProjectId(id);
            publicStepsService.deleteByProjectId(id);
            testCasesService.deleteByProjectId(id);
            stepsService.deleteByProjectId(id);
            elementsService.deleteByProjectId(id);
            modulesService.deleteByProjectId(id);
            globalParamsService.deleteByProjectId(id);
            List<Results> resultsList = resultsService.findByProjectId(id);
            for (Results results : resultsList) {
                resultDetailService.deleteByResultId(results.getId());
            }
            resultsService.deleteByProjectId(id);
            versionsService.deleteByProjectId(id);
            baseMapper.deleteById(id);
        } catch (Exception e) {
            throw new SonicException("删除出错！请联系管理员！");
        }
    }
}
