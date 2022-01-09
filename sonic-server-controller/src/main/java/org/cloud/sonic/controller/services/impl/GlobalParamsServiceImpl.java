package org.cloud.sonic.controller.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.cloud.sonic.controller.mapper.GlobalParamsMapper;
import org.cloud.sonic.controller.models.domain.GlobalParams;
import org.cloud.sonic.controller.services.GlobalParamsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/10/9 23:28
 */
@Service
public class GlobalParamsServiceImpl extends SonicServiceImpl<GlobalParamsMapper, GlobalParams> implements GlobalParamsService {

    @Autowired
    private GlobalParamsMapper globalParamsMapper;

    @Override
    public List<GlobalParams> findAll(int projectId) {
        return lambdaQuery().eq(GlobalParams::getProjectId, projectId).list();
    }

    @Override
    public boolean delete(int id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public GlobalParams findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean deleteByProjectId(int projectId) {
        return baseMapper.delete(new LambdaQueryWrapper<GlobalParams>().eq(GlobalParams::getProjectId, projectId)) > 0;
    }
}
