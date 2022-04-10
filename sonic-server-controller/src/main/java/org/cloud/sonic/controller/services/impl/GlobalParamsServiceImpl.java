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
import org.cloud.sonic.controller.mapper.GlobalParamsMapper;
import org.cloud.sonic.common.models.domain.GlobalParams;
import org.cloud.sonic.common.services.GlobalParamsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/10/9 23:28
 */
@Service
@DubboService
public class GlobalParamsServiceImpl extends SonicServiceImpl<GlobalParamsMapper, GlobalParams> implements GlobalParamsService {

    @Autowired
    private GlobalParamsMapper globalParamsMapper;

    @Override
    public List<GlobalParams> findAll(int projectId) {
        return lambdaQuery().eq(GlobalParams::getProjectId, projectId).list();
    }

    @Override
    public boolean delete(int id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public GlobalParams findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean deleteByProjectId(int projectId) {
        return baseMapper.delete(new LambdaQueryWrapper<GlobalParams>().eq(GlobalParams::getProjectId, projectId)) > 0;
    }
}
