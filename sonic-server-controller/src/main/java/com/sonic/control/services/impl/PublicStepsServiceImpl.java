package com.sonic.control.services.impl;

import com.sonic.control.dao.PublicStepsRepository;
import com.sonic.control.models.PublicSteps;
import com.sonic.control.services.PublicStepsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ZhouYiXun
 * @des 公共步骤逻辑实现
 * @date 2021/8/20 17:51
 */
@Service
public class PublicStepsServiceImpl implements PublicStepsService {
    @Autowired
    private PublicStepsRepository publicStepsRepository;

    @Override
    public Page<PublicSteps> findByProjectId(int projectId, Pageable pageable) {
        return publicStepsRepository.findByProjectId(projectId, pageable);
    }

    @Override
    public List<Map<Integer, String>> findByProjectId(int projectId) {
        return publicStepsRepository.findByProjectId(projectId);
    }

    @Override
    @CachePut(value = "sonic:publicSteps", key = "#publicOper.id", unless = "#result == null")
    public PublicSteps save(PublicSteps publicSteps) {
        publicStepsRepository.save(publicSteps);
        return publicSteps;
    }

    @Override
    @CacheEvict(value = "sonic:publicSteps", key = "#id")
    public boolean delete(int id) {
        if (publicStepsRepository.existsById(id)) {
            publicStepsRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Cacheable(value = "sonic:publicSteps", key = "#id", unless = "#result == null")
    public PublicSteps findById(int id) {
        if (publicStepsRepository.existsById(id)) {
            return publicStepsRepository.findById(id).get();
        } else {
            return null;
        }
    }
}
