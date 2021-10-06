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

    @Query(value = "select t2.device_id, t2.case_id, IFNULL(t1.status,4) as status " +
            "from (select device_id, status from result_detail where result_id=?1 and type = 'status')t1" +
            " right join" +
            "(select case_id, device_id from result_detail where result_id=?1 and type = 'step' group by device_id)t2" +
            " on t1.device_id = t2.device_id", nativeQuery = true)
    List<JSONObject> findStatusByResultIdGroupByCaseId(int resultId);

    @Transactional
    void deleteByResultId(int resultId);
}
