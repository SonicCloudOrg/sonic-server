package com.sonic.task.dao;

import com.sonic.task.models.Jobs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Job数据库操作
 * @date 2021/8/22 11:29
 */
public interface JobsRepository extends JpaRepository<Jobs, Integer> {
    Page<Jobs> findByProjectId(int projectId, Pageable pageable);
}
