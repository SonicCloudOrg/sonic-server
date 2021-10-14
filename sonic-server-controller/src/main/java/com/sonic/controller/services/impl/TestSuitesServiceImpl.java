package com.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.dao.AgentsRepository;
import com.sonic.controller.dao.GlobalParamsRepository;
import com.sonic.controller.dao.TestSuitesRepository;
import com.sonic.controller.feign.TransportFeignClient;
import com.sonic.controller.models.*;
import com.sonic.controller.models.interfaces.AgentStatus;
import com.sonic.controller.models.interfaces.CoverType;
import com.sonic.controller.models.interfaces.DeviceStatus;
import com.sonic.controller.models.interfaces.ResultStatus;
import com.sonic.controller.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * @author ZhouYiXun
 * @des 测试套件逻辑实现
 * @date 2021/8/20 17:51
 */
@Service
public class TestSuitesServiceImpl implements TestSuitesService {
    @Autowired
    private TestSuitesRepository testSuitesRepository;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private PublicStepsService publicStepsService;
    @Autowired
    private ResultsService resultsService;
    @Autowired
    private AgentsService agentsService;
    @Autowired
    private GlobalParamsRepository globalParamsRepository;
    @Autowired
    private TransportFeignClient transportFeignClient;

    @Override
    public RespModel runSuite(int suiteId, String strike) {
        TestSuites testSuites;
        if (testSuitesRepository.existsById(suiteId)) {
            testSuites = testSuitesRepository.findById(suiteId).get();
        } else {
            return new RespModel(3001, "测试套件已删除！");
        }
        if (testSuites.getTestCases().size() == 0) {
            return new RespModel(3002, "该测试套件内无测试用例！");
        }
        List<Devices> devicesList = new ArrayList<>(testSuites.getDevices());
        for (int i = devicesList.size() - 1; i >= 0; i--) {
            if (devicesList.get(i).getStatus().equals(DeviceStatus.OFFLINE) || devicesList.get(i).getStatus().equals(DeviceStatus.DISCONNECTED)) {
                devicesList.remove(devicesList.get(i));
            }
        }
        if (devicesList.size() == 0) {
            return new RespModel(3003, "所选设备暂无可用！");
        }
        Results results = new Results();
        results.setStatus(ResultStatus.RUNNING);
        results.setSuiteId(suiteId);
        results.setSuiteName(testSuites.getName());
        results.setStrike(strike);
        if (testSuites.getCover() == CoverType.CASE) {
            results.setSendMsgCount(testSuites.getTestCases().size());
        }
        if (testSuites.getCover() == CoverType.DEVICE) {
            results.setSendMsgCount(testSuites.getTestCases().size() * testSuites.getDevices().size());
        }
        results.setReceiveMsgCount(0);
        results.setProjectId(testSuites.getProjectId());
        resultsService.save(results);
        //组装全局参数为json对象
        List<GlobalParams> globalParamsList = globalParamsRepository.findByProjectId(testSuites.getProjectId());
        //将包含|的拆开多个参数并打乱，去掉json对象多参数的字段
        Map<String, List<String>> valueMap = new HashMap<>();
        JSONObject gp = new JSONObject();
        for (GlobalParams g : globalParamsList) {
            if (g.getParamsValue().contains("|")) {
                List<String> shuffle = Arrays.asList(g.getParamsValue().split("|"));
                Collections.shuffle(shuffle);
                valueMap.put(g.getParamsKey(), shuffle);
            } else {
                gp.put(g.getParamsKey(), g.getParamsValue());
            }
        }
        int deviceIndex = 0;
        if (testSuites.getCover() == CoverType.CASE) {
            for (TestCases testCases : testSuites.getTestCases()) {
                JSONObject suite = new JSONObject();
                List<JSONObject> steps = new ArrayList<>();
                List<Steps> stepsList = stepsService.findByCaseIdOrderBySort(testCases.getId());
                for (Steps s : stepsList) {
                    steps.add(getStep(s));
                }
                suite.put("steps", steps);
                suite.put("cid", testCases.getId());
                Devices devices = devicesList.get(deviceIndex);
                suite.put("device", devices);
                if (deviceIndex == devicesList.size() - 1) {
                    deviceIndex = 0;
                } else {
                    deviceIndex++;
                }
                //如果该字段的多参数数组还有，放入对象。否则去掉字段
                for (String k : valueMap.keySet()) {
                    if (valueMap.get(k).size() > 0) {
                        String v = valueMap.get(k).get(0);
                        gp.put(k, v);
                        valueMap.get(k).remove(0);
                    } else {
                        valueMap.remove(k);
                    }
                }
                suite.put("gp", gp);
                suite.put("rid", results.getId());
                String key = agentsService.findKeyById(devices.getAgentId());
                suite.put("key", key);
                suite.put("wait", 0);
                RespModel testDataResp = transportFeignClient.sendTestData(suite);
                if (testDataResp.getCode() != 2000) {
                    resultsService.subResultCount(results.getId());
                }
            }
        }
        if (testSuites.getCover() == CoverType.DEVICE) {
            for (TestCases testCases : testSuites.getTestCases()) {
                JSONObject suite = new JSONObject();
                List<JSONObject> steps = new ArrayList<>();
                List<Steps> stepsList = stepsService.findByCaseIdOrderBySort(testCases.getId());
                for (Steps s : stepsList) {
                    steps.add(getStep(s));
                }
                for (Devices devices : devicesList) {
                    suite.put("steps", steps);
                    suite.put("cid", testCases.getId());
                    suite.put("device", devices);
                    //如果该字段的多参数数组还有，放入对象。否则去掉字段
                    for (String k : valueMap.keySet()) {
                        if (valueMap.get(k).size() > 0) {
                            String v = valueMap.get(k).get(0);
                            gp.put(k, v);
                            valueMap.get(k).remove(0);
                        } else {
                            valueMap.remove(k);
                        }
                    }
                    suite.put("gp", gp);
                    suite.put("rid", results.getId());
                    String key = agentsService.findKeyById(devices.getAgentId());
                    suite.put("key", key);
                    suite.put("wait", 0);
                    RespModel testDataResp = transportFeignClient.sendTestData(suite);
                    if (testDataResp.getCode() != 2000) {
                        resultsService.subResultCount(results.getId());
                    }
                }
            }
        }
        return new RespModel(RespEnum.HANDLE_OK);
    }

    @Override
    public TestSuites findById(int id) {
        if (testSuitesRepository.existsById(id)) {
            TestSuites testSuites = testSuitesRepository.findById(id).get();
            Set<TestCases> testCasesSet = new HashSet<>(testSuites.getTestCases());
            testSuites.setTestCases(new ArrayList<>(testCasesSet));
            return testSuites;
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
        step.put("step", steps);
        return step;
    }

    @Override
    public boolean delete(int id) {
        if (testSuitesRepository.existsById(id)) {
            testSuitesRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void save(TestSuites testSuites) {
        testSuitesRepository.save(testSuites);
    }

    @Override
    public Page<TestSuites> findByProjectId(int projectId, String name, Pageable pageable) {
        Specification<TestSuites> spc = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (projectId != 0) {
                predicateList.add(cb.and(cb.equal(root.get("projectId"), projectId)));
            }
            if (name != null && name.length() > 0) {
                predicateList.add(cb.and(cb.like(root.get("name"), "%" + name + "%")));
            }
            query.orderBy(cb.desc(root.get("id")));
            if (predicateList.size() != 0) {
                Predicate[] p = new Predicate[predicateList.size()];
                return query.where(predicateList.toArray(p)).getRestriction();
            } else {
                return query.getRestriction();
            }
        };
        return testSuitesRepository.findAll(spc, pageable);
    }

    @Override
    public List<TestSuites> findByProjectId(int projectId) {
        return testSuitesRepository.findByProjectId(projectId, Sort.by(Sort.Direction.DESC, "id"));
    }
}