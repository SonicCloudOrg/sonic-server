package com.sonic.controller.dao;

import com.sonic.controller.models.Versions;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Versions数据库操作
 * @date 2021/8/20 20:29
 */
public interface VersionsRepository extends JpaRepository<Versions, Integer> {
    List<Versions> findByProjectId(int projectId, Sort s);

    @Transactional
    void deleteByProjectId(int projectId);
}
