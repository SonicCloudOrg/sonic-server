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
@ApiModel("用户模型")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    int id;
    @NotNull
    @ApiModelProperty(value = "用户名称", required = true, example = "ZhouYiXun")
    String username;
    @NotNull
    @ApiModelProperty(value = "用户密码", required = true, example = "123456")
    String password;
    @NotNull
    @ApiModelProperty(value = "所属部门", required = true, example = "中后台-测试部")
    String department;
    @Positive
    @ApiModelProperty(value = "角色", required = true, example = "1")
    int role;

    public Users(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}