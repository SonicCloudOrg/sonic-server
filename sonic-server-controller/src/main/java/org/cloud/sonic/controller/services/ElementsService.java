package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Elements;
import org.cloud.sonic.controller.models.domain.Steps;
import org.cloud.sonic.controller.models.dto.ElementsDTO;
import org.cloud.sonic.controller.models.dto.StepsDTO;

import java.util.List;

public interface ElementsService extends IService<Elements> {
    CommentPage<ElementsDTO> findAll(int projectId, String type, List<String> eleTypes, String name, String value, List<Integer> moduleIds, Page<Elements> pageable);

    List<StepsDTO> findAllStepsByElementsId(int elementsId);

    RespModel<String> delete(int id);

    Elements findById(int id);

    boolean deleteByProjectId(int projectId);


    /**
     * 复制元素 按照元素id
     *
     * @param id 元素id
     * @return
     */
    RespModel<String> copy(int id);

    /**
     * 关联stepsDto下面的元素
     *
     * @param stepsDTO
     * @return
     */
    Boolean newStepBeLinkedEle(StepsDTO stepsDTO, Steps step);

    Boolean updateEleModuleByModuleId(Integer module);
}
