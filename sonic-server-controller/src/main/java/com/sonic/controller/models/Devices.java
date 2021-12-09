package com.sonic.controller.models;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
@ApiModel("设备模型")
//添加udId字段索引
@Table(indexes = {@Index(columnList = "udId")})
public class Devices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @ApiModelProperty(value = "设备名称", example = "My HUAWEI")
    String name;
    @ApiModelProperty(value = "设备备注", example = "My HUAWEI")
    String nickName;
    @ApiModelProperty(value = "型号", example = "HUAWEI MATE 40")
    String model;
    @ApiModelProperty(value = "序列号", example = "random")
    String udId;
    @ApiModelProperty(value = "设备状态", example = "ONLINE")
    String status;
    @ApiModelProperty(value = "所属Agent", example = "1")
    int agentId;
    @ApiModelProperty(value = "设备系统", example = "1")
    int platform;
    @ApiModelProperty(value = "分辨率", example = "1080x1920")
    String size;
    @ApiModelProperty(value = "系统版本", example = "12")
    String version;
    @ApiModelProperty(value = "cpu", example = "arm64")
    String cpu;
    @ApiModelProperty(value = "制造商", example = "HUAWEI")
    String manufacturer;
    @ApiModelProperty(value = "安装密码", example = "123456")
    String password;
    @ApiModelProperty(value = "设备图片路径")
    String imgUrl;
    @ManyToMany(mappedBy = "devices")
    @JsonIgnore
    @JSONField(serialize = false)
    Set<TestSuites> testSuites;
    @ApiModelProperty(value = "设备占用者")
    String user;

    public Devices() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUdId() {
        return udId;
    }

    public void setUdId(String udId) {
        this.udId = udId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<TestSuites> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(Set<TestSuites> testSuites) {
        this.testSuites = testSuites;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Devices{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", model='" + model + '\'' +
                ", udId='" + udId + '\'' +
                ", status='" + status + '\'' +
                ", agentId=" + agentId +
                ", platform=" + platform +
                ", size='" + size + '\'' +
                ", version='" + version + '\'' +
                ", cpu='" + cpu + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", password='" + password + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
