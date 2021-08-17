package com.sonic.control.dao;

import com.sonic.control.models.Modules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author ZhouYiXun
 * @des Modules数据库操作
 * @date 2021/8/16 20:29
 */
public interface ModulesRepository extends JpaRepository<Modules, Integer> {
    List<Modules> findByProjectId(int projectId);

    @Transactional
    void deleteByProjectId(int projectId);
}
