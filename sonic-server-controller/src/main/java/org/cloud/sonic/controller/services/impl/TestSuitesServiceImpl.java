package org.cloud.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.BeanTool;
import org.cloud.sonic.controller.feign.TransportFeignClient;
import org.cloud.sonic.controller.mapper.*;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.*;
import org.cloud.sonic.controller.models.dto.*;
import org.cloud.sonic.controller.models.interfaces.CoverType;
import org.cloud.sonic.controller.models.interfaces.DeviceStatus;
import org.cloud.sonic.controller.models.interfaces.ResultStatus;
import org.cloud.sonic.controller.services.*;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZhouYiXun
 * @des 测试套件逻辑实现
 * @date 2021/8/20 17:51
 */
@Service
public class TestSuitesServiceImpl extends SonicServiceImpl<TestSuitesMapper, TestSuites> implements TestSuitesService {

    @Autowired private TestCasesMapper testCasesMapper;
    @Autowired private ElementsMapper elementsMapper;
    @Autowired private DevicesMapper devicesMapper;
    @Autowired private ResultsService resultsService;
    @Autowired private GlobalParamsService globalParamsService;
    @Autowired private StepsService stepsService;
    @Autowired private PublicStepsService publicStepsService;
    @Autowired private TestSuitesTestCasesMapper testSuitesTestCasesMapper;
    @Autowired private TestSuitesDevicesMapper testSuitesDevicesMapper;
    @Autowired private TransportFeignClient transportFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespModel<String> runSuite(int suiteId, String strike) {
        TestSuitesDTO testSuitesDTO;
        if (existsById(suiteId)) {
            testSuitesDTO = findById(suiteId);
        } else {
            return new RespModel<>(3001, "测试套件已删除！");
        }

        if (testSuitesDTO.getTestCases().size() == 0) {
            return new RespModel<>(3002, "该测试套件内无测试用例！");
        }

        List<Devices> devicesList = BeanTool.transformFromInBatch(testSuitesDTO.getDevices(), Devices.class);
        if (devicesList.size() == 0) {
            return new RespModel<>(3003, "所选设备暂无可用！");
        }

        // 初始化部分结果状态信息
        Results results = new Results();
        results.setStatus(ResultStatus.RUNNING);
        results.setSuiteId(suiteId);
        results.setSuiteName(testSuitesDTO.getName());
        results.setStrike(strike);
        if (testSuitesDTO.getCover() == CoverType.CASE) {
            results.setSendMsgCount(testSuitesDTO.getTestCases().size());
        }
        if (testSuitesDTO.getCover() == CoverType.DEVICE) {
            results.setSendMsgCount(testSuitesDTO.getTestCases().size() * testSuitesDTO.getDevices().size());
        }
        results.setReceiveMsgCount(0);
        results.setProjectId(testSuitesDTO.getProjectId());
        resultsService.save(results);

        //组装全局参数为json对象
        List<GlobalParams> globalParamsList = globalParamsService.findAll(testSuitesDTO.getProjectId());

        //将包含|的拆开多个参数并打乱，去掉json对象多参数的字段
        Map<String, List<String>> valueMap = new HashMap<>();
        JSONObject gp = new JSONObject();
        for (GlobalParams g : globalParamsList) {
            if (g.getParamsValue().contains("|")) {
                List<String> shuffle = Arrays.asList(g.getParamsValue().split("\\|"));
                Collections.shuffle(shuffle);
                valueMap.put(g.getParamsKey(), shuffle);
            } else {
                gp.put(g.getParamsKey(), g.getParamsValue());
            }
        }
        int deviceIndex = 0;
        if (testSuitesDTO.getCover() == CoverType.CASE) {
            List<JSONObject> suiteDetail = new ArrayList<>();
            Set<Integer> agentIds = new HashSet<>();
            for (TestCasesDTO testCases : testSuitesDTO.getTestCases()) {
                JSONObject suite = new JSONObject();
                List<JSONObject> steps = new ArrayList<>();
                List<StepsDTO> stepsList = stepsService.findByCaseIdOrderBySort(testCases.getId());
                for (StepsDTO s : stepsList) {
                    steps.add(getStep(s));
                }
                suite.put("steps", steps);
                suite.put("cid", testCases.getId());
                Devices devices = devicesList.get(deviceIndex);
                suite.put("device", List.of(devices));
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
                agentIds.add(devices.getAgentId());
                suiteDetail.add(suite);
            }
            JSONObject result = new JSONObject();
            result.put("cases", suiteDetail);
            for (Integer id : agentIds) {
                result.put("id", id);
                result.put("pf", testSuitesDTO.getPlatform());
                result.put("msg", "suite");
                transportFeignClient.sendTestData(result);
            }
        }
        if (testSuitesDTO.getCover() == CoverType.DEVICE) {
            List<JSONObject> suiteDetail = new ArrayList<>();
            Set<Integer> agentIds = new HashSet<>();
            for (TestCasesDTO testCases : testSuitesDTO.getTestCases()) {
                JSONObject suite = new JSONObject();
                List<JSONObject> steps = new ArrayList<>();
                List<StepsDTO> stepsList = stepsService.findByCaseIdOrderBySort(testCases.getId());
                for (StepsDTO s : stepsList) {
                    steps.add(getStep(s));
                }
                for (Devices devices : devicesList) {
                    agentIds.add(devices.getAgentId());
                }
                suite.put("steps", steps);
                suite.put("cid", testCases.getId());
                suite.put("device", devicesList);
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
                suiteDetail.add(suite);
            }
            JSONObject result = new JSONObject();
            result.put("cases", suiteDetail);
            for (Integer id : agentIds) {
                result.put("id", id);
                result.put("pf", testSuitesDTO.getPlatform());
                result.put("msg", "suite");
                transportFeignClient.sendTestData(result);
            }
        }
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespModel<String> forceStopSuite(int resultId, String strike) {

        Results results = resultsService.findById(resultId);
        if (ObjectUtils.isEmpty(results)) {
            return new RespModel<>(3001, "测试结果模板不存在！");
        }
        int suiteId = results.getSuiteId();

        TestSuitesDTO testSuitesDTO;
        if (existsById(suiteId)) {
            testSuitesDTO = findById(suiteId);
        } else {
            return new RespModel<>(3001, "测试套件已删除！");
        }

        if (testSuitesDTO.getTestCases().size() == 0) {
            return new RespModel<>(3002, "该测试套件内无测试用例！");
        }

        List<Devices> devicesList = BeanTool.transformFromInBatch(testSuitesDTO.getDevices(), Devices.class);
        for (int i = devicesList.size() - 1; i >= 0; i--) {
            if (devicesList.get(i).getStatus().equals(DeviceStatus.OFFLINE) || devicesList.get(i).getStatus().equals(DeviceStatus.DISCONNECTED)) {
                devicesList.remove(devicesList.get(i));
            }
        }
        if (devicesList.size() == 0) {
            return new RespModel<>(3003, "运行设备暂无法连接！");
        }

        results.setStatus(ResultStatus.FAIL);
        results.setStrike(strike);
        if (testSuitesDTO.getCover() == CoverType.CASE) {
            results.setSendMsgCount(testSuitesDTO.getTestCases().size());
        }
        if (testSuitesDTO.getCover() == CoverType.DEVICE) {
            results.setSendMsgCount(testSuitesDTO.getTestCases().size() * testSuitesDTO.getDevices().size());
        }
        results.setProjectId(testSuitesDTO.getProjectId());
        resultsService.save(results);


        int deviceIndex = 0;
        if (testSuitesDTO.getCover() == CoverType.CASE) {
            List<JSONObject> suiteDetail = new ArrayList<>();
            Set<Integer> agentIds = new HashSet<>();
            for (TestCasesDTO testCases : testSuitesDTO.getTestCases()) {
                JSONObject suite = new JSONObject();
                suite.put("cid", testCases.getId());
                Devices devices = devicesList.get(deviceIndex);
                suite.put("device", List.of(devices));
                if (deviceIndex == devicesList.size() - 1) {
                    deviceIndex = 0;
                } else {
                    deviceIndex++;
                }
                suite.put("rid", results.getId());
                agentIds.add(devices.getAgentId());
                suiteDetail.add(suite);
            }
            JSONObject result = new JSONObject();
            result.put("msg", "forceStopSuite");
            result.put("pf", testSuitesDTO.getPlatform());
            result.put("cases", suiteDetail);
            for (Integer id : agentIds) {
                result.put("id", id);
                transportFeignClient.sendTestData(result);
            }
        }
        if (testSuitesDTO.getCover() == CoverType.DEVICE) {
            List<JSONObject> suiteDetail = new ArrayList<>();
            Set<Integer> agentIds = new HashSet<>();
            for (TestCasesDTO testCases : testSuitesDTO.getTestCases()) {
                JSONObject suite = new JSONObject();
                for (Devices devices : devicesList) {
                    agentIds.add(devices.getAgentId());
                }
                suite.put("cid", testCases.getId());
                suite.put("device", devicesList);
                suite.put("rid", results.getId());
                suiteDetail.add(suite);
            }
            JSONObject result = new JSONObject();
            result.put("msg", "forceStopSuite");
            result.put("pf", testSuitesDTO.getPlatform());
            result.put("cases", suiteDetail);
            for (Integer id : agentIds) {
                result.put("id", id);
                transportFeignClient.sendTestData(result);
            }
        }
        return new RespModel<>(RespEnum.HANDLE_OK);
    }

    @Override
    @Transactional
    public TestSuitesDTO findById(int id) {
        if (existsById(id)) {
            TestSuitesDTO testSuitesDTO = baseMapper.selectById(id).convertTo();
            int suiteId = testSuitesDTO.getId();

            // 填充testcase
            List<TestCasesDTO> testCasesDTOList = testCasesMapper.listByTestSuitesId(suiteId)
                    .stream().map(TypeConverter::convertTo).collect(Collectors.toList());
            testSuitesDTO.setTestCases(testCasesDTOList);

            // 填充devices
            List<DevicesDTO> devicesDTOList = devicesMapper.listByTestSuitesId(suiteId)
                    .stream().map(TypeConverter::convertTo).collect(Collectors.toList());
            testSuitesDTO.setDevices(devicesDTOList);

            return testSuitesDTO;
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
    @Transactional
    public JSONObject getStep(StepsDTO steps) {
        JSONObject step = new JSONObject();
        if (steps.getStepType().equals("publicStep")) {
            PublicStepsDTO publicStepsDTO = publicStepsService.findById(Integer.parseInt(steps.getText()));
            if (publicStepsDTO != null) {
                JSONArray publicStepsJson = new JSONArray();
                for (StepsDTO pubStep : publicStepsDTO.getSteps()) {
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
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTestSuites(TestSuitesDTO testSuitesDTO) {
        TestSuites testSuites = testSuitesDTO.convertTo();
        save(testSuites);

        Integer suiteId = testSuites.getId();
        testSuitesDTO.setId(suiteId);

        List<TestCasesDTO> testCases = testSuitesDTO.getTestCases();
        List<DevicesDTO> devices = testSuitesDTO.getDevices();

        // 删除旧数据
        testSuitesDevicesMapper.delete(new LambdaQueryWrapper<TestSuitesDevices>()
                .eq(TestSuitesDevices::getTestSuitesId, suiteId)
        );
        testSuitesTestCasesMapper.delete(new LambdaQueryWrapper<TestSuitesTestCases>()
                .eq(TestSuitesTestCases::getTestSuitesId, suiteId)
        );

        // 保存testcase映射
        for (TestCasesDTO testCase : testCases) {
            testSuitesTestCasesMapper.insert(
                    new TestSuitesTestCases().setTestSuitesId(suiteId).setTestCasesId(testCase.getId())
            );
        }

        // 保存devices映射
        for (DevicesDTO device : devices) {
            testSuitesDevicesMapper.insert(
                    new TestSuitesDevices().setTestSuitesId(suiteId).setDevicesId(device.getId())
            );
        }
    }

    @Override
    @Transactional
    public CommentPage<TestSuitesDTO> findByProjectId(int projectId, String name, Page<TestSuites> pageable) {

        LambdaQueryChainWrapper<TestSuites> lambdaQuery = lambdaQuery();

        if (projectId != 0) {
            lambdaQuery.eq(TestSuites::getProjectId, projectId);
        }
        if (name != null && name.length() > 0) {
            lambdaQuery.like(TestSuites::getName, name);
        }

        lambdaQuery.orderByDesc(TestSuites::getId);
        Page<TestSuites> page = lambdaQuery.page(pageable);

        List<TestSuitesDTO> testSuitesDTOList = page.getRecords()
                // 转换 + 填充 testcase 和 devices
                .stream().map(e -> findById(e.getId())).collect(Collectors.toList());

        return CommentPage.convertFrom(page, testSuitesDTOList);
    }

    @Override
    public List<TestSuitesDTO> findByProjectId(int projectId) {
        return lambdaQuery().eq(TestSuites::getProjectId, projectId)
                .orderByDesc(TestSuites::getId)
                .list()
                // 转换 + 填充 testcase 和 devices
                .stream().map(e -> findById(e.getId())).collect(Collectors.toList());
    }

    @Override
    public boolean deleteByProjectId(int projectId) {
        return baseMapper.delete(new LambdaQueryWrapper<TestSuites>().eq(TestSuites::getProjectId, projectId)) > 0;
    }

    @Override
    public List<TestSuites> listTestSuitesByTestCasesId(int testCasesId) {
        return testSuitesTestCasesMapper.listTestSuitesByTestCasesId(testCasesId);
    }
}