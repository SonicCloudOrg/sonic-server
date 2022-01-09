package com.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.domain.Elements;
import com.sonic.controller.models.domain.Steps;
import com.sonic.controller.models.dto.StepsDTO;

import java.util.List;

public interface ElementsService extends IService<Elements> {
    Page<Elements> findAll(int projectId, String type, List<String> eleTypes, String name, Page<Elements> pageable);

    List<StepsDTO> findAllStepsByElementsId(int elementsId);

    RespModel<String> delete(int id);

    Elements findById(int id);

    boolean deleteByProjectId(int projectId);
}
