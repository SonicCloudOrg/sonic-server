package com.sonic.control.dao;

import com.sonic.control.models.Bugs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author ZhouYiXun
 * @des Bugs数据库操作
 * @date 2021/8/16 20:30
 */
public interface BugsRepository extends JpaRepository<Bugs, Integer>, JpaSpecificationExecutor<Bugs> {
    Bugs findByProjectIdAndTypeAndPlatformAndVersionAndErrorTypeAndErrorStack(
            int projectId, int type, int platform, String version, String errorType, String errorStack);

    List<Bugs> findByProjectIdAndTypeAndLastTimeBetween(int projectId, int type, Date yesterday, Date today);

    List<Bugs> findByProjectIdAndTypeAndStatus(int projectId, int type, int status);

    @Query(value = "select version from bugs where project_id=?1 and platform=?2 group by version", nativeQuery = true)
    List<String> findVersion(int projectId, int platform);

    List<Bugs> findByProjectIdAndTypeAndPlatformAndStatus(int projectId, int type, int platform, int status);

    List<Bugs> findByProjectIdAndTypeAndPlatformAndLastTimeBetween(int projectId, int type, int platform, Date yesterday, Date today);
}
