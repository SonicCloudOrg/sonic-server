package com.sonic.controller.dao;

import com.sonic.controller.models.ElapsedTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

public interface ElapsedTimeRepository extends JpaRepository<ElapsedTime, Integer>, JpaSpecificationExecutor<ElapsedTime> {
    ElapsedTime findByProjectIdAndTimeAndPlatformAndPackageVersionAndManufacturerAndSystemVersion(
            int projectId, Date time, int platform, String packageVersion, String manufacturer, String systemVersion
    );
}
