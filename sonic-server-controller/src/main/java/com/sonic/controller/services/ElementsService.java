package com.sonic.controller.services;

import com.sonic.common.http.RespModel;
import com.sonic.controller.models.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ElementsService {
    Page<Elements> findAll(int projectId, String type, Pageable pageable);

    RespModel delete(int id);

    boolean save(Elements elements);

    Elements findById(int id);
}
