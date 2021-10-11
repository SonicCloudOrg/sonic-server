package com.sonic.controller.dao;

import com.sonic.controller.models.TestCases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des TestCases数据库操作
 * @date 2021/8/20 15:29
 */
public interface TestCasesRepository extends JpaRepository<TestCases, Integer>, JpaSpecificationExecutor<TestCases> {
    List<TestCases> findByProjectIdAndPlatformOrderByEditTimeDesc(int projectId, int platform);

    List<TestCases> findByIdIn(List<Integer> ids);

    @Transactional
    void deleteByProjectId(int projectId);
}
