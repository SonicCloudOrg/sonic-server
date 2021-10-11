package com.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.dao.GlobalParamsRepository;
import com.sonic.controller.dao.TestCasesRepository;
import com.sonic.controller.models.*;
import com.sonic.controller.services.PublicStepsService;
import com.sonic.controller.services.StepsService;
import com.sonic.controller.services.TestCasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * @author ZhouYiXun
 * @des 测试用例逻辑实现
 * @date 2021/8/20 17:51
 */
@Service
public class TestCasesServiceImpl implements TestCasesService {
    @Autowired
    private TestCasesRepository testCasesRepository;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private PublicStepsService publicStepsService;
    @Autowired
    private GlobalParamsRepository globalParamsRepository;

    @Override
    public Page<TestCases> findAll(int projectId, int platform, String name, Pageable pageable) {
        Specification<TestCases> spc = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (projectId != 0) {
                predicateList.add(cb.and(cb.equal(root.get("projectId"), projectId)));
            }
            if (platform != 0) {
                predicateList.add(cb.and(cb.equal(root.get("platform"), platform)));
            }
            if (name != null && name.length() > 0) {
                predicateList.add(cb.and(cb.like(root.get("name"), "%" + name + "%")));
            }
            //默认按照最后更新时间倒序
            query.orderBy(cb.desc(root.get("editTime")));
            if (predicateList.size() != 0) {
                Predicate[] p = new Predicate[predicateList.size()];
                return query.where(predicateList.toArray(p)).getRestriction();
            } else {
                return query.getRestriction();
            }
        };
        return testCasesRepository.findAll(spc, pageable);
    }

    @Override
    public List<TestCases> findAll(int projectId, int platform) {
        return testCasesRepository.findByProjectIdAndPlatformOrderByEditTimeDesc(projectId, platform);
    }

    @Override
    public boolean delete(int id) {
        if (testCasesRepository.existsById(id)) {
            TestCases testCases = testCasesRepository.findById(id).get();
            Set<TestSuites> testSuitesSet = testCases.getTestSuites();
            for (TestSuites testSuites : testSuitesSet) {
                testSuites.getTestCases().remove(testCases);
            }
            List<Steps> stepsList = stepsService.findByCaseIdOrderBySort(id);
            for (Steps steps : stepsList) {
                steps.setCaseId(0);
                stepsService.save(steps);
            }
            testCasesRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void save(TestCases testCases) {
        testCasesRepository.save(testCases);
    }

    @Override
    public TestCases findById(int id) {
        if (testCasesRepository.existsById(id)) {
            return testCasesRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public JSONObject findSteps(int id) {
        if (testCasesRepository.existsById(id)) {
            TestCases runStepCase = testCasesRepository.findById(id).get();
            JSONObject jsonDebug = new JSONObject();
            jsonDebug.put("pf", runStepCase.getPlatform());
            JSONArray array = new JSONArray();
            List<Steps> stepsList = stepsService.findByCaseIdOrderBySort(id);
            for (Steps steps : stepsList) {
                array.add(getStep(steps));
            }
            jsonDebug.put("steps", array);
            List<GlobalParams> globalParamsList = globalParamsRepository.findByProjectId(runStepCase.getProjectId());
            JSONObject gp = new JSONObject();
            Map<String, List<String>> valueMap = new HashMap<>();
            for (GlobalParams g : globalParamsList) {
                if (g.getParamsValue().contains("|")) {
                    List<String> shuffle = Arrays.asList(g.getParamsValue().split("|"));
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
        return testCasesRepository.findByIdIn(ids);
    }

    /**
     * @param steps
     * @return com.alibaba.fastjson.JSONObject
     * @author ZhouYiXun
     * @des 递归获取步骤
     * @date 2021/8/20 17:50
     */
    private JSONObject getStep(Steps steps) {
        JSONObject step = new JSONObject();
        if (steps.getStepType().equals("publicStep")) {
            PublicSteps publicSteps = publicStepsService.findById(Integer.parseInt(steps.getText()));
            if (publicSteps != null) {
                JSONArray publicStepsJson = new JSONArray();
                for (Steps pubStep : publicSteps.getSteps()) {
                    publicStepsJson.add(getStep(pubStep));
                }
                step.put("pubSteps", publicStepsJson);
            }
        }
        step.put("step", steps);
        return step;
    }
}
