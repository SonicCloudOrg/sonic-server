package com.sonic.controller.dao;

import com.sonic.controller.models.Results;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ResultsRepository extends JpaRepository<Results, Integer> {
    Page<Results> findByProjectId(int projectId, Pageable pageable);

    List<Results> findByProjectId(int projectId);

    @Transactional
    void deleteByProjectId(int projectId);

    List<Results> findByCreateTimeBefore(Date time);
}
