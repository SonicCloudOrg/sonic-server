package com.sonic.control.models.http;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@ApiModel("更新测试包请求模型")
public class PackageUpdate {
    @Positive
    @ApiModelProperty(value = "项目id", required = true, example = "1")
    private int projectId;
    @NotNull
    @ApiModelProperty(value = "所属平台", required = true, example = "1")
    private int platform;
    @NotNull
    @ApiModelProperty(value = "分支名", required = true, example = "v1.0.0/release")
    private String branchName;
    @NotNull
    @ApiModelProperty(value = "版本号", required = true, example = "1.0.0")
    private String appVersion;
    @NotNull
    @ApiModelProperty(value = "下载路径（如无下载路径可以传本地安装路径）", required = true, example = "http://localhost:123/abc.apk")
    private String url;
    @ApiModelProperty(value = "符号表路径（iOS only）", example = "./dSYM/Test.app.dSYM")
    private String dsymUrl;
    @Positive
    @ApiModelProperty(value = "Jenkins构建id（$BUILD_NUMBER in Jenkins）", required = true, example = "123")
    private int buildId;
    @NotNull
    @ApiModelProperty(value = "是否强制关闭其他测试的包", required = true, example = "false")
    private boolean isForce;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDsymUrl() {
        return dsymUrl;
    }

    public void setDsymUrl(String dsymUrl) {
        this.dsymUrl = dsymUrl;
    }

    public int getBuildId() {
        return buildId;
    }

    public void setBuildId(int buildId) {
        this.buildId = buildId;
    }

    public boolean getIsForce() {
        return isForce;
    }

    public void setIsForce(boolean isForce) {
        this.isForce = isForce;
    }

    @Override
    public String toString() {
        return "PackageUpdate{" +
                "projectId=" + projectId +
                ", platform='" + platform + '\'' +
                ", branchName='" + branchName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", url='" + url + '\'' +
                ", dsymUrl='" + dsymUrl + '\'' +
                ", buildId=" + buildId +
                ", isForce=" + isForce +
                '}';
    }
}
