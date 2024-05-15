package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.TestCaseElements;
import org.cloud.sonic.controller.models.dto.TestCaseElementsDTO;

import java.util.List;

/**
 * @author amy。wen
 * @des 测试步骤逻辑层
 * @date 2024/2/7 17:51
 */
public interface TestcaseElementsService extends IService<TestCaseElements> {
    List<TestCaseElementsDTO> findByCaseIdOrderBySort(int caseId, boolean hiddenDisabled);

    List<TestCaseElementsDTO> handleTestCaseElements(List<TestCaseElementsDTO> TestCaseElementsDTOS, boolean hiddenDisabled);

    /**
     * 获取每个step下的childTestCaseElements 组装成一个list返回
     *
     * @param TestCaseElementsDTOS 步骤集合
     * @return 包含所有子步骤的集合
     */
    List<TestCaseElementsDTO> getChildTestCaseElements(List<TestCaseElementsDTO> TestCaseElementsDTOS);

    /**
     * 如果步骤是条件步骤，且子条件也可能是条件步骤，则递归填充条件步骤的子步骤，且所有步骤都会填充
     *
     * @param TestCaseElementsDTO 步骤对象（不需要填充）
     */
    TestCaseElementsDTO handleStep(TestCaseElementsDTO TestCaseElementsDTO, boolean hiddenDisabled);

    boolean resetCaseId(int id);

    boolean delete(int id);

    void saveStep(TestCaseElementsDTO operations);

    TestCaseElementsDTO findById(int id);


    CommentPage<TestCaseElementsDTO> findByProjectIdAndPlatform(int projectId, int platform, Page<TestCaseElements> pageable);
    CommentPage<TestCaseElementsDTO> findByProjectIdAndPlatformById(int projectId, int platform, int testcaseId, Page<TestCaseElements> pageable);
    List<TestCaseElements> listTestCaseElementsByElementsId(int elementsId);

    boolean deleteByProjectId(int projectId);

    /**
     * 获取公共步骤里面的步骤
     */
    List<TestCaseElementsDTO> listByPublicTestCaseElementsId(int publicTestCaseElementsId);

    /**
     * 公共步骤信息页，搜索步骤
     *
     * @param projectId
     * @param platform
     * @param page          页码
     * @param pageSize      页面大小
     * @param searchContent 搜索的文案；elements表中名字， TestCaseElements表中的Content
     * @return 返回TestCaseElements表中步骤；
     */
    CommentPage<TestCaseElementsDTO> searchFindByProjectIdAndPlatform(int projectId, int platform, int page, int pageSize,
                                                           String searchContent);

    Boolean copyTestCaseElementsIdByCase(Integer stepId, boolean toLast);

    Boolean switchStep(int id, int type);


    /**
     * 找到指定用例中最后一个步骤的sort
     *
     * @param castId 用例的id
     * @return 最后一个步骤的sort
     */
    Integer findMaxTestCaseElementsort(int castId);

    /**
     * 找到指定用例的指定步骤，下一个step的sort值
     *
     * @param castId       目标用例
     * @param targetStepId 目标步骤id
     * @return 下一个step的sort值
     */
    Integer findNextTestCaseElementsort(int castId, int targetStepId);

}
