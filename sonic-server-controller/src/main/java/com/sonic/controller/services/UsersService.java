package com.sonic.controller.services;

import com.sonic.common.exception.SonicException;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.Users;
import com.sonic.controller.models.http.ChangePwd;
import com.sonic.controller.models.http.UserInfo;

public interface UsersService {
    void register(Users users) throws SonicException;

    String login(UserInfo userInfo);

    Users getUserInfo(String token);

    RespModel resetPwd(String token, ChangePwd changePwd);
}
