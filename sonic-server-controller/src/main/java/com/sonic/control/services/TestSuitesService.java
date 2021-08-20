package com.sonic.control.services;


import com.sonic.common.http.RespModel;
import com.sonic.control.models.TestSuites;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试套件逻辑层
 * @date 2021/8/20 17:51
 */
public interface TestSuitesService {
    RespModel runSuite(int id, String strike);

    TestSuites findById(int id);

    boolean delete(int id);

    void save(TestSuites testSuites);

    List<TestSuites> findByProjectId(int projectId);
}
