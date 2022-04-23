package org.cloud.sonic.controller.services;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.domain.TestCases;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试用例逻辑层
 * @date 2021/8/20 17:51
 */
public interface TestCasesService extends IService<TestCases> {
    Page<TestCases> findAll(int projectId, int platform, String name, Page<TestCases> pageable);

    List<TestCases> findAll(int projectId, int platform);

    boolean delete(int id);

    TestCases findById(int id);

    JSONObject findSteps(int id);

    List<TestCases> findByIdIn(List<Integer> ids);

    boolean deleteByProjectId(int projectId);

    List<TestCases> listByPublicStepsId(int publicStepsId);
}
