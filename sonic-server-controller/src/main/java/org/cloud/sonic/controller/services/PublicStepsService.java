package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.PublicSteps;
import org.cloud.sonic.controller.models.dto.PublicStepsDTO;

import java.util.List;
import java.util.Map;

/**
 * @author ZhouYiXun
 * @des 公共步骤逻辑层
 * @date 2021/8/20 17:51
 */
public interface PublicStepsService extends IService<PublicSteps> {
    CommentPage<PublicStepsDTO> findByProjectId(int projectId, Page<PublicSteps> pageable);

    List<Map<String, Object>> findByProjectIdAndPlatform(int projectId, int platform);

    PublicStepsDTO savePublicSteps(PublicStepsDTO publicStepsDTO);

    boolean delete(int id);

    PublicStepsDTO findById(int id, boolean hiddenDisabled);

    boolean deleteByProjectId(int projectId);

    /**
     * 复制公共用例
     */
    void copyPublicSetpsIds(int id);
}
