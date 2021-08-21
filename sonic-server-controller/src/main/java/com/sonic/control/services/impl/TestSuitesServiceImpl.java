package com.sonic.control.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.control.dao.AgentsRepository;
import com.sonic.control.dao.GlobalParamsRepository;
import com.sonic.control.dao.TestSuitesRepository;
import com.sonic.control.feign.TransportFeignClient;
import com.sonic.control.models.*;
import com.sonic.control.models.interfaces.DeviceStatus;
import com.sonic.control.models.interfaces.ResultStatus;
import com.sonic.control.services.PublicStepsService;
import com.sonic.control.services.ResultsService;
import com.sonic.control.services.StepsService;
import com.sonic.control.services.TestSuitesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
    private AgentsRepository agentsRepository;
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
            return new RespModel(3000, "测试套件已删除！");
        }
        if (testSuites.getTestCases().size() == 0) {
            return new RespModel(3000, "该测试套件内无测试用例！");
        }
        Set<Devices> devicesList = testSuites.getDevices();
        int onLineCount = 0;
        for (Devices devices :devicesList) {
            if (devices.getStatus().equals(DeviceStatus.ONLINE)) {
                onLineCount++;
            }
        }
        if(onLineCount==0){
            return new RespModel(3000, "所选设备暂无可用！");
        }
        Results results = new Results();
        results.setStatus(ResultStatus.RUNNING);
        results.setSuiteId(suiteId);
        results.setSuiteName(testSuites.getName());
        results.setStrike(strike);
        results.setProjectId(testSuites.getProjectId());
        resultsService.save(results);
        JSONObject jsonSuite = new JSONObject();
        JSONArray suiteArray = new JSONArray();
        for (TestCases testCases : testSuites.getTestCases()) {
            JSONObject cases = new JSONObject();
            JSONArray arraySuite = new JSONArray();
            List<Steps> stepsList = stepsService.findByCaseIdOrderBySort(testCases.getId());
            for (Steps steps : stepsList) {
                arraySuite.add(getStep(steps));
            }
            cases.put("steps", arraySuite);
            cases.put("case", testCases);
            suiteArray.add(cases);
        }
        jsonSuite.put("mt", testSuites.getModuleThread());
        jsonSuite.put("dt", testSuites.getDeviceThread());
        jsonSuite.put("ct", testSuites.getCaseThread());
        jsonSuite.put("gp", globalParamsRepository.findByProjectId(testSuites.getProjectId()));
        jsonSuite.put("suite", suiteArray);
        jsonSuite.put("sp", testSuites.getPlatform());
        jsonSuite.put("rid", results.getId());
        JSONObject sonicData = new JSONObject();
        for (Devices devices : devicesList) {
            sonicData.put(devices.getUdId(), devices.getPassword());
        }
        JSONObject testData = new JSONObject();
        testData.put("sonicAssist", sonicData);
        testData.put("suiteData", jsonSuite);
        RespModel testDataResp = transportFeignClient.sendTestData(testData);
        if (testDataResp.getError() != 0) {
            return new RespModel(RespEnum.SERVICE_NOT_FOUND);
        }
        return new RespModel(RespEnum.HANDLE_OK);
    }

    @Override
    public TestSuites findById(int id) {
        if (testSuitesRepository.existsById(id)) {
            return testSuitesRepository.findById(id).get();
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
    public List<TestSuites> findByProjectId(int projectId) {
        return testSuitesRepository.findByProjectId(projectId, Sort.by(Sort.Direction.DESC, "id"));
    }
}