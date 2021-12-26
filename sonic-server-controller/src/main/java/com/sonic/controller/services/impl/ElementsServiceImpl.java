package com.sonic.controller.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.mapper.ElementsMapper;
import com.sonic.controller.models.domain.Elements;
import com.sonic.controller.models.domain.Steps;
import com.sonic.controller.services.ElementsService;
import com.sonic.controller.services.StepsService;
import com.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ElementsServiceImpl extends SonicServiceImpl<ElementsMapper, Elements> implements ElementsService {

    @Autowired
    private ElementsMapper elementsMapper;
    @Autowired
    private StepsService stepsService;

    @Override
    public Page<Elements> findAll(int projectId, String type, List<String> eleTypes, String name, Page<Elements> pageable) {
        LambdaQueryChainWrapper<Elements> lambdaQuery = lambdaQuery();

        if (type != null && type.length() > 0) {
            switch (type) {
                case "normal" -> lambdaQuery.and(
                        l -> l.ne(Elements::getEleType, "point").ne(Elements::getEleType, "image")
                );
                case "point" -> lambdaQuery.eq(Elements::getEleType, "point");
                case "image" -> lambdaQuery.eq(Elements::getEleType, "image");
            }
        }

        if (eleTypes != null) {
            lambdaQuery.in(Elements::getEleType, eleTypes);
        }
        if (name != null && name.length() > 0) {
            lambdaQuery.like(Elements::getEleName, name);
        }

        lambdaQuery.eq(Elements::getProjectId, projectId);
        lambdaQuery.orderByDesc(Elements::getId);

        return lambdaQuery.page(pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RespModel<String> delete(int id) {
        if (existsById(id)) {
            try {
                baseMapper.deleteById(id);
                return new RespModel<>(RespEnum.DELETE_OK);
            } catch (Exception e) {


                List<Steps> stepsList = stepsService.listStepsByElementsId(id);
                StringBuilder sList = new StringBuilder();
                for (Steps s : stepsList) {
                    if (sList.length() == 0) {
                        sList.append(s.getId());
                    } else {
                        sList.append("，").append(s.getId());
                    }
                }
                return new RespModel<>(-2, "删除失败！控件元素已存在于步骤id：" + sList + "中！");
            }
        } else {
            return new RespModel<>(RespEnum.ID_NOT_FOUND);
        }
    }

    @Override
    public Elements findById(int id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean deleteByProjectId(int projectId) {
        return baseMapper.delete(new LambdaQueryWrapper<Elements>().eq(Elements::getProjectId, projectId)) > 0;
    }
}
