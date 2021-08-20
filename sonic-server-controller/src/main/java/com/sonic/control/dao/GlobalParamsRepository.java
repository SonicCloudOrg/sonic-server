package com.sonic.control.dao;

import com.sonic.control.models.GlobalParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des GlobalParams数据库操作
 * @date 2021/8/20 20:29
 */
public interface GlobalParamsRepository extends JpaRepository<GlobalParams, Integer> {
    List<GlobalParams> findByProjectId(int projectId);

    @Transactional
    void deleteByProjectId(int projectId);
}
