/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
