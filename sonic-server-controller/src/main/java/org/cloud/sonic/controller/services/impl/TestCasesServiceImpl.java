package org.cloud.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.controller.mapper.PublicStepsMapper;
import org.cloud.sonic.controller.mapper.TestCasesMapper;
import org.cloud.sonic.controller.mapper.TestSuitesTestCasesMapper;
import org.cloud.sonic.controller.models.domain.*;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import org.cloud.sonic.controller.services.GlobalParamsService;
import org.cloud.sonic.controller.services.StepsService;
import org.cloud.sonic.controller.services.TestCasesService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZhouYiXun
 * @des 测试用例逻辑实现
 * @date 2021/8/20 17:51
 */
@Service
public class TestCasesServiceImpl extends SonicServiceImpl<TestCasesMapper, TestCases> implements TestCasesService {

    @Autowired private StepsService stepsService;
    @Autowired private PublicStepsMapper publicStepsMapper;
    @Autowired private GlobalParamsService globalParamsService;
    @Autowired private TestSuitesTestCasesMapper testSuitesTestCasesMapper;

    @Override
    public Page<TestCases> findAll(int projectId, int platform, String name, Page<TestCases> pageable) {

        LambdaQueryChainWrapper<TestCases> lambdaQuery = lambdaQuery();
        if (projectId != 0) {
            lambdaQuery.eq(TestCases::getProjectId, projectId);
        }
        if (platform != 0) {
            lambdaQuery.eq(TestCases::getPlatform, platform);
        }
        if (name != null && name.length() > 0) {
            lambdaQuery.like(TestCases::getName, name);
        }

        return lambdaQuery.orderByDesc(TestCases::getEditTime)
                .page(pageable);
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

            List<StepsDTO> stepsList = stepsService.findByCaseIdOrderBySort(id);
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
    public TestCases findById(int id) {
        return baseMapper.selectById(id);
    }

    @Transactional
    @Override
    public JSONObject findSteps(int id) {

        if (existsById(id)) {
            TestCases runStepCase = baseMapper.selectById(id);
            JSONObject jsonDebug = new JSONObject();
            jsonDebug.put("pf", runStepCase.getPlatform());

            JSONArray array = new JSONArray();
            List<StepsDTO> stepsList = stepsService.findByCaseIdOrderBySort(id);
            for (StepsDTO steps : stepsList) {
                array.add(getStep(steps));
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
        return listByIds(ids);
    }

    /**
     * @param steps
     * @return com.alibaba.fastjson.JSONObject
     * @author ZhouYiXun
     * @des 递归获取步骤
     * @date 2021/8/20 17:50
     */
    private JSONObject getStep(StepsDTO steps) {
        JSONObject step = new JSONObject();
        if (steps.getStepType().equals("publicStep")) {
            PublicSteps publicSteps = publicStepsMapper.selectById(Integer.parseInt(steps.getText()));

            if (publicSteps != null) {
                List<StepsDTO> stepsList = stepsService.listByPublicStepsId(publicSteps.getId());
                JSONArray publicStepsJson = new JSONArray();
                for (StepsDTO pubStep : stepsList) {
                    publicStepsJson.add(getStep(pubStep));
                }
                step.put("pubSteps", publicStepsJson);
            }
        }
        step.put("step", steps);
        return step;
    }

    @Override
    public boolean deleteByProjectId(int projectId) {
        return baseMapper.delete(new LambdaQueryWrapper<>()) > 0;
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
}
