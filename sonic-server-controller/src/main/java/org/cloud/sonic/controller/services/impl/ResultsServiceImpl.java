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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cloud.sonic.controller.mapper.ResultsMapper;
import org.cloud.sonic.controller.models.domain.Projects;
import org.cloud.sonic.controller.models.domain.ResultDetail;
import org.cloud.sonic.controller.models.domain.Results;
import org.cloud.sonic.controller.models.domain.TestCases;
import org.cloud.sonic.controller.models.dto.TestSuitesDTO;
import org.cloud.sonic.controller.models.interfaces.ResultDetailStatus;
import org.cloud.sonic.controller.models.interfaces.ResultStatus;
import org.cloud.sonic.controller.services.*;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ZhouYiXun
 * @des 测试结果逻辑实现
 * @date 2021/8/21 16:09
 */
@Service
public class ResultsServiceImpl extends SonicServiceImpl<ResultsMapper, Results> implements ResultsService {

    private final Logger logger = LoggerFactory.getLogger(ResultsServiceImpl.class);
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    @Autowired
    private ResultsMapper resultsMapper;
    @Autowired
    private ResultDetailService resultDetailService;
    @Autowired
    private ProjectsService projectsService;
    @Autowired
    private AlertRobotsService alertRobotsService;
    @Autowired
    private TestSuitesService testSuitesService;
    @Autowired
    private TestCasesService testCasesService;

    @Override
    public Page<Results> findByProjectId(int projectId, Page<Results> pageable) {
        return lambdaQuery().eq(Results::getProjectId, projectId)
                .orderByDesc(Results::getId)
                .page(pageable);
    }

    @Override
    public List<Results> findByProjectId(int projectId) {
        return lambdaQuery().eq(Results::getProjectId, projectId).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(int id) {
        int count = resultsMapper.deleteById(id);
        resultDetailService.deleteByResultId(id);
        return count > 0;
    }

    @Override
    public Results findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void clean(int day) {
        long timeMillis = Calendar.getInstance().getTimeInMillis();
        long time = timeMillis - day * 86400000L;
        List<Results> resultsList = lambdaQuery()
                .lt(Results::getCreateTime, new Date(time))
                .list();
        cachedThreadPool.execute(() -> {
            for (Results results : resultsList) {
                logger.info("clear report id: " + results.getId());
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
            save(results);
            setStatus(results);
        }
    }

    @Override
    @Transactional
    public JSONArray findCaseStatus(int id) {
        Results results = findById(id);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (results != null) {
            TestSuitesDTO testSuitesDTO = testSuitesService.findById(results.getSuiteId());
            if (testSuitesDTO != null) {
                JSONArray result = new JSONArray();
                List<JSONObject> caseTimes = resultDetailService.findTimeByResultIdGroupByCaseId(results.getId());
                List<Integer> ci = new ArrayList<>();
                for (JSONObject j : caseTimes) {
                    ci.add(j.getInteger("case_id"));
                }
                List<TestCases> testCasesList = testCasesService.findByIdIn(ci);
                List<JSONObject> statusList = resultDetailService.findStatusByResultIdGroupByCaseId(results.getId());
                for (TestCases testCases : testCasesList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("case", testCases);
                    int status = 0;
                    for (int j = caseTimes.size() - 1; j >= 0; j--) {
                        if (Objects.equals(caseTimes.get(j).getInteger("case_id"), testCases.getId())) {
                            jsonObject.put("startTime", sf.format(caseTimes.get(j).getDate("startTime")));
                            jsonObject.put("endTime", sf.format(caseTimes.get(j).getDate("endTime")));
                            caseTimes.remove(j);
                            break;
                        }
                    }
                    List<JSONObject> device = new ArrayList<>();
                    for (int i = statusList.size() - 1; i >= 0; i--) {
                        if (statusList.get(i).getInteger("case_id").equals(testCases.getId())) {
                            JSONObject deviceIdAndStatus = new JSONObject();
                            deviceIdAndStatus.put("deviceId", statusList.get(i).getInteger("device_id"));
                            deviceIdAndStatus.put("status", statusList.get(i).getInteger("status"));
                            if (statusList.get(i).getInteger("status") > status) {
                                status = statusList.get(i).getInteger("status");
                            }
                            device.add(deviceIdAndStatus);
                            statusList.remove(i);
                        }
                    }
                    jsonObject.put("status", status);
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

    @Transactional
    @Override
    public JSONObject chart(String startTime, String endTime, int projectId) {
        List<String> dateList = getBetweenDate(startTime.substring(0, 10), endTime.substring(0, 10));
        JSONObject result = new JSONObject();
        result.put("case", resultDetailService.findTopCases(startTime, endTime, projectId));
        result.put("device", resultDetailService.findTopDevices(startTime, endTime, projectId));
        List<JSONObject> rateList = resultsMapper.findDayPassRate(startTime, endTime, projectId);
        List<JSONObject> rateResult = new ArrayList<>();
        for (String date : dateList) {
            JSONObject d = new JSONObject();
            d.put("date", date);
            d.put("rate", 0);
            for (Iterator<JSONObject> ite = rateList.iterator(); ite.hasNext(); ) {
                JSONObject s = ite.next();
                if (s.getString("date").equals(date)) {
                    d.put("rate", s.getFloat("rate"));
                    ite.remove();
                    break;
                }
            }
            rateResult.add(d);
        }
        result.put("pass", rateResult);
        result.put("status", resultsMapper.findDayStatus(startTime, endTime, projectId));
        return result;
    }

    @Transactional
    @Override
    public void sendDayReport() {
        long timeMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Projects> projectsList = projectsService.findAll();
        for (Projects projects : projectsList) {
            Date yesterday = new Date(timeMillis - 86400000);
            Date today = new Date(timeMillis);
            List<JSONObject> status = resultsMapper.findDayStatus(sf.format(yesterday), sf.format(today), projects.getId());
            int suc = 0;
            int warn = 0;
            int fail = 0;
            for (JSONObject j : status) {
                switch (j.getInteger("status")) {
                    case 1:
                        suc += j.getInteger("total");
                        break;
                    case 2:
                        warn += j.getInteger("total");
                        break;
                    case 3:
                        fail += j.getInteger("total");
                        break;
                }
            }
            alertRobotsService.sendProjectReportMessage(projects.getId(), projects.getProjectName(), yesterday, today, false, suc, warn, fail);
        }
    }

    @Transactional
    @Override
    public void sendWeekReport() {
        long timeMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Projects> projectsList = projectsService.findAll();
        for (Projects projects : projectsList) {
            Date lastWeek = new Date(timeMillis - 86400000 * 7L);
            Date today = new Date(timeMillis);
            List<JSONObject> status = resultsMapper.findDayStatus(sf.format(lastWeek), sf.format(today), projects.getId());
            int count = resultsMapper.findRunCount(sf.format(lastWeek), sf.format(today), projects.getId());
            int suc = 0;
            int warn = 0;
            int fail = 0;
            for (JSONObject j : status) {
                switch (j.getInteger("status")) {
                    case 1:
                        suc += j.getInteger("total");
                        break;
                    case 2:
                        warn += j.getInteger("total");
                        break;
                    case 3:
                        fail += j.getInteger("total");
                        break;
                }
            }
            alertRobotsService.sendProjectReportMessage(projects.getId(), projects.getProjectName(), lastWeek, today, true, suc, warn, fail);
        }
    }

    public static List<String> getBetweenDate(String begin, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<String> betweenList = new ArrayList<String>();

        try {
            Calendar startDay = Calendar.getInstance();
            startDay.setTime(format.parse(begin));
            startDay.add(Calendar.DATE, -1);

            while (true) {
                startDay.add(Calendar.DATE, 1);
                Date newDate = startDay.getTime();
                String newend = format.format(newDate);
                betweenList.add(newend);
                if (end.equals(newend)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return betweenList;
    }

    @Transactional(rollbackFor = Exception.class)
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
            //发收相同的话，表明测试结束了
            if (Objects.equals(results.getReceiveMsgCount(), results.getSendMsgCount())) {
                results.setEndTime(new Date());
                save(results);
                alertRobotsService.sendResultFinishReport(results.getSuiteId(),
                        results.getSuiteName(), sucCount, warnCount, failCount, results.getProjectId(), results.getId());
            }
        }
    }

    @Override
    public void deleteByProjectId(int projectId) {
        baseMapper.delete(new LambdaQueryWrapper<Results>().eq(Results::getProjectId, projectId));
    }
}
