package com.sonic.controller.dao;

import com.sonic.controller.models.ResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

public interface ResultDetailRepository extends JpaRepository<ResultDetail, Integer>, JpaSpecificationExecutor<ResultDetail> {
    @Transactional
    void deleteByResultId(int resultId);
}
