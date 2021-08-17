package com.sonic.control.services.impl;

import com.sonic.control.dao.VersionsRepository;
import com.sonic.control.models.Versions;
import com.sonic.control.services.VersionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des 迭代逻辑层实现
 * @date 2021/8/16 22:56
 */
@Service
public class VersionsServiceImpl implements VersionsService {
    @Autowired
    private VersionsRepository versionsRepository;

    @Override
    public void save(Versions versions) {
        versionsRepository.save(versions);
    }

    @Override
    public boolean delete(int id) {
        if (versionsRepository.existsById(id)) {
            versionsRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Versions> findByProjectId(int projectId) {
        return versionsRepository.findByProjectId(projectId);
    }

    @Override
    public Versions findById(int id) {
        if (versionsRepository.existsById(id)) {
            return versionsRepository.findById(id).get();
        } else {
            return null;
        }
    }
}
