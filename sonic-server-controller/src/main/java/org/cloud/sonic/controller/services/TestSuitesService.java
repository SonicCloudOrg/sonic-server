package org.cloud.sonic.controller.services;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.TestSuites;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import org.cloud.sonic.controller.models.dto.TestSuitesDTO;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试套件逻辑层
 * @date 2021/8/20 17:51
 */
public interface TestSuitesService extends IService<TestSuites> {
    RespModel<Integer> runSuite(int id, String strike);

    RespModel<String> forceStopSuite(int id, String strike);

    TestSuitesDTO findById(int id);

    JSONObject getStep(StepsDTO steps);

    boolean delete(int id);

    void saveTestSuites(TestSuitesDTO testSuitesDTO);

    CommentPage<TestSuitesDTO> findByProjectId(int projectId, String name, Page<TestSuites> pageable);

    List<TestSuitesDTO> findByProjectId(int projectId);

    boolean deleteByProjectId(int projectId);

    List<TestSuites> listTestSuitesByTestCasesId(int testCasesId);
}
