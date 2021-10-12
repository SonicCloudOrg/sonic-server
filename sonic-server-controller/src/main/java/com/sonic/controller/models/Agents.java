package com.sonic.controller.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@ApiModel("Agent端模型")
public class Agents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @NotNull
    @ApiModelProperty(value = "Agent端名称", example = "本地Agent")
    String name;
    @NotNull
    @ApiModelProperty(value = "Agent端系统类型", example = "Windows 10")
    String systemType;
    @NotNull
    @ApiModelProperty(value = "Agent端版本号", example = "1.0.0")
    String version;
    @NotNull
    @ApiModelProperty(value = "Agent端所在host", example = "127.0.0.1")
    String host;
    @NotNull
    @ApiModelProperty(value = "Agent端暴露web端口", example = "1234")
    int port;
    @ApiModelProperty(value = "Agent端状态", example = "1")
    int status;
    @ApiModelProperty(value = "Agent端密钥", example = "qwe")
    String secretKey;

    public Agents() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        return "Agents{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", systemType='" + systemType + '\'' +
                ", version='" + version + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", status=" + status +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }
}
