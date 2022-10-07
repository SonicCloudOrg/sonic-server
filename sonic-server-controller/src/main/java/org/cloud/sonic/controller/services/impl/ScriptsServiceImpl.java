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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.controller.mapper.ScriptsMapper;
import org.cloud.sonic.controller.models.domain.Scripts;
import org.cloud.sonic.controller.services.ScriptsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ScriptsServiceImpl extends SonicServiceImpl<ScriptsMapper, Scripts> implements ScriptsService {
    @Autowired
    private ScriptsMapper scriptsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(int id) {
        return scriptsMapper.deleteById(id) > 0;
    }

    @Override
    public Page<Scripts> findByProjectId(int projectId, String name, Page<Scripts> pageable) {
        return lambdaQuery().eq(Scripts::getProjectId, projectId)
                .like((name != null && name.length() > 0), Scripts::getName, name)
                .orderByDesc(Scripts::getId)
                .page(pageable);
    }

    @Override
    public Scripts findById(int id) {
        return scriptsMapper.selectById(id);
    }

    @Override
    public boolean deleteByProjectId(int projectId) {
        return baseMapper.delete(new LambdaQueryWrapper<Scripts>().eq(Scripts::getProjectId, projectId)) > 0;
    }
}
