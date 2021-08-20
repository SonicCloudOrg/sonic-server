package com.sonic.control.services;

import com.sonic.control.models.Steps;
import com.sonic.control.models.http.StepSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试步骤逻辑层
 * @date 2021/8/20 17:51
 */
public interface StepsService {
    List<Steps> findByCaseIdOrderBySort(int caseId);

    boolean resetCaseId(int id);

    boolean delete(int id);

    void save(Steps operations);

    Steps findById(int id);

    void sortSteps(StepSort stepSort);

    Page<Steps> findByProjectId(int projectId, Pageable pageable);
}
