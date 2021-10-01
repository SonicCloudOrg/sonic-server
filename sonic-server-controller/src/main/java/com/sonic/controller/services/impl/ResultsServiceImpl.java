package com.sonic.controller.services.impl;

import com.sonic.controller.dao.ResultsRepository;
import com.sonic.controller.models.Projects;
import com.sonic.controller.models.ResultDetail;
import com.sonic.controller.models.Results;
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
            List<ResultDetail> resultDetailList = resultDetailService.findByResultIdAndType(id, "status");
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
            if (results.getSendMsgCount() <= 1 && sucCount == 0 && failCount == 0 && warnCount == 0) {
                delete(id);
            } else {
//                results.setReceiveAgentCount(results.getReceiveAgentCount() + 1);
//                save(results);
//                //发收相同的话，表明测试结束了
//                if (results.getReceiveAgentCount() == results.getSendAgentCount()) {
//                    Projects projects = projectsService.findById(results.getProjectId());
//                    if (projects != null && projects.getRobotToken().length() > 0 && projects.getRobotSecret().length() > 0) {
//                        robotMsgTool.sendResultFinishReport(projects.getRobotToken(), projects.getRobotSecret(),
//                                results.getSuiteName(), sucCount, warnCount, failCount, projects.getId(), results.getId());
//                    }
//                }
            }
        }
    }

    @Override
    public void subResultCount(int id) {
        Results results = findById(id);
        if (results != null) {
            results.setSendMsgCount(results.getSendMsgCount() - 1);
            resultsRepository.save(results);
        }
    }
}
