package com.sonic.control.services.impl;

import com.sonic.control.dao.StepsRepository;
import com.sonic.control.models.PublicSteps;
import com.sonic.control.models.Steps;
import com.sonic.control.models.http.StepSort;
import com.sonic.control.services.PublicStepsService;
import com.sonic.control.services.StepsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试步骤实现
 * @date 2021/8/20 17:51
 */
@Service
public class StepsServiceImpl implements StepsService {
    @Autowired
    private StepsRepository stepsRepository;
    @Autowired
    private PublicStepsService publicStepsService;
    @Autowired
    private CacheManager cacheManager;

    @Override
    public List<Steps> findByCaseIdOrderBySort(int caseId) {
        List<Steps> list = stepsRepository.findByCaseIdAndStepTypeNotOrderBySort(caseId, "monkey");
        Steps monkey = stepsRepository.findTopByCaseIdAndStepType(caseId, "monkey");
        if (monkey != null) {
            list.add(monkey);
        }
        return list;
    }

    @Override
    public boolean resetCaseId(int id) {
        if (stepsRepository.existsById(id)) {
            Steps steps = stepsRepository.findById(id).get();
            steps.setCaseId(0);
            stepsRepository.save(steps);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        if (stepsRepository.existsById(id)) {
            //清理缓存
            Steps steps = stepsRepository.findById(id).get();
            List<PublicSteps> publicSteps = steps.getPublicSteps();
            Cache cachePublic = cacheManager.getCache("sonic:publicSteps");
            for (PublicSteps publicStep : publicSteps) {
                publicStep.getSteps().remove(steps);
                cachePublic.evict(publicStep.getId());
            }
            stepsRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void save(Steps steps) {
        if (steps.getStepType().equals("publicStep")) {
            PublicSteps publicSteps = publicStepsService.findById(Integer.parseInt(steps.getText()));
            if (publicSteps != null) {
                steps.setContent(publicSteps.getName());
            } else {
                steps.setContent("未知");
            }
        }
        if (!stepsRepository.existsById(steps.getId())) {
            steps.setSort(stepsRepository.findMaxSort() + 1);
        } else {
            //清理缓存，因为发生了变动，需要清理已有缓存
            Steps s = stepsRepository.findById(steps.getId()).get();
            Cache cachePublic = cacheManager.getCache("sonic:publicStep");
            List<PublicSteps> publicSteps = s.getPublicSteps();
            for (PublicSteps p : publicSteps) {
                cachePublic.evict(p.getId());
            }
        }
        stepsRepository.save(steps);
    }

    @Override
    public Steps findById(int id) {
        if (stepsRepository.existsById(id)) {
            return stepsRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public void sortSteps(StepSort stepSort) {
        List<Steps> stepsList =
                stepsRepository.findByCaseIdAndSortLessThanEqualAndSortGreaterThanEqualOrderBySort(
                        stepSort.getCaseId(), stepSort.getStartId(), stepSort.getEndId());
        if (stepSort.getDirection().equals("down")) {
            for (int i = 0; i < stepsList.size() - 1; i++) {
                int temp = stepsList.get(stepsList.size() - 1).getSort();
                stepsList.get(stepsList.size() - 1).setSort(stepsList.get(i).getSort());
                stepsList.get(i).setSort(temp);
            }
        } else {
            for (int i = 0; i < stepsList.size() - 1; i++) {
                int temp = stepsList.get(0).getSort();
                stepsList.get(0).setSort(stepsList.get(stepsList.size() - 1 - i).getSort());
                stepsList.get(stepsList.size() - 1 - i).setSort(temp);
            }
        }
        stepsRepository.saveAll(stepsList);
    }

    @Override
    public Page<Steps> findByProjectId(int projectId, Pageable pageable) {
        return stepsRepository.findByProjectIdAndStepTypeNot(projectId
                , "monkey", pageable);
    }
}
