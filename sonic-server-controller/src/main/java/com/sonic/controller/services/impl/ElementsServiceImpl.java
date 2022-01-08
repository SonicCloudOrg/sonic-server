package com.sonic.controller.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.mapper.ElementsMapper;
import com.sonic.controller.models.domain.Elements;
import com.sonic.controller.models.domain.Steps;
import com.sonic.controller.models.dto.StepsDTO;
import com.sonic.controller.services.ElementsService;
import com.sonic.controller.services.StepsService;
import com.sonic.controller.services.TestCasesService;
import com.sonic.controller.services.impl.base.SonicServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
