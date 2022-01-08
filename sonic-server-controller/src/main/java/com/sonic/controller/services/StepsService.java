package com.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sonic.controller.models.base.CommentPage;
import com.sonic.controller.models.domain.Steps;
import com.sonic.controller.models.dto.StepsDTO;
import com.sonic.controller.models.http.StepSort;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试步骤逻辑层
 * @date 2021/8/20 17:51
 */
public interface StepsService extends IService<Steps> {
    List<StepsDTO> findByCaseIdOrderBySort(int caseId);

    boolean resetCaseId(int id);

    boolean delete(int id);

    void saveStep(StepsDTO operations);

    StepsDTO findById(int id);

    void sortSteps(StepSort stepSort);

    CommentPage<StepsDTO> findByProjectIdAndPlatform(int projectId, int platform, Page<Steps> pageable);

    List<Steps> listStepsByElementsId(int elementsId);

    boolean deleteByProjectId(int projectId);

    /**
     * 获取公共步骤里面的步骤
     */
    List<StepsDTO> listByPublicStepsId(int publicStepsId);
}
