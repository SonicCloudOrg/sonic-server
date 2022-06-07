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
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.mapper.ElementsMapper;
import org.cloud.sonic.controller.models.domain.Elements;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import org.cloud.sonic.controller.models.dto.TestCasesDTO;
import org.cloud.sonic.controller.services.ElementsService;
import org.cloud.sonic.controller.services.StepsService;
import org.cloud.sonic.controller.services.TestCasesService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElementsServiceImpl extends SonicServiceImpl<ElementsMapper, Elements> implements ElementsService {

    @Autowired
    private ElementsMapper elementsMapper;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private TestCasesService testCasesService;

    @Override
    public Page<Elements> findAll(int projectId, String type, List<String> eleTypes, String name, Page<Elements> pageable) {
        LambdaQueryChainWrapper<Elements> lambdaQuery = lambdaQuery();

        if (type != null && type.length() > 0) {
            switch (type) {
                case "normal" -> lambdaQuery.and(
                        l -> l.ne(Elements::getEleType, "point").ne(Elements::getEleType, "image")
                );
                case "point" -> lambdaQuery.eq(Elements::getEleType, "point");
                case "image" -> lambdaQuery.eq(Elements::getEleType, "image");
            }
        }

        if (eleTypes != null) {
            lambdaQuery.in(Elements::getEleType, eleTypes);
        }
        if (name != null && name.length() > 0) {
            lambdaQuery.like(Elements::getEleName, name);
        }

        lambdaQuery.eq(Elements::getProjectId, projectId);
        lambdaQuery.orderByDesc(Elements::getId);

        return lambdaQuery.page(pageable);
    }

    @Override
    public List<StepsDTO> findAllStepsByElementsId(int elementsId) {
        return stepsService.listStepsByElementsId(elementsId).stream().map(e -> {
            StepsDTO stepsDTO = e.convertTo();
            if (0 == stepsDTO.getCaseId()) {
                return stepsDTO.setTestCasesDTO(new TestCasesDTO().setId(0).setName("unknown"));
            }
            return stepsDTO.setTestCasesDTO(testCasesService.findById(stepsDTO.getCaseId()).convertTo());
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespModel delete(int id) {
        if (existsById(id)) {
            List<StepsDTO> stepsList = findAllStepsByElementsId(id);
            for (StepsDTO steps : stepsList) {
                stepsService.delete(steps.getId());
            }
            baseMapper.deleteById(id);
            return new RespModel<>(RespEnum.DELETE_OK);
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @Override
    public Elements findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean deleteByProjectId(int projectId) {
        return baseMapper.delete(new LambdaQueryWrapper<Elements>().eq(Elements::getProjectId, projectId)) > 0;
    }
}
