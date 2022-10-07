/*
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.controller.services.impl;

import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.controller.mapper.ProjectsMapper;
import org.cloud.sonic.controller.models.domain.Projects;
import org.cloud.sonic.controller.models.domain.Results;
import org.cloud.sonic.controller.services.*;
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
public class ProjectsServiceImpl extends SonicServiceImpl<ProjectsMapper, Projects> implements ProjectsService {

    @Autowired
    private ElementsService elementsService;
    @Autowired
    private GlobalParamsService globalParamsService;
    @Autowired
    private ModulesService modulesService;
    @Autowired
    private VersionsService versionsService;
    @Autowired
    private PublicStepsService publicStepsService;
    @Autowired
    private ResultsService resultsService;
    @Autowired
    private ResultDetailService resultDetailService;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private TestSuitesService testSuitesService;
    @Autowired
    private TestCasesService testCasesService;
    @Autowired
    private ScriptsService scriptsService;

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
            scriptsService.deleteByProjectId(id);
            baseMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SonicException("project.delete.fail");
        }
    }
}
