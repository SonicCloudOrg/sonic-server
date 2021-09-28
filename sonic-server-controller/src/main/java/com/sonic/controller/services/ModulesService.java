package com.sonic.controller.services;

import com.sonic.controller.models.Modules;

import java.util.List;

public interface ModulesService {
    void save(Modules modules);

    boolean delete(int id);

    List<Modules> findByProjectId(int projectId);

    Modules findById(int id);
}
