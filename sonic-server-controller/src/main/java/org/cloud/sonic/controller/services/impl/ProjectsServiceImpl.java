package org.cloud.sonic.controller.services.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.services.*;
import org.cloud.sonic.controller.mapper.ProjectsMapper;
import org.cloud.sonic.common.models.domain.Projects;
import org.cloud.sonic.common.models.domain.Results;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
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
@DubboService
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
            e.printStackTrace();
            throw new SonicException("删除出错！请联系管理员！");
        }
    }
}
