package com.sonic.controller.dao;

import com.sonic.controller.models.TestSuites;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des TestSuites数据库操作
 * @date 2021/8/20 15:29
 */
public interface TestSuitesRepository extends JpaRepository<TestSuites, Integer> {
    List<TestSuites> findByProjectId(int projectId, Sort sort);

    @Transactional
    void deleteByProjectId(@Param("projectId") int projectId);
}
