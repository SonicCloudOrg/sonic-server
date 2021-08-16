package com.sonic.control.Models;

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
    String system;
    @NotNull
    @ApiModelProperty(value = "Agent端版本号", example = "1.0.0")
    String version;
    @NotNull
    @ApiModelProperty(value = "Agent端所在ip", example = "127.0.0.1")
    String ip;
    @Positive
    @ApiModelProperty(value = "Agent端暴露web端口", example = "1234")
    int port;
    @ApiModelProperty(value = "Agent端状态", example = "1")
    int status;

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

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    @Override
    public String toString() {
        return "Agents{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", system='" + system + '\'' +
                ", version='" + version + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", status=" + status +
                '}';
    }
}
