package com.sonic.control.Models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(indexes = {
        @Index(name = "IDX_PROJECT_ID_TIME_PLATFORM_PACKAGE_VERSION", columnList = "projectId"),
        @Index(name = "IDX_PROJECT_ID_TIME_PLATFORM_PACKAGE_VERSION", columnList = "time"),
        @Index(name = "IDX_PROJECT_ID_TIME_PLATFORM_PACKAGE_VERSION", columnList = "platform"),
        @Index(name = "IDX_PROJECT_ID_TIME_PLATFORM_PACKAGE_VERSION", columnList = "packageVersion"),
})
public class ElapsedTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int projectId; //项目id
    String manufacturer; //制造商
    String systemVersion; //系统版本
    int platform; //平台
    String packageVersion; //安装包版本
    int runTime; //运行时长
    @Temporal(TemporalType.DATE)
    Date time; //日期

    public ElapsedTime() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ElapsedTime{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", manufacturer='" + manufacturer + '\'' +
                ", systemVersion='" + systemVersion + '\'' +
                ", platform=" + platform +
                ", packageVersion='" + packageVersion + '\'' +
                ", runTime=" + runTime +
                ", time=" + time +
                '}';
    }
}
