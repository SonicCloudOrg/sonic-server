package com.sonic.controller.dao;

import com.sonic.controller.models.CrashDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author ZhouYiXun
 * @des CrashDetail数据库操作
 * @date 2021/8/16 20:29
 */
public interface CrashDetailRepository extends JpaRepository<CrashDetail, Integer> {
    @Query(value = "select count(*) from (select count(*) from crash_detail where bug_id=?1 group by device_id) a", nativeQuery = true)
    int findAffectNum(int bugId);

    List<CrashDetail> findByBugIdOrderByAppearTimeDesc(int bugId);

    List<CrashDetail> findByResultIdAndCaseIdAndDeviceId(int resultId, int caseId, int deviceId);

    List<CrashDetail> findByBugIdInAndAppearTimeBetween(List<Integer> bugIds, Date time1, Date time2);
}
