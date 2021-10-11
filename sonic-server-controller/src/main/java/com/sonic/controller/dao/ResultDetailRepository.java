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
            "from (select case_id, device_id, status from result_detail where result_id=?1 and type = 'status')t1" +
            " right join" +
            "(select case_id, device_id from result_detail where result_id=?1 and type = 'step' group by case_id, device_id)t2" +
            " on t1.device_id = t2.device_id and t1.case_id = t2.case_id", nativeQuery = true)
    List<JSONObject> findStatusByResultIdGroupByCaseId(int resultId);

    @Query(value = "select t.case_id,sum(t.diff) as total from (select case_id,result_id,TIMESTAMPDIFF(SECOND,min(time),max(time)) as diff " +
            "from result_detail where result_id in " +
            "(select id from results where end_time>?1 and end_time<=?2 and project_id=?3) " +
            "and type='step' group by result_id,case_id)t group by t.case_id order by total desc limit 5", nativeQuery = true)
    List<JSONObject> findTopCases(String startTime, String endTime, int projectId);

    @Query(value = "select t.device_id,sum(t.diff) as total from (select device_id,case_id,result_id,TIMESTAMPDIFF(SECOND,min(time),max(time)) as diff " +
            "from result_detail where result_id in " +
            "(select id from results where end_time>?1 and end_time<=?2 and project_id=?3) " +
            "and type='step' group by result_id,case_id,device_id)t group by t.device_id order by total desc limit 5", nativeQuery = true)
    List<JSONObject> findTopDevices(String startTime, String endTime, int projectId);

    @Transactional
    void deleteByResultId(int resultId);
}
