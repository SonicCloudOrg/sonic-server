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
import org.cloud.sonic.controller.mapper.*;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.PublicSteps;
import org.cloud.sonic.controller.models.domain.PublicStepsSteps;
import org.cloud.sonic.controller.models.domain.Steps;
import org.cloud.sonic.controller.models.dto.PublicStepsAndStepsIdDTO;
import org.cloud.sonic.controller.models.dto.PublicStepsDTO;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import org.cloud.sonic.controller.services.ElementsService;
import org.cloud.sonic.controller.services.PublicStepsService;
import org.cloud.sonic.controller.services.StepsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ZhouYiXun
 * @des 公共步骤逻辑实现
 * @date 2021/8/20 17:51
 */
@Service
public class PublicStepsServiceImpl extends SonicServiceImpl<PublicStepsMapper, PublicSteps> implements PublicStepsService {

    @Autowired
    private PublicStepsMapper publicStepsMapper;
    @Autowired
    private ElementsMapper elementsMapper;
    @Autowired
    private PublicStepsStepsMapper publicStepsStepsMapper;
    @Autowired
    private StepsElementsMapper stepsElementsMapper;
    @Autowired
    private StepsMapper stepsMapper;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private ElementsService elementsService;

    @Transactional
    @Override
    public CommentPage<PublicStepsDTO> findByProjectId(int projectId, Page<PublicSteps> pageable) {
        Page<PublicSteps> page = lambdaQuery().eq(PublicSteps::getProjectId, projectId)
                .orderByDesc(PublicSteps::getId)
                .page(pageable);

        List<PublicStepsDTO> publicStepsDTOList = page.getRecords()
                .stream().map(TypeConverter::convertTo).collect(Collectors.toList());

        publicStepsDTOList.forEach(
                e -> e.setSteps(findById(e.getId(), false).getSteps())
        );

        return CommentPage.convertFrom(page, publicStepsDTOList);
    }

    @Override
    public List<Map<String, Object>> findByProjectIdAndPlatform(int projectId, int platform) {
        LambdaQueryWrapper<PublicSteps> lqw = new LambdaQueryWrapper<>();
        lqw.eq(PublicSteps::getProjectId, projectId)
                .eq(PublicSteps::getPlatform, platform)
                .select(PublicSteps::getId, PublicSteps::getName);
        return publicStepsMapper.selectMaps(lqw);
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
            // 保存 public_step 与 最外层step 映射关系
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
    public PublicStepsDTO findById(int id, boolean hiddenDisabled) {
        PublicSteps publicSteps = lambdaQuery().eq(PublicSteps::getId, id)
                .orderByDesc(PublicSteps::getId)
                .one();

        // 填充step
        List<StepsDTO> steps = stepsMapper.listByPublicStepsId(publicSteps.getId())
                .stream().map(TypeConverter::convertTo).collect(Collectors.toList());

        stepsService.handleSteps(steps, hiddenDisabled);

        PublicStepsDTO publicStepsDTO = publicSteps.convertTo().setSteps(steps);
        return publicStepsDTO.setSteps(steps);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByProjectId(int projectId) {
        List<PublicSteps> publicSteps = lambdaQuery().eq(PublicSteps::getProjectId, projectId).list();
        for (PublicSteps publicStep : publicSteps) {
            if (!ObjectUtils.isEmpty(publicSteps)) {
                publicStepsStepsMapper.delete(new LambdaQueryWrapper<PublicStepsSteps>()
                        .eq(PublicStepsSteps::getPublicStepsId, publicStep.getId()));
                delete(publicStep.getId());
            }
        }
        return true;
    }


    /**
     * 复制公共步骤
     *
     * @param id 被复制公共步骤id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyPublicSetpsIds(int id) {
        PublicSteps ps = publicStepsMapper.selectById(id);
        ps.setId(null).setName(ps.getName() + "_copy");
        //插入被复制的公共步骤
        save(ps);

        //根据旧公共步骤去查询需要被复制的step
        LambdaQueryWrapper<PublicStepsSteps> queryWrapper = new LambdaQueryWrapper<>();
        List<PublicStepsSteps> list = publicStepsStepsMapper.selectList(
                queryWrapper.eq(PublicStepsSteps::getPublicStepsId, id));

        List<Steps> oldStepsList = new ArrayList<>();
        for (PublicStepsSteps publicStepsSteps : list) {
            oldStepsList.add(stepsMapper.selectById(publicStepsSteps.getStepsId()));
        }
        //Steps转为DTO 方便后续管理数据，以及全部取出
        List<StepsDTO> oldStepsDtoList = new ArrayList<>();
        for (Steps steps : oldStepsList) {
            oldStepsDtoList.add(steps.convertTo());
        }
        //递归关联所有步骤，然后取出
        List<StepsDTO> stepsDTOS = stepsService.handleSteps(oldStepsDtoList, false);
        List<StepsDTO> needAllCopySteps = stepsService.getChildSteps(stepsDTOS);

        List<PublicStepsAndStepsIdDTO> oldStepDto = stepsService.stepAndIndex(needAllCopySteps);

        //统计需要和公共步骤关联的步骤，
        int n = 1;  // n 用来保持搜索map时候 caseId  和 key中setCaseId一致

        List<Integer> publicStepsStepsId = new ArrayList<>();

        for (StepsDTO steps : needAllCopySteps) {
            Steps step = steps.convertTo();

            if (step.getParentId() != 0) {
                //如果有关联的父亲步骤， 就计算插入过得父亲ID 写入parentId
                Integer fatherIdIndex = 0;
                Integer idIndex = 0;
                //计算子步骤和父步骤的相对间距
                for (PublicStepsAndStepsIdDTO stepsIdDTO : oldStepDto) {
                    if (stepsIdDTO.getStepsDTO().convertTo().equals(step)) {
                        fatherIdIndex = stepsIdDTO.getIndex();
                    }
                    if (stepsIdDTO.getStepsDTO().convertTo().equals(stepsMapper.selectById(step.getParentId()))) {
                        idIndex = stepsIdDTO.getIndex();
                    }
                }
                step.setId(null).setParentId(fatherIdIndex).setCaseId(0).setSort(stepsMapper.findMaxSort() + n);
                stepsMapper.insert(step.setCaseId(0));
                //修改父步骤Id
                step.setParentId(step.getId() - (fatherIdIndex - idIndex));
                stepsMapper.updateById(step);
                n++;
                //关联steps和elId
                if (steps.getElements() != null) {
                    elementsService.newStepBeLinkedEle(steps, step);
                }
                continue;
            }

            step.setId(null).setCaseId(0).setSort(stepsMapper.findMaxSort() + n);
            stepsMapper.insert(step);
            //关联steps和elId
            if (steps.getElements() != null) {
                elementsService.newStepBeLinkedEle(steps, step);
            }
            //插入的stepId 记录到需要关联步骤的list种
            publicStepsStepsId.add(step.getId());
            n++;
        }
        //查询新增step的步骤list 来遍历id  此时不包括子步骤
        for (Integer stepsId : publicStepsStepsId) {
            // 保存 public_step 与 最外层step 映射关系
            publicStepsStepsMapper.insert(
                    new PublicStepsSteps()
                            .setPublicStepsId(ps.getId())
                            .setStepsId(stepsId)
            );
        }
    }

}