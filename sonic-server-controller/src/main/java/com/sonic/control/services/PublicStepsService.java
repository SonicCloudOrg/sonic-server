package com.sonic.control.services;

import com.sonic.control.models.PublicSteps;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author ZhouYiXun
 * @des 公共步骤逻辑层
 * @date 2021/8/20 17:51
 */
public interface PublicStepsService {
    Page<PublicSteps> findByProjectId(int projectId, Pageable pageable);

    List<Map<Integer, String>> findByProjectId(int projectId);

    PublicSteps save(PublicSteps publicSteps);

    boolean delete(int id);

    PublicSteps findById(int id);
}
