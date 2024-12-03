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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.controller.mapper.*;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.*;
import org.cloud.sonic.controller.models.dto.ElementsDTO;
import org.cloud.sonic.controller.models.dto.PublicStepsAndStepsIdDTO;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import org.cloud.sonic.controller.models.enums.ConditionEnum;
import org.cloud.sonic.controller.models.http.StepSort;
import org.cloud.sonic.controller.services.ElementsService;
import org.cloud.sonic.controller.services.StepsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZhouYiXun
 * @des 测试步骤实现
 * @date 2021/8/20 17:51
 */
@Service
public class StepsServiceImpl extends SonicServiceImpl<StepsMapper, Steps> implements StepsService {

    @Autowired
    private StepsMapper stepsMapper;
    @Autowired
    private ElementsMapper elementsMapper;
    @Autowired
    private PublicStepsMapper publicStepsMapper;
    @Autowired
    private PublicStepsStepsMapper publicStepsStepsMapper;
    @Autowired
    private StepsElementsMapper stepsElementsMapper;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private ElementsService elementsService;

    @Transactional
    @Override
    public List<StepsDTO> findByCaseIdOrderBySort(int caseId, boolean hiddenDisabled) {

        // 取出用例下所有无父级的步骤
        List<StepsDTO> stepsDTOList = lambdaQuery()
                .eq(Steps::getCaseId, caseId)
                .eq(Steps::getParentId, 0)
                .eq(hiddenDisabled, Steps::getDisabled, 0)
                .orderByAsc(Steps::getSort)
                .list()
                // 转换成DTO
                .stream().map(TypeConverter::convertTo).collect(Collectors.toList());

        // 遍历父级步骤，如果是条件步骤，则取出子步骤集合
        handleSteps(stepsDTOList, hiddenDisabled);

        return stepsDTOList;
    }

    @Transactional
    @Override
    public List<StepsDTO> handleSteps(List<StepsDTO> stepsDTOS, boolean hiddenDisabled) {
        if (CollectionUtils.isEmpty(stepsDTOS)) {
            return stepsDTOS;
        }
        for (StepsDTO stepsDTO : stepsDTOS) {
            handleStep(stepsDTO, hiddenDisabled);
        }
        return stepsDTOS;
    }


    /**
     * 获取每个step下的childSteps 组装成一个list返回
     *
     * @param stepsDTOS 步骤集合
     * @return 包含所有子步骤的集合
     */
    public List<StepsDTO> getChildSteps(List<StepsDTO> stepsDTOS) {

        // 记录一下层级，第一层不要
        List<StepsDTO> childSteps = new ArrayList<>();

        if (stepsDTOS == null || stepsDTOS.isEmpty()) {
            // 为空说明递归到最后一层，直接返回空集合
            return childSteps;
        }

        for (StepsDTO stepsDTO : stepsDTOS) {
            // 递归调用，底层返回的集合直接add到上层来
            childSteps.add(stepsDTO);
            childSteps.addAll(getChildSteps(stepsDTO.getChildSteps()));
        }

        // 结束递归的集合，最外层的就是结果
        return childSteps;
    }

//    //插入子步骤步骤
//    public boolean insertSteps(List<StepsDTO> stepsDTOS){
//        List<Steps> stepsDTOList = getChildSteps(stepsDTOS)
//                .stream().map(TypeConverter::convertTo).collect(Collectors.toList());
//        for(Steps steps : stepsDTOList){
//            steps.setId(null);
//            save(steps);
//        }
//        return true;
//    }


    @Transactional
    @Override
    public StepsDTO handleStep(StepsDTO stepsDTO, boolean hiddenDisabled) {
        if (stepsDTO == null) {
            return null;
        }
        stepsDTO.setElements(new LambdaQueryChainWrapper<>(stepsElementsMapper).eq(StepsElements::getStepsId, stepsDTO.getId()).list()
                .stream().map(e -> {
                    Elements ele = elementsService.findById(e.getElementsId());
                    if (ele != null) {
                        return ele.convertTo();
                    } else {
                        return Elements.newDeletedElement(e.getElementsId()).convertTo();
                    }
                }).collect(Collectors.toList()));
        // 如果是条件步骤
        if (!stepsDTO.getConditionType().equals(ConditionEnum.NONE.getValue())) {
            List<StepsDTO> childSteps = lambdaQuery()
                    .eq(Steps::getParentId, stepsDTO.getId())
                    .orderByAsc(Steps::getSort)
                    .list()
                    // 转换成DTO
                    .stream().map(TypeConverter::convertTo).collect(Collectors.toList());
            stepsDTO.setChildSteps(handleSteps(childSteps, hiddenDisabled));
        }
        return stepsDTO;
    }

    @Override
    public boolean resetCaseId(int id) {
        if (existsById(id)) {
            Steps steps = baseMapper.selectById(id);
            steps.setCaseId(0);
            save(steps);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(int id) {
        if (existsById(id)) {
            Steps steps = baseMapper.selectById(id);
            publicStepsStepsMapper.delete(new QueryWrapper<PublicStepsSteps>().eq("steps_id", steps.getId()));
            stepsElementsMapper.delete(new LambdaQueryWrapper<StepsElements>().eq(StepsElements::getStepsId,
                    steps.getId()));
            baseMapper.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStep(StepsDTO stepsDTO) {
        if (stepsDTO.getStepType().equals("publicStep")) {
            PublicSteps publicSteps = publicStepsMapper.selectById(Integer.parseInt(stepsDTO.getText()));
            if (publicSteps != null) {
                stepsDTO.setContent(publicSteps.getName());
            } else {
                stepsDTO.setContent("unknown");
            }
        }

        // 设置排序为最后
        if (!existsById(stepsDTO.getId())) {
            stepsDTO.setSort(stepsMapper.findMaxSort() + 1);
        }
        // 子步骤的caseId跟随父步骤的
        Steps parent = getById(stepsDTO.getParentId());
        if (!ObjectUtils.isEmpty(parent)) {
            stepsDTO.setCaseId(parent.getCaseId());
        }
        Steps steps = stepsDTO.convertTo();
        save(steps);

        // 删除旧关系
        stepsElementsMapper.delete(new LambdaQueryWrapper<StepsElements>().eq(StepsElements::getStepsId, steps.getId()));

        // 保存element映射关系
        List<ElementsDTO> elements = stepsDTO.getElements();
        List<String> iterator = Arrays.asList("androidIterator", "pocoIterator", "iOSIterator");
        for (ElementsDTO element : elements) {
            if (iterator.contains(element.getEleType())) {
                List<Elements> es = new LambdaQueryChainWrapper<>(elementsMapper).eq(Elements::getEleType, element.getEleType()).list();
                Elements e;
                if (es.size() > 0) {
                    e = es.get(0);
                } else {
                    e = new Elements().setEleName("当前迭代控件").setEleType(element.getEleType()).setEleValue("").setProjectId(0);
                    elementsMapper.insert(e);
                }
                stepsElementsMapper.insert(new StepsElements().setElementsId(e.getId()).setStepsId(steps.getId()));
            } else {
                stepsElementsMapper.insert(new StepsElements().setElementsId(element.getId()).setStepsId(steps.getId()));
            }
        }
    }

    @Transactional
    @Override
    public StepsDTO findById(int id) {
        StepsDTO stepsDTO = baseMapper.selectById(id).convertTo();
        handleStep(stepsDTO, false);
        return stepsDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sortSteps(StepSort stepSort) {
        List<Steps> stepsList;
        if (stepSort.getNewParentId() != null && stepSort.getNewIndex() != null) {
            // 分组拖拽
            stepsList = exchangeAddedStepSort(stepSort);
        } else {
            // 同组内拖拽排序
            stepsList = lambdaQuery().eq(Steps::getCaseId, stepSort.getCaseId())
                    // <=
                    .le(Steps::getSort, stepSort.getStartId())
                    // >=
                    .ge(Steps::getSort, stepSort.getEndId())
                    .orderByAsc(Steps::getSort)
                    .list();
        }
        if (stepSort.getDirection().equals("down")) {
            for (int i = 0; i < stepsList.size() - 1; i++) {
                int temp = stepsList.get(stepsList.size() - 1).getSort();
                stepsList.get(stepsList.size() - 1).setSort(stepsList.get(i).getSort());
                stepsList.get(i).setSort(temp);
            }
        } else {
            for (int i = 0; i < stepsList.size() - 1; i++) {
                int temp = stepsList.get(0).getSort();
                stepsList.get(0).setSort(stepsList.get(stepsList.size() - 1 - i).getSort());
                stepsList.get(stepsList.size() - 1 - i).setSort(temp);
            }
        }
        saveOrUpdateBatch(stepsList);
    }

    /**
     *  拖拽步骤顺序，步骤所在分组发生变化时，仅对新分组以及移动步骤的sort进行重新排序
     * @param stepSort
     * @return
     */
    private List<Steps> exchangeAddedStepSort(StepSort stepSort) {
        // 获取新分组的case步骤
        List<Steps> stepsList = lambdaQuery().eq(Steps::getCaseId, stepSort.getCaseId()).eq(Steps::getParentId, stepSort.getNewParentId()).list();
        // 被移动的步骤实例
        Steps movedStep = lambdaQuery().eq(Steps::getId, stepSort.getStepsId()).eq(Steps::getCaseId, stepSort.getCaseId()).one();
        if(movedStep == null){
            throw new SonicException("case中未能获取到该id的数据: %s", stepSort.getStepsId());
        }
        movedStep.setParentId(stepSort.getNewParentId()); // 更新父步骤id
        stepsList.add(movedStep); // 添加到组内列表
        if (stepsList.size() == 1){
            // 原本没有子步骤，直接更改父id就好了，没必要重新排序
            stepSort.setEndId(movedStep.getSort()) ;
            stepSort.setStartId(movedStep.getSort());
            stepSort.setDirection("down");
            return stepsList;
        }else {
            // 将所有子步骤包含新加入的步骤重新排序，这样就相当于在同一个分组内拖拽排序
            List<Steps> groupStepList = stepsList.stream().sorted(Comparator.comparingInt(Steps::getSort)).collect(Collectors.toList());
            if (groupStepList.get(stepSort.getNewIndex()).getSort() >= movedStep.getSort()){
                stepSort.setDirection("down");
                stepSort.setStartId(groupStepList.get(stepSort.getNewIndex()).getSort());
                stepSort.setEndId(movedStep.getSort());
            }else {
                stepSort.setDirection("up");
                stepSort.setStartId(movedStep.getSort());
                stepSort.setEndId(groupStepList.get(stepSort.getNewIndex()).getSort());
            }
            // 取出需要重新排序的步骤
            groupStepList = groupStepList.stream().filter(
                        steps -> steps.getSort() >= stepSort.getEndId()
                                && steps.getSort() <= stepSort.getStartId())
                        .collect(Collectors.toList());
            return groupStepList;
        }
    }

    @Override
    public CommentPage<StepsDTO> findByProjectIdAndPlatform(int projectId, int platform, Page<Steps> pageable) {

        Page<Steps> page = lambdaQuery().eq(Steps::getProjectId, projectId)
                .eq(Steps::getPlatform, platform)
                .eq(Steps::getParentId, 0)
                .orderByDesc(Steps::getId)
                .page(pageable);

        List<StepsDTO> stepsDTOList = page.getRecords()
                .stream().map(TypeConverter::convertTo).collect(Collectors.toList());
        handleSteps(stepsDTOList, false);

        return CommentPage.convertFrom(page, stepsDTOList);
    }

    @Override
    public List<Steps> listStepsByElementsId(int elementsId) {
        return stepsMapper.listStepsByElementId(elementsId);
    }

    @Override
    public boolean deleteByProjectId(int projectId) {
        // 先删除steps_elements表中，属于该projectId下的"步骤-元素"关联记录
        List<Steps> allSteps = stepsMapper.selectList(new LambdaQueryWrapper<Steps>().eq(Steps::getProjectId, projectId));
        for (Steps curStep : allSteps) {
            stepsElementsMapper.delete(new LambdaQueryWrapper<StepsElements>().eq(StepsElements::getStepsId, curStep.getId()));
        }
        // 再删除指定projectId的Step
        return baseMapper.delete(new LambdaQueryWrapper<Steps>().eq(Steps::getProjectId, projectId)) > 0;
    }

    @Override
    public List<StepsDTO> listByPublicStepsId(int publicStepsId) {
        return stepsMapper.listByPublicStepsId(publicStepsId)
                // 填充elements
                .stream().map(e -> e.convertTo().setElements(
                        new LambdaQueryChainWrapper<>(stepsElementsMapper).eq(StepsElements::getStepsId, e.getId()).list()
                                .stream().map(ec -> {
                                    Elements ele = elementsService.findById(ec.getElementsId());
                                    if (ele != null) {
                                        return ele.convertTo();
                                    } else {
                                        return Elements.newDeletedElement(ec.getElementsId()).convertTo();
                                    }
                                }).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    /**
     * 步骤列表:搜索
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentPage<StepsDTO> searchFindByProjectIdAndPlatform(int projectId, int platform, int page, int pageSize,
                                                                  String searchContent) {
        Page<Steps> pageList = new Page<>(page, pageSize);
        //分页返回数据
        IPage<Steps> steps = stepsMapper.searchByEleName(pageList, searchContent);
        //取出页面里面的数据，转为List<StepDTO>
        List<StepsDTO> stepsDTOList = steps.getRecords()
                .stream().map(TypeConverter::convertTo).collect(Collectors.toList());

        handleSteps(stepsDTOList, false);

        return CommentPage.convertFrom(pageList, stepsDTOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean copyStepsIdByCase(Integer stepId, boolean toLast) {
        Steps steps = stepsMapper.selectById(stepId);
        Integer originSortId = steps.getSort();
        StepsDTO stepsCopyDTO = stepsService.handleStep(steps.convertTo(), false);

        save(steps.setId(null).setSort(stepsMapper.findMaxSort() + 1));
        //关联ele
        if (stepsCopyDTO.getElements() != null) {
            elementsService.newStepBeLinkedEle(stepsCopyDTO, steps);
        }
        //插入子步骤
        if (stepsCopyDTO.getChildSteps() != null) {
            List<StepsDTO> needAllCopySteps = stepsService.getChildSteps(stepsCopyDTO.getChildSteps());

            List<PublicStepsAndStepsIdDTO> oldStepDto = stepAndIndex(needAllCopySteps);
            //统计需要和公共步骤关联的步骤，
            int n = 1;

            for (StepsDTO steps1 : needAllCopySteps) {
                Steps step = steps1.convertTo();

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

                    step.setId(null).setParentId(fatherIdIndex).setSort(stepsMapper.findMaxSort() + n);

                    stepsMapper.insert(step);

                    //修改父步骤Id
                    step.setParentId(step.getId() - (fatherIdIndex - idIndex));
                    stepsMapper.updateById(step);
                    n++;
                    //关联steps和elId
                    if (steps1.getElements() != null) {
                        elementsService.newStepBeLinkedEle(steps1, step);
                    }
                    continue;
                }

                step.setId(null).setSort(stepsMapper.findMaxSort() + n);
                stepsMapper.insert(step);
                //关联steps和elId
                if (steps1.getElements() != null) {
                    elementsService.newStepBeLinkedEle(steps1, step);
                }
                //插入的stepId 记录到需要关联步骤的list种
                n++;
            }
        }
        if (!toLast) {
            // 插入到当前步骤的下一行，需要做特殊的排序逻辑处理
            StepSort tempStepSort = new StepSort();
            tempStepSort.setCaseId(steps.getCaseId());
            tempStepSort.setDirection("up");
            // 设置start为当前新增的步骤的sort
            tempStepSort.setStartId(steps.getSort());
            // 设置end为之前复制出来的步骤的sort
            tempStepSort.setEndId(originSortId);
            sortSteps(tempStepSort);
        }
        return true;
    }

    @Override
    public Boolean switchStep(int id, int type) {
        Steps steps = baseMapper.selectById(id);
        if (steps != null) {
            steps.setDisabled(type);
            save(steps);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer findMaxStepSort(int castId) {
        List<Steps> stepsList = lambdaQuery().eq(Steps::getCaseId, castId)
                .orderByDesc(Steps::getSort)
                .list();
        if (stepsList != null && stepsList.size() > 0) {
            return stepsList.get(0).getSort();
        } else {
            return null;
        }
    }

    @Override
    public Integer findNextStepSort(int castId, int targetStepId) {
        List<Steps> stepsList = lambdaQuery().eq(Steps::getCaseId, castId)
                .orderByAsc(Steps::getSort)
                .list();
        for (int i = 0; i < stepsList.size(); i++) {
            if (stepsList.get(i).getId() == targetStepId) {
                return i == stepsList.size() - 1 ? null : stepsList.get(i + 1).getSort();
            }
        }
        return null;
    }

    /**
     * 记录一组步骤中他们所在的位置；ma
     *
     * @return 步骤和对应位置
     */
    public List<PublicStepsAndStepsIdDTO> stepAndIndex(List<StepsDTO> needAllCopySteps) {
        List<PublicStepsAndStepsIdDTO> oldStepDto = new ArrayList<>();
        int i = 1; //用来统计所在位置， 以及保持map中 key不同
        for (StepsDTO steps1 : needAllCopySteps) {
            PublicStepsAndStepsIdDTO psasId = new PublicStepsAndStepsIdDTO();
            psasId.setStepsDTO(steps1);
            psasId.setIndex(i);
            oldStepDto.add(psasId);
            i++;
        }
        return oldStepDto;
    }
}
