package com.sonic.controller.dao;

import com.alibaba.fastjson.JSONObject;
import com.sonic.controller.models.Results;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ResultsRepository extends JpaRepository<Results, Integer> {
    Page<Results> findByProjectId(int projectId, Pageable pageable);

    List<Results> findByProjectId(int projectId);

    @Query(value = "select t2.date,round(IFNULL((t1.count/t2.count),0)*100,2) as rate from " +
            "(select DATE_FORMAT(end_time,'%Y-%m-%d') as date,count(status) as count from results where end_time>?1 and end_time<=?2 and project_id=?3 and status=1 " +
            "group by status,DATE_FORMAT(end_time,'%Y-%m-%d'))t1 " +
            "right join" +
            "(select DATE_FORMAT(end_time,'%Y-%m-%d') as date,count(status) as count from results where end_time>?1 and end_time<=?2 and project_id=?3 " +
            "group by DATE_FORMAT(end_time,'%Y-%m-%d'))t2 " +
            "on t1.date = t2.date", nativeQuery = true)
    List<JSONObject> findDayPassRate(String startTime, String endTime, int projectId);

    @Query(value = "select status,count(*) as total from results where end_time>?1 and end_time<=?2 and project_id=?3 group by status", nativeQuery = true)
    List<JSONObject> findDayStatus(String startTime, String endTime, int projectId);

    @Transactional
    void deleteByProjectId(int projectId);

    List<Results> findByCreateTimeBefore(Date time);
}
