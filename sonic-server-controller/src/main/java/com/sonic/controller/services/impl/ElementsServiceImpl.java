package com.sonic.controller.services.impl;

import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.dao.ElementsRepository;
import com.sonic.controller.dao.StepsRepository;
import com.sonic.controller.models.Elements;
import com.sonic.controller.models.PublicSteps;
import com.sonic.controller.models.Steps;
import com.sonic.controller.services.ElementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElementsServiceImpl implements ElementsService {
    @Autowired
    private ElementsRepository elementsRepository;
    @Autowired
    private StepsRepository stepsRepository;
    @Autowired
    private CacheManager cacheManager;

    @Override
    public boolean save(Elements elements) {
        try {
            if (elementsRepository.existsById(elements.getId())) {
                //清理缓存
                Elements full = elementsRepository.findById(elements.getId()).get();
                Cache cachePublic = cacheManager.getCache("sonic:publicSteps");
                List<Steps> stepsList = full.getSteps();
                for (Steps steps : stepsList) {
                    List<PublicSteps> publicStepsList = steps.getPublicSteps();
                    for (PublicSteps publicSteps : publicStepsList) {
                        cachePublic.evict(publicSteps.getId());
                    }
                }
            }
            elementsRepository.save(elements);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Page<Elements> findAll(int projectId, String type, Pageable pageable) {
        Specification<Elements> spc = (root, query, cb) -> {
            List<Order> orders = new ArrayList<>();
            orders.add(cb.desc(root.get("id")));
            query.orderBy(orders);
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(cb.and(cb.equal(root.get("projectId"), projectId)));
            if (type != null && type.length() > 0) {
                switch (type) {
                    case "normal":
                        predicateList.add(cb.and(cb.notEqual(root.get("eleType"), "point")));
                        predicateList.add(cb.and(cb.notEqual(root.get("eleType"), "image")));
                        break;
                    case "point":
                        predicateList.add(cb.and(cb.equal(root.get("eleType"), "point")));
                        break;
                    case "image":
                        predicateList.add(cb.and(cb.equal(root.get("eleType"), "image")));
                        break;
                }
            }
            Predicate[] p = new Predicate[predicateList.size()];
            return query.where(predicateList.toArray(p)).getRestriction();
        };
        return elementsRepository.findAll(spc, pageable);
    }

    @Override
    public RespModel delete(int id) {
        if (elementsRepository.existsById(id)) {
            try {
                elementsRepository.deleteById(id);
                return new RespModel(RespEnum.DELETE_OK);
            } catch (Exception e) {
                List<Steps> stepsList = elementsRepository.findById(id).get().getSteps();
                String sList = "";
                for (Steps s : stepsList) {
                    if (sList.length() == 0) {
                        sList += s.getId();
                    } else {
                        sList += "，" + s.getId();
                    }
                }
                return new RespModel(-2, "删除失败！控件元素已存在于步骤id：" + sList + "中！");
            }
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }

    @Override
    public Elements findById(int id) {
        if (elementsRepository.existsById(id)) {
            return elementsRepository.findById(id).get();
        } else {
            return null;
        }
    }
}
