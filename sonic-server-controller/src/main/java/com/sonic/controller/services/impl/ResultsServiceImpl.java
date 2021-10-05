package com.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.dao.ResultDetailRepository;
import com.sonic.controller.dao.ResultsRepository;
import com.sonic.controller.dao.TestSuitesRepository;
import com.sonic.controller.models.*;
import com.sonic.controller.models.interfaces.ResultDetailStatus;
import com.sonic.controller.models.interfaces.ResultStatus;
import com.sonic.controller.services.*;
import com.sonic.controller.tools.RobotMsgTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ZhouYiXun
 * @des 测试结果逻辑实现
 * @date 2021/8/21 16:09
 */
@Service
public class ResultsServiceImpl implements ResultsService {
    private final Logger logger = LoggerFactory.getLogger(ResultsServiceImpl.class);
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    @Autowired
    private ResultsRepository resultsRepository;
    @Autowired
    private ResultDetailService resultDetailService;
    @Autowired
    private ProjectsService projectsService;
    @Autowired
    private RobotMsgTool robotMsgTool;
    @Autowired
    private TestSuitesService testSuitesService;
    @Autowired
    private ResultDetailRepository resultDetailRepository;

    @Override
    public Page<Results> findByProjectId(int projectId, Pageable pageable) {
        return resultsRepository.findByProjectId(projectId, pageable);
    }

    @Override
    public boolean delete(int id) {
        if (resultsRepository.existsById(id)) {
            resultsRepository.deleteById(id);
            resultDetailService.deleteByResultId(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Results findById(int id) {
        if (resultsRepository.existsById(id)) {
            return resultsRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public void save(Results results) {
        resultsRepository.saveAndFlush(results);
    }

    @Override
    public void clean(int day) {
        long timeMillis = Calendar.getInstance().getTimeInMillis();
        long time = timeMillis - day * 86400000L;
        List<Results> resultsList = resultsRepository.findByCreateTimeBefore(new Date(time));
        cachedThreadPool.execute(() -> {
            for (Results results : resultsList) {
                logger.info("清理测试报告id：" + results.getId());
                delete(results.getId());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void suiteResult(int id) {
        Results results = findById(id);
        if (results != null) {
            results.setReceiveMsgCount(results.getReceiveMsgCount() + 1);
            setStatus(results);
        }
    }

    @Override
    public JSONArray findCaseStatus(int id) {
        Results results = findById(id);
        if (results != null) {
            TestSuites testSuites = testSuitesService.findById(results.getSuiteId());
            if (testSuites != null) {
                JSONArray result = new JSONArray();
                List<TestCases> testCasesList = testSuites.getTestCases();
                List<JSONObject> caseTimes = resultDetailRepository.findTimeByResultIdGroupByCaseId(results.getId());
                List<ResultDetail> statusList = resultDetailService.findAll(results.getId(), 0, "status", 0);
                for (TestCases testCases : testCasesList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("case", testCases);
                    for (int j = caseTimes.size() - 1; j >= 0; j--) {
                        if (caseTimes.get(j).getInteger("case_id") == testCases.getId()) {
                            jsonObject.put("startTime", caseTimes.get(j).getDate("startTime"));
                            jsonObject.put("endTime", caseTimes.get(j).getDate("endTime"));
                            caseTimes.remove(j);
                            break;
                        }
                    }
                    List<JSONObject> device = new ArrayList<>();
                    for (int i = statusList.size() - 1; i >= 0; i--) {
                        if (statusList.get(i).getCaseId() == testCases.getId()) {
                            JSONObject deviceIdAndStatus = new JSONObject();
                            deviceIdAndStatus.put("deviceId", statusList.get(i).getDeviceId());
                            deviceIdAndStatus.put("status", statusList.get(i).getStatus());
                            device.add(deviceIdAndStatus);
                            statusList.remove(i);
                        }
                    }
                    jsonObject.put("device", device);
                    result.add(jsonObject);
                }
                return result;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void subResultCount(int id) {
        Results results = findById(id);
        if (results != null) {
            results.setSendMsgCount(results.getSendMsgCount() - 1);
            setStatus(results);
        }
    }

    public void setStatus(Results results) {
        List<ResultDetail> resultDetailList = resultDetailService.findAll(results.getId(), 0, "status", 0);
        int failCount = 0;
        int sucCount = 0;
        int warnCount = 0;
        int status;
        for (ResultDetail resultDetail : resultDetailList) {
            if (resultDetail.getStatus() == ResultDetailStatus.FAIL) {
                failCount++;
            } else if (resultDetail.getStatus() == ResultDetailStatus.WARN) {
                warnCount++;
            } else {
                sucCount++;
            }
        }
        if (failCount > 0) {
            status = ResultStatus.FAIL;
        } else if (warnCount > 0) {
            status = ResultStatus.WARNING;
        } else {
            status = ResultStatus.PASS;
        }
        //状态赋予等级最高的
        results.setStatus(status > results.getStatus() ? status : results.getStatus());
        if (results.getSendMsgCount() < 1 && sucCount == 0 && failCount == 0 && warnCount == 0) {
            delete(results.getId());
        } else {
            save(results);
            //发收相同的话，表明测试结束了
            if (results.getReceiveMsgCount() == results.getSendMsgCount()) {
                results.setEndTime(new Date());
                Projects projects = projectsService.findById(results.getProjectId());
                if (projects != null && projects.getRobotToken().length() > 0 && projects.getRobotSecret().length() > 0) {
                    robotMsgTool.sendResultFinishReport(projects.getRobotToken(), projects.getRobotSecret(),
                            results.getSuiteName(), sucCount, warnCount, failCount, projects.getId(), results.getId());
                }
            }
        }
    }
}
