package com.sonic.controller.services;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sonic.common.exception.SonicException;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.domain.Users;
import com.sonic.controller.models.http.ChangePwd;
import com.sonic.controller.models.http.UserInfo;

public interface UsersService extends IService<Users> {
    void register(Users users) throws SonicException;

    String login(UserInfo userInfo);

    Users getUserInfo(String token);

    RespModel<String> resetPwd(String token, ChangePwd changePwd);

    Users findByUserName(String userName);
}
