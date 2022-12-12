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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.controller.mapper.ScriptsMapper;
import org.cloud.sonic.controller.models.domain.Scripts;
import org.cloud.sonic.controller.services.ScriptsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<Scripts> findByProjectId(Integer projectId, String name, Page<Scripts> pageable) {
        return lambdaQuery().eq((projectId != null && projectId != 0), Scripts::getProjectId, projectId)
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
