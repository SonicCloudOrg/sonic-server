package com.sonic.controller.dao;

import com.sonic.controller.models.PublicSteps;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author ZhouYiXun
 * @des PublicSteps数据库操作
 * @date 2021/8/16 20:29
 */
public interface PublicStepsRepository extends JpaRepository<PublicSteps, Integer> {
    Page<PublicSteps> findByProjectId(int projectId, Pageable pageable);

    @Query(value = "select id,name from public_steps where project_id=?1", nativeQuery = true)
    List<Map<Integer, String>> findByProjectId(int projectId);

    @Transactional
    void deleteByProjectId(int projectId);
}
