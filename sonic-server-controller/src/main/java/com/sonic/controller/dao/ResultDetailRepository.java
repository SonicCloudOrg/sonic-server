package com.sonic.controller.dao;

import com.sonic.controller.models.ResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ResultDetailRepository extends JpaRepository<ResultDetail, Integer> {
    @Transactional
    void deleteByResultId(int resultId);
}
