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
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.mapper.ElementsMapper;
import org.cloud.sonic.controller.mapper.ModulesMapper;
import org.cloud.sonic.controller.mapper.StepsElementsMapper;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Elements;
import org.cloud.sonic.controller.models.domain.Modules;
import org.cloud.sonic.controller.models.domain.Steps;
import org.cloud.sonic.controller.models.domain.StepsElements;
import org.cloud.sonic.controller.models.dto.ElementsDTO;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import org.cloud.sonic.controller.models.dto.TestCasesDTO;
import org.cloud.sonic.controller.services.ElementsService;
import org.cloud.sonic.controller.services.StepsService;
import org.cloud.sonic.controller.services.TestCasesService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    @Autowired
    private StepsElementsMapper stepsElementsMapper;
    @Autowired
    private ModulesMapper modulesMapper;

    @Override
    public CommentPage<ElementsDTO> findAll(int projectId, String type, List<String> eleTypes, String name, String value, List<Integer> moduleIds, Page<Elements> pageable) {
        LambdaQueryChainWrapper<Elements> lambdaQuery = new LambdaQueryChainWrapper<>(elementsMapper);

        if (type != null && type.length() > 0) {
            switch (type) {
                case "normal" -> lambdaQuery.and(
                        l -> l.ne(Elements::getEleType, "point").ne(Elements::getEleType, "image").ne(Elements::getEleType, "poco")
                );
                case "poco" ->
                        lambdaQuery.and(i -> i.eq(Elements::getEleType, "poco").or().eq(Elements::getEleType, "xpath").or().eq(Elements::getEleType, "cssSelector"));
                case "point" -> lambdaQuery.eq(Elements::getEleType, "point");
                case "image" -> lambdaQuery.eq(Elements::getEleType, "image");
            }
        }

        lambdaQuery.eq(Elements::getProjectId, projectId)
                .in(eleTypes != null, Elements::getEleType, eleTypes)
                .in(moduleIds != null && moduleIds.size() > 0, Elements::getModuleId, moduleIds)
                .like(!StringUtils.isEmpty(name), Elements::getEleName, name)
                .like(!StringUtils.isEmpty(value), Elements::getEleValue, value)
                .orderByDesc(Elements::getId);

        //写入对应模块信息
        Page<Elements> page = lambdaQuery.page(pageable);
        List<ElementsDTO> elementsDTOS = page.getRecords()
                .stream().map(e -> findEleDetail(e)).collect(Collectors.toList());

        return CommentPage.convertFrom(page, elementsDTOS);
    }

    @Transactional
    private ElementsDTO findEleDetail(Elements elements) {
        if (elements.getModuleId() != null && elements.getModuleId() != 0) {
            Modules modules = modulesMapper.selectById(elements.getModuleId());
            if (modules != null) {
                return elements.convertTo().setModulesDTO(modules.convertTo());
            }
        }
        return elements.convertTo();
    }

    @Override
    public List<StepsDTO> findAllStepsByElementsId(int elementsId) {
        return stepsService.listStepsByElementsId(elementsId).stream().map(e -> {
            StepsDTO stepsDTO = e.convertTo();
            if (0 == stepsDTO.getCaseId()) {
                return stepsDTO.setTestCasesDTO(new TestCasesDTO().setId(0).setName("unknown"));
            }
            return stepsDTO.setTestCasesDTO(testCasesService.findById(stepsDTO.getCaseId()));
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespModel delete(int id) {
        if (existsById(id)) {
            new LambdaUpdateChainWrapper<StepsElements>(stepsElementsMapper).eq(StepsElements::getElementsId, id)
                    .set(StepsElements::getElementsId, 0).update();
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


    /**
     * 复制控件元素
     *
     * @param id 元素id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespModel<String> copy(int id) {
        Elements element = elementsMapper.selectById(id);
        element.setId(null).setEleName(element.getEleName() + "_copy");
        save(element);

        return new RespModel<>(RespEnum.COPY_OK);
    }

    @Override
    public Boolean newStepBeLinkedEle(StepsDTO stepsDTO, Steps step) {
        for (ElementsDTO elements : stepsDTO.getElements()) {
            stepsElementsMapper.insert(new StepsElements()
                    .setElementsId(elements.getId())
                    .setStepsId(step.getId()));
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateEleModuleByModuleId(Integer module) {
        List<Elements> elements = lambdaQuery().eq(Elements::getModuleId, module).list();
        if (elements == null) {
            return true;
        }

        for (Elements element : elements) {
            save(element.setModuleId(0));
        }
        return true;
    }
}
