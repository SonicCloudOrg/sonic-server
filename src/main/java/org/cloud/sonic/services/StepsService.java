package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Steps;
import org.cloud.sonic.controller.models.dto.PublicStepsAndStepsIdDTO;
import org.cloud.sonic.controller.models.dto.StepsDTO;
import org.cloud.sonic.controller.models.http.StepSort;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 测试步骤逻辑层
 * @date 2021/8/20 17:51
 */
public interface StepsService extends IService<Steps> {
    List<StepsDTO> findByCaseIdOrderBySort(int caseId, boolean hiddenDisabled);

    List<StepsDTO> handleSteps(List<StepsDTO> stepsDTOS, boolean hiddenDisabled);

    /**
     * 获取每个step下的childSteps 组装成一个list返回
     *
     * @param stepsDTOS 步骤集合
     * @return 包含所有子步骤的集合
     */
    List<StepsDTO> getChildSteps(List<StepsDTO> stepsDTOS);

    /**
     * 如果步骤是条件步骤，且子条件也可能是条件步骤，则递归填充条件步骤的子步骤，且所有步骤都会填充
     *
     * @param stepsDTO 步骤对象（不需要填充）
     */
    StepsDTO handleStep(StepsDTO stepsDTO, boolean hiddenDisabled);

    boolean resetCaseId(int id);

    boolean delete(int id);

    void saveStep(StepsDTO operations);

    StepsDTO findById(int id);

    void sortSteps(StepSort stepSort);

    CommentPage<StepsDTO> findByProjectIdAndPlatform(int projectId, int platform, Page<Steps> pageable);

    List<Steps> listStepsByElementsId(int elementsId);

    boolean deleteByProjectId(int projectId);

    /**
     * 获取公共步骤里面的步骤
     */
    List<StepsDTO> listByPublicStepsId(int publicStepsId);

    /**
     * 公共步骤信息页，搜索步骤
     *
     * @param projectId
     * @param platform
     * @param page          页码
     * @param pageSize      页面大小
     * @param searchContent 搜索的文案；elements表中名字， steps表中的Content
     * @return 返回Steps表中步骤；
     */
    CommentPage<StepsDTO> searchFindByProjectIdAndPlatform(int projectId, int platform, int page, int pageSize,
                                                           String searchContent);

    Boolean copyStepsIdByCase(Integer stepId, boolean toLast);

    Boolean switchStep(int id, int type);

    List<PublicStepsAndStepsIdDTO> stepAndIndex(List<StepsDTO> needAllCopySteps);

    /**
     * 找到指定用例中最后一个步骤的sort
     *
     * @param castId 用例的id
     * @return 最后一个步骤的sort
     */
    Integer findMaxStepSort(int castId);

    /**
     * 找到指定用例的指定步骤，下一个step的sort值
     *
     * @param castId       目标用例
     * @param targetStepId 目标步骤id
     * @return 下一个step的sort值
     */
    Integer findNextStepSort(int castId, int targetStepId);

}
