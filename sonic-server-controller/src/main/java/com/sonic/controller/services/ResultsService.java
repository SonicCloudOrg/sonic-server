package com.sonic.controller.services;

import com.sonic.controller.models.Results;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ZhouYiXun
 * @des 测试结果逻辑层
 * @date 2021/8/21 16:08
 */
public interface ResultsService {
    Page<Results> findByProjectId(int projectId, Pageable pageable);

    boolean delete(int id);

    Results findById(int id);

    void save(Results results);

    void clean(int day);

    void suiteResult(int id);

    void subResultCount(int id);
}
