package com.sonic.control.services.impl;

import com.sonic.control.dao.ResultsRepository;
import com.sonic.control.models.Projects;
import com.sonic.control.models.ResultDetail;
import com.sonic.control.models.Results;
import com.sonic.control.models.interfaces.ResultDetailStatus;
import com.sonic.control.models.interfaces.ResultStatus;
import com.sonic.control.services.*;
import com.sonic.control.tools.DingTalkMsgTool;
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
    private DingTalkMsgTool dingTalkMsgTool;

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
                results.setStatus(ResultStatus.FAIL);
            } else if (warnCount > 0) {
                results.setStatus(ResultStatus.WARNING);
            } else {
                results.setStatus(ResultStatus.PASS);
            }
            if (sucCount == 0 && failCount == 0 && warnCount == 0) {
                delete(id);
            } else {
                save(results);
                Projects projects = projectsService.findById(results.getProjectId());
                if (projects != null && projects.getDingTalkToken().length() > 0 && projects.getDingTalkSecret().length() > 0) {
                    dingTalkMsgTool.sendResultFinishReport(projects.getDingTalkToken(), projects.getDingTalkSecret(),
                            results.getSuiteName(), sucCount, warnCount, failCount, projects.getId(), results.getId());
                }
            }
        }
    }
}
