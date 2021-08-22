package com.sonic.controller.dao;

import com.sonic.controller.models.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Elements数据库操作
 * @date 2021/8/16 20:29
 */
public interface ElementsRepository extends JpaRepository<Elements, Integer> {
    Page<Elements> findByProjectId(int projectId, Pageable pageable);

    List<Elements> findByProjectIdAndEleTypeNotAndEleTypeNot(int projectId, String eleType1, String eleType2);

    List<Elements> findByProjectIdAndEleType(int projectId, String eleType);

    @Transactional
    void deleteByProjectId(int projectId);
}
