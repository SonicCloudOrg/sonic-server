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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.cloud.sonic.controller.mapper.ElementsMapper;
import org.cloud.sonic.controller.mapper.ModulesMapper;
import org.cloud.sonic.controller.models.domain.Modules;
import org.cloud.sonic.controller.services.ElementsService;
import org.cloud.sonic.controller.services.ModulesService;
import org.cloud.sonic.controller.services.TestCasesService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModulesServiceImpl extends SonicServiceImpl<ModulesMapper, Modules> implements ModulesService {

    @Autowired
    private ModulesMapper modulesMapper;
    @Autowired
    private ElementsService elementsService;
    @Autowired
    private TestCasesService testCasesService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(int id) {
        int i = modulesMapper.deleteById(id);
        elementsService.updateEleModuleByModuleId(id);
        testCasesService.updateTestCaseModuleByModuleId(id);
        return i > 0;
    }

    @Override
    public List<Modules> findByProjectId(int projectId) {
        return lambdaQuery().eq(Modules::getProjectId, projectId).list();
    }

    @Override
    public Modules findById(int id) {
        return modulesMapper.selectById(id);
    }

    @Override
    public boolean deleteByProjectId(int projectId) {
        return baseMapper.delete(new LambdaQueryWrapper<Modules>().eq(Modules::getProjectId, projectId)) > 0;
    }
}
