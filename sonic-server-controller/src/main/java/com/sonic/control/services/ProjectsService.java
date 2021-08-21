package com.sonic.control.services;

import com.alibaba.fastjson.JSONObject;
import com.sonic.control.models.Projects;

import java.util.List;

public interface ProjectsService {
    Projects findById(int id);
}
