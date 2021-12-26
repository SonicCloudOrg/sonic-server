package com.sonic.controller.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sonic.controller.mapper.VersionsMapper;
import com.sonic.controller.models.domain.Versions;
import com.sonic.controller.services.VersionsService;
import com.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 迭代逻辑层实现
 * @date 2021/8/16 22:56
 */
@Service
public class VersionsServiceImpl extends SonicServiceImpl<VersionsMapper, Versions> implements VersionsService {

    @Autowired
    private VersionsMapper versionsMapper;

    @Override
    public boolean delete(int id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public List<Versions> findByProjectId(int projectId) {
        return lambdaQuery().eq(Versions::getProjectId, projectId)
                .orderByDesc(Versions::getCreateTime)
                .list();
    }

    @Override
    public Versions findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void deleteByProjectId(int projectId) {
        baseMapper.delete(new LambdaQueryWrapper<Versions>().eq(Versions::getProjectId, projectId));
    }
}
