package com.sonic.controller.dao;

import com.sonic.controller.models.Steps;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Steps数据库操作
 * @date 2021/8/16 20:29
 */
public interface StepsRepository extends JpaRepository<Steps, Integer> {
    List<Steps> findByCaseIdOrderBySort(int caseId);

    @Query(value = "select IFNULL(max(sort),0) from steps", nativeQuery = true)
    int findMaxSort();

    List<Steps> findByCaseIdAndSortLessThanEqualAndSortGreaterThanEqualOrderBySort(int caseId, int startSort, int endSort);

    @Transactional
    void deleteByCaseId(int caseId);

    @Transactional
    void deleteByProjectId(@Param("projectId") int projectId);

    Page<Steps> findByProjectId(int projectId, Pageable pageable);
}
