package com.sonic.task.dao;

import com.sonic.task.models.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Task数据库操作
 * @date 2021/8/22 11:29
 */
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    List<Tasks> findByProjectId(int projectId);
}
