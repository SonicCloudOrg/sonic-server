package org.cloud.sonic.controller.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.cloud.sonic.controller.mapper.ModulesMapper;
import org.cloud.sonic.controller.models.domain.Modules;
import org.cloud.sonic.controller.services.ModulesService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModulesServiceImpl extends SonicServiceImpl<ModulesMapper, Modules> implements ModulesService {

    @Autowired
    private ModulesMapper modulesMapper;

    @Override
    public boolean delete(int id) {
        return modulesMapper.deleteById(id) > 0;
    }

    @Override
    public List<Modules> findByProjectId(int projectId) {
        return lambdaQuery().eq(Modules::getProjectId, projectId).list();
    }

    @Override
    public Modules findById(int id) {
        return modulesMapper.selectById(id);
    }

    @Override
    public boolean deleteByProjectId(int projectId) {
        return baseMapper.delete(new LambdaQueryWrapper<Modules>().eq(Modules::getProjectId, projectId)) > 0;
    }
}
