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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.controller.mapper.*;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.*;
import org.cloud.sonic.controller.models.dto.PublicStepsAndStepsIdDTO;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import org.cloud.sonic.controller.models.dto.TestCasesDTO;
import org.cloud.sonic.controller.services.*;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZhouYiXun
 * @des 测试用例逻辑实现
 * @date 2021/8/20 17:51
 */
@Service
public class TestCasesServiceImpl extends SonicServiceImpl<TestCasesMapper, TestCases> implements TestCasesService {
    @Autowired
    private StepsService stepsService;
    @Autowired
    private StepsElementsMapper stepsElementsMapper;
    @Autowired
    private GlobalParamsService globalParamsService;
    @Autowired
    private TestSuitesTestCasesMapper testSuitesTestCasesMapper;
    @Autowired
    private TestSuitesService testSuitesService;
    @Autowired
    private TestCasesMapper testCasesMapper;
    @Autowired
    private StepsMapper stepsMapper;
    @Autowired
    private ElementsService elementsService;
    @Autowired
    private ModulesMapper modulesMapper;

    @Override
    public CommentPage<TestCasesDTO> findAll(int projectId, int platform, String name, List<Integer> moduleIds,
                                             List<String> caseAuthorNames,
                                             Page<TestCases> pageable,
                                             String idSort, String editTimeSort) {

        LambdaQueryChainWrapper<TestCases> lambdaQuery = lambdaQuery();

        lambdaQuery.eq(projectId != 0, TestCases::getProjectId, projectId)
                .eq(platform != 0, TestCases::getPlatform, platform)
                .in(moduleIds != null && moduleIds.size() > 0, TestCases::getModuleId, moduleIds)
                .in(caseAuthorNames != null && caseAuthorNames.size() > 0, TestCases::getDesigner, caseAuthorNames)
                .like(!StringUtils.isEmpty(name), TestCases::getName, name)
                .orderByDesc(StringUtils.isEmpty(editTimeSort) && StringUtils.isEmpty(idSort),
                        TestCases::getEditTime)
                .orderBy(!StringUtils.isEmpty(editTimeSort), "asc".equals(editTimeSort), TestCases::getEditTime)
                .orderBy(!StringUtils.isEmpty(idSort), "asc".equals(idSort), TestCases::getId);

        //写入对应模块信息
        Page<TestCases> page = lambdaQuery.page(pageable);
        List<TestCasesDTO> testCasesDTOS = page.getRecords()
                .stream().map(this::findCaseDetail).collect(Collectors.toList());

        return CommentPage.convertFrom(page, testCasesDTOS);
    }

    @Transactional
    public TestCasesDTO findCaseDetail(TestCases testCases) {
        if (testCases == null){
            return new TestCasesDTO().setId(0).setName("unknown");
        }

        if (testCases.getModuleId() != null && testCases.getModuleId() != 0) {
            Modules modules = modulesMapper.selectById(testCases.getModuleId());
            if (modules != null) {
                return testCases.convertTo().setModulesDTO(modules.convertTo());
            }
        }
        return testCases.convertTo();
    }

    @Override
    public List<TestCases> findAll(int projectId, int platform) {
        return lambdaQuery().eq(TestCases::getProjectId, projectId)
                .eq(TestCases::getPlatform, platform)
                .orderByDesc(TestCases::getEditTime)
                .list();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(int id) {
        if (existsById(id)) {
            // 删除suite映射关系
            testSuitesTestCasesMapper.delete(
                    new LambdaQueryWrapper<TestSuitesTestCases>()
                            .eq(TestSuitesTestCases::getTestCasesId, id)
            );

            List<StepsDTO> stepsList = stepsService.findByCaseIdOrderBySort(id, false);
            for (StepsDTO steps : stepsList) {
                steps.setCaseId(0);
                stepsService.updateById(steps.convertTo());
            }
            baseMapper.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public TestCasesDTO findById(int id) {
        TestCases testCases = baseMapper.selectById(id);
        return findCaseDetail(testCases);
    }

    @Transactional
    @Override
    public JSONObject findSteps(int id) {

        if (existsById(id)) {
            TestCases runStepCase = baseMapper.selectById(id);
            JSONObject jsonDebug = new JSONObject();
            jsonDebug.put("pf", runStepCase.getPlatform());

            JSONArray array = new JSONArray();
            List<StepsDTO> stepsList = stepsService.findByCaseIdOrderBySort(id, true);
            for (StepsDTO steps : stepsList) {
                array.add(testSuitesService.getStep(steps));
            }
            jsonDebug.put("steps", array);
            List<GlobalParams> globalParamsList = globalParamsService.findAll(runStepCase.getProjectId());
            JSONObject gp = new JSONObject();
            Map<String, List<String>> valueMap = new HashMap<>();
            for (GlobalParams g : globalParamsList) {
                if (g.getParamsValue().contains("|")) {
                    List<String> shuffle = Arrays.asList(g.getParamsValue().split("\\|"));
                    Collections.shuffle(shuffle);
                    valueMap.put(g.getParamsKey(), shuffle);
                } else {
                    gp.put(g.getParamsKey(), g.getParamsValue());
                }
            }
            for (String k : valueMap.keySet()) {
                if (valueMap.get(k).size() > 0) {
                    String v = valueMap.get(k).get(0);
                    gp.put(k, v);
                }
            }
            jsonDebug.put("gp", gp);
            return jsonDebug;
        } else {
            return null;
        }
    }

    @Override
    public List<TestCases> findByIdIn(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return listByIds(ids);
    }

    @Override
    public boolean deleteByProjectId(int projectId) {
        return baseMapper.delete(new LambdaQueryWrapper<TestCases>().eq(TestCases::getProjectId, projectId)) > 0;
    }

    @Override
    public List<TestCases> listByPublicStepsId(int publicStepsId) {
        List<Steps> steps = stepsService.lambdaQuery().eq(Steps::getText, publicStepsId).list();
        if (CollectionUtils.isEmpty(steps)) {
            return new ArrayList<>();
        }
        Set<Integer> caseIdSet = steps.stream().map(Steps::getCaseId).collect(Collectors.toSet());
        return lambdaQuery().in(TestCases::getId, caseIdSet).list();
    }

    /**
     * 测试用例的复制
     * 基本原理和公共步骤相同，不需要关联publicStep+step
     * 只需要关联了step+ele。
     *
     * @param oldId 需要复制的id
     * @return 返回成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyTestById(int oldId) {
        //插入新的testCase
        TestCases oldTestCases = testCasesMapper.selectById(oldId);
        save(oldTestCases.setId(null).setName(oldTestCases.getName() + "_copy"));

        //查找旧的case Step&&对应的ele
        LambdaQueryWrapper<Steps> queryWrapper = new LambdaQueryWrapper<>();
        List<Steps> oldStepsList = stepsMapper.selectList(
                queryWrapper.eq(Steps::getCaseId, oldId).orderByAsc(Steps::getSort));
        List<StepsDTO> stepsDTO = new ArrayList<>();
        for (Steps steps : oldStepsList) {
            stepsDTO.add(steps.convertTo());

        }
        List<StepsDTO> stepsCopyDTOS = stepsService.handleSteps(stepsDTO, false);

        //需要插入的步骤记录
        List<PublicStepsAndStepsIdDTO> needCopySteps = stepsService.stepAndIndex(stepsCopyDTOS);

        //插入新的步骤
        int n = 1;
        for (StepsDTO steps : stepsCopyDTOS) {
            Steps step = steps.convertTo();

            if (step.getParentId() != 0) {
                //如果有关联的父亲步骤， 就计算插入过得父亲ID 写入parentId
                Integer fatherIdIndex = 0;
                Integer idIndex = 0;
                //计算子步骤和父步骤的相对间距
                for (PublicStepsAndStepsIdDTO stepsIdDTO : needCopySteps) {
                    if (stepsIdDTO.getStepsDTO().convertTo().equals(step)) {
                        fatherIdIndex = stepsIdDTO.getIndex();
                    }
                    if (stepsIdDTO.getStepsDTO().convertTo().equals(stepsMapper.selectById(step.getParentId()))) {
                        idIndex = stepsIdDTO.getIndex();
                    }
                }
                step.setId(null).setParentId(fatherIdIndex).setCaseId(oldTestCases.getId()).setSort(stepsMapper.findMaxSort() + n);
                stepsMapper.insert(step.setCaseId(oldTestCases.getId()));
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
            step.setId(null).setCaseId(oldTestCases.getId()).setSort(stepsMapper.findMaxSort() + n);
            stepsMapper.insert(step);
            //关联steps和elId
            if (steps.getElements() != null) {
                elementsService.newStepBeLinkedEle(steps, step);
            }
            n++;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTestCaseModuleByModuleId(Integer module) {
        List<TestCases> testCasesList = lambdaQuery().eq(TestCases::getModuleId, module).list();
        if (testCasesList == null) {
            return true;
        }

        for (TestCases testCases : testCasesList) {
            save(testCases.setModuleId(0));
        }
        return true;
    }

    @Override
    public List<String> findAllCaseAuthor(int projectId, int platform) {
        return testCasesMapper.listAllTestCaseAuthor(projectId, platform);
    }
}

