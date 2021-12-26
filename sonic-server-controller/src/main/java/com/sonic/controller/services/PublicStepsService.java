package com.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sonic.controller.models.base.CommentPage;
import com.sonic.controller.models.domain.PublicSteps;
import com.sonic.controller.models.dto.PublicStepsDTO;

import java.util.List;
import java.util.Map;

/**
 * @author ZhouYiXun
 * @des 公共步骤逻辑层
 * @date 2021/8/20 17:51
 */
public interface PublicStepsService extends IService<PublicSteps> {
    CommentPage<PublicStepsDTO> findByProjectId(int projectId, Page<PublicSteps> pageable);

    List<Map<Integer, String>> findByProjectIdAndPlatform(int projectId, int platform);

    PublicStepsDTO savePublicSteps(PublicStepsDTO publicStepsDTO);

    boolean delete(int id);

    PublicStepsDTO findById(int id);

    boolean deleteByProjectId(int projectId);
}
