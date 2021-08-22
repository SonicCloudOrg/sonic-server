package com.sonic.controller.services;

import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.models.TestCases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author ZhouYiXun
 * @des 测试用例逻辑层
 * @date 2021/8/20 17:51
 */
public interface TestCasesService {
    Page<TestCases> findAll(int projectId, int platform, String name, Pageable pageable);

    boolean delete(int id);

    void save(TestCases testCases);

    TestCases findById(int id);

    JSONObject findSteps(int id);
}
