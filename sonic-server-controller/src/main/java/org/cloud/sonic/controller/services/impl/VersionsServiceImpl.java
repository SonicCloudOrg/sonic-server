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
import org.apache.dubbo.config.annotation.DubboService;
import org.cloud.sonic.controller.mapper.VersionsMapper;
import org.cloud.sonic.common.models.domain.Versions;
import org.cloud.sonic.common.services.VersionsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 迭代逻辑层实现
 * @date 2021/8/16 22:56
 */
@Service
@DubboService
public class VersionsServiceImpl extends SonicServiceImpl<VersionsMapper, Versions> implements VersionsService {

    @Autowired
    private VersionsMapper versionsMapper;

    @Override
    public boolean delete(int id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public List<Versions> findByProjectId(int projectId) {
        return lambdaQuery().eq(Versions::getProjectId, projectId)
                .orderByDesc(Versions::getCreateTime)
                .list();
    }

    @Override
    public Versions findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void deleteByProjectId(int projectId) {
        baseMapper.delete(new LambdaQueryWrapper<Versions>().eq(Versions::getProjectId, projectId));
    }
}
