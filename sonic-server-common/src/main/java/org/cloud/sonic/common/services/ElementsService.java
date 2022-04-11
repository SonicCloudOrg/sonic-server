package org.cloud.sonic.common.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.models.domain.Elements;
import org.cloud.sonic.common.models.dto.StepsDTO;

import java.util.List;

public interface ElementsService extends IService<Elements> {
    Page<Elements> findAll(int projectId, String type, List<String> eleTypes, String name, Page<Elements> pageable);

    List<StepsDTO> findAllStepsByElementsId(int elementsId);

    RespModel<String> delete(int id);

    Elements findById(int id);

    boolean deleteByProjectId(int projectId);
}
