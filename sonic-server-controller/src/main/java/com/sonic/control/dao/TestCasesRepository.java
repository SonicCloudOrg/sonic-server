package com.sonic.control.dao;

import com.sonic.control.models.TestCases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ZhouYiXun
 * @des TestCases数据库操作
 * @date 2021/8/20 15:29
 */
public interface TestCasesRepository extends JpaRepository<TestCases, Integer>, JpaSpecificationExecutor<TestCases> {

    @Transactional
    void deleteByProjectId(int projectId);
}
