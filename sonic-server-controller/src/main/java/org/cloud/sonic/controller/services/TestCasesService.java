package org.cloud.sonic.controller.services;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.TestCases;
import org.cloud.sonic.controller.models.dto.TestCasesDTO;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试用例逻辑层
 * @date 2021/8/20 17:51
 */
public interface TestCasesService extends IService<TestCases> {
    CommentPage<TestCasesDTO> findAll(int projectId, int platform, String name, List<Integer> moduleIds,
                                      List<String> caseAuthorNames, Page<TestCases> pageable,
                                      String idSort, String editTimeSort);

    List<TestCases> findAll(int projectId, int platform);

    boolean delete(int id);

    TestCasesDTO findById(int id);

    JSONObject findSteps(int id);

    List<TestCases> findByIdIn(List<Integer> ids);

    boolean deleteByProjectId(int projectId);

    List<TestCases> listByPublicStepsId(int publicStepsId);

    /**
     * 复制测试用例
     *
     * @param id 测试用例id  （test_cases，步骤表 steps case_id字段）
     * @return
     */
    boolean copyTestById(int id);

    Boolean updateTestCaseModuleByModuleId(Integer module);

    /**
     * 查询指定项目，指定平台下，所有的用例作者列表集合
     *
     * @param projectId 项目id
     * @param platform  平台
     * @return 用例作者列表集合
     */
    List<String> findAllCaseAuthor(int projectId, int platform);
}
