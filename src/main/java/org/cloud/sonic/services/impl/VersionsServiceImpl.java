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
import org.cloud.sonic.controller.mapper.VersionsMapper;
import org.cloud.sonic.controller.models.domain.Versions;
import org.cloud.sonic.controller.services.VersionsService;
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
