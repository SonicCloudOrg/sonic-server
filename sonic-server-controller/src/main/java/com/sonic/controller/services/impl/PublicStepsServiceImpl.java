package com.sonic.controller.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sonic.controller.mapper.*;
import com.sonic.controller.models.base.CommentPage;
import com.sonic.controller.models.base.TypeConverter;
import com.sonic.controller.models.domain.PublicSteps;
import com.sonic.controller.models.domain.PublicStepsSteps;
import com.sonic.controller.models.domain.Steps;
import com.sonic.controller.models.dto.ElementsDTO;
import com.sonic.controller.models.dto.PublicStepsDTO;
import com.sonic.controller.models.dto.StepsDTO;
import com.sonic.controller.services.PublicStepsService;
import com.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZhouYiXun
 * @des 公共步骤逻辑实现
 * @date 2021/8/20 17:51
 */
@Service
public class PublicStepsServiceImpl extends SonicServiceImpl<PublicStepsMapper, PublicSteps> implements PublicStepsService {

    @Autowired private PublicStepsMapper publicStepsMapper;
    @Autowired private ElementsMapper elementsMapper;
    @Autowired private PublicStepsStepsMapper publicStepsStepsMapper;
    @Autowired private StepsElementsMapper stepsElementsMapper;
    @Autowired private StepsMapper stepsMapper;

    @Transactional
    @Override
    public CommentPage<PublicStepsDTO> findByProjectId(int projectId, Page<PublicSteps> pageable) {
        Page<PublicSteps> page = lambdaQuery().eq(PublicSteps::getProjectId, projectId)
                .orderByDesc(PublicSteps::getId)
                .page(pageable);
        // 业务join，java层拼接结果，虽然麻烦一点，但sql性能确实能优化
        List<PublicStepsDTO> publicStepsDTOList = page.getRecords()
                .stream().map(TypeConverter::convertTo).collect(Collectors.toList());
        Set<Integer> publicStepsIdSet = publicStepsDTOList.stream().map(PublicStepsDTO::getId).collect(Collectors.toSet());
        if (publicStepsIdSet.isEmpty()) {
            return CommentPage.emptyPage();
        }

        // publicStepsId -> StepsDTO
        Map<Integer, List<StepsDTO>> stepsDTOMap = publicStepsMapper.listStepsByPublicStepsIds(publicStepsIdSet)
                .stream().collect(Collectors.groupingBy(StepsDTO::getPublicStepsId));
        Set<Integer> stepIdSet = new HashSet<>();
        stepsDTOMap.values().forEach(vList -> {
            vList.forEach(e -> stepIdSet.add(e.getId()));
        });

        // stepsId -> elementDTO
        Map<Integer, List<ElementsDTO>> elementDTOMap = new HashMap<>();
        if (!stepIdSet.isEmpty()) {
            elementDTOMap = elementsMapper.listElementsByStepsIds(stepIdSet)
                    .stream().collect(Collectors.groupingBy(ElementsDTO::getStepsId));
        }

        // 将element填充到step
        Map<Integer, List<ElementsDTO>> finalElementDTOMap = elementDTOMap;
        stepsDTOMap.forEach((k, v) -> {
            v.forEach(e -> e.setElements(finalElementDTOMap.get(e.getId())));
        });
        // 将step填充到public step
        publicStepsDTOList.forEach(e -> e.setSteps(stepsDTOMap.get(e.getId())));

        return CommentPage.convertFrom(page, publicStepsDTOList);
    }

    @Override
    public List<Map<Integer, String>> findByProjectIdAndPlatform(int projectId, int platform) {
        return publicStepsMapper.findByProjectIdAndPlatform(projectId, platform);
    }

    @Override
    @Transactional
    public PublicStepsDTO savePublicSteps(PublicStepsDTO publicStepsDTO) {
        PublicSteps publicSteps = publicStepsDTO.convertTo();
        save(publicSteps);
        List<StepsDTO> steps = publicStepsDTO.getSteps();
        // 先删除旧的数据
        publicStepsStepsMapper.delete(new LambdaQueryWrapper<PublicStepsSteps>()
                .eq(PublicStepsSteps::getPublicStepsId, publicStepsDTO.getId()));
        // 重新填充新数据
        for (StepsDTO step : steps) {
            // 保存 public_step 与 step 映射关系
            publicStepsStepsMapper.insert(
                    new PublicStepsSteps()
                            .setPublicStepsId(publicSteps.getId())
                            .setStepsId(step.getId())
            );
        }
        return publicSteps.convertTo();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(int id) {
        // 删除用例中的公共步骤
        stepsMapper.delete(new LambdaQueryWrapper<Steps>().eq(Steps::getText, id));
        // 删除与步骤的映射关系
        publicStepsStepsMapper.delete(new LambdaQueryWrapper<PublicStepsSteps>()
                .eq(PublicStepsSteps::getPublicStepsId, id));
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public PublicStepsDTO findById(int id) {
        PublicSteps publicSteps = lambdaQuery().eq(PublicSteps::getId, id)
                .orderByDesc(PublicSteps::getId)
                .one();

        // 填充step
        List<StepsDTO> steps = stepsMapper.listByPublicStepsId(publicSteps.getId())
                .stream().map(TypeConverter::convertTo).collect(Collectors.toList());

        for (StepsDTO step : steps) {
            step.setElements(elementsMapper.listElementsByStepsId(step.getId()));
        }

        PublicStepsDTO publicStepsDTO = publicSteps.convertTo().setSteps(steps);
        return publicStepsDTO.setSteps(steps);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByProjectId(int projectId) {
        PublicSteps publicSteps = lambdaQuery().eq(PublicSteps::getProjectId, projectId).one();
        publicStepsStepsMapper.delete(new LambdaQueryWrapper<PublicStepsSteps>()
                .eq(PublicStepsSteps::getPublicStepsId, publicSteps.getId()));
        return delete(publicSteps.getId());
    }
}
