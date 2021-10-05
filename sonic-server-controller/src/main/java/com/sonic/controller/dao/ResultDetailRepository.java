package com.sonic.controller.dao;

import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.models.ResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResultDetailRepository extends JpaRepository<ResultDetail, Integer>, JpaSpecificationExecutor<ResultDetail> {
    @Query(value = "select case_id, max(time) as endTime,min(time) as startTime from result_detail where result_id=?1 and type = 'step' group by case_id", nativeQuery = true)
    List<JSONObject> findTimeByResultIdGroupByCaseId(int resultId);

    @Transactional
    void deleteByResultId(int resultId);
}
