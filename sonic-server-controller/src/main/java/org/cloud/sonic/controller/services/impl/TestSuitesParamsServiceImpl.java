/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.cloud.sonic.controller.mapper.GlobalParamsMapper;
import org.cloud.sonic.controller.mapper.TestSuitesParamsMapper;
import org.cloud.sonic.controller.models.domain.GlobalParams;
import org.cloud.sonic.controller.models.domain.TestSuitesParams;
import org.cloud.sonic.controller.services.GlobalParamsService;
import org.cloud.sonic.controller.services.TestSuitesParamsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zengxiaofan
 * @des
 * @date 2023/01/16 23:28
 */
@Service
public class TestSuitesParamsServiceImpl extends SonicServiceImpl<TestSuitesParamsMapper, TestSuitesParams> implements TestSuitesParamsService {

    @Autowired
    private TestSuitesParamsMapper testSuitesParamsMapper;

    @Override
    public List<TestSuitesParams> findAll(int suiteId) {
        return lambdaQuery().eq(TestSuitesParams::getSuiteId, suiteId).list();
    }

    @Override
    public boolean delete(int id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public TestSuitesParams findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean deleteBySuiteId(int suiteId) {
        return baseMapper.delete(new LambdaQueryWrapper<TestSuitesParams>().eq(TestSuitesParams::getSuiteId, suiteId)) > 0;
    }
}
