package com.sonic.control.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sonic.control.dao.GlobalParamsRepository;
import com.sonic.control.dao.TestCasesRepository;
import com.sonic.control.models.PublicSteps;
import com.sonic.control.models.Steps;
import com.sonic.control.models.TestCases;
import com.sonic.control.models.TestSuites;
import com.sonic.control.services.PublicStepsService;
import com.sonic.control.services.StepsService;
import com.sonic.control.services.TestCasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            jsonDebug.put("gp", globalParamsRepository.findByProjectId(runStepCase.getProjectId()));
            return jsonDebug;
        } else {
            return null;
        }
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
        step.put("steps", step);
        return step;
    }
}
