package com.sonic.controller.services;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sonic.controller.models.domain.GlobalParams;

import java.util.List;

public interface GlobalParamsService extends IService<GlobalParams> {
    List<GlobalParams> findAll(int projectId);

    boolean delete(int id);

    GlobalParams findById(int id);

    boolean deleteByProjectId(int projectId);


}
