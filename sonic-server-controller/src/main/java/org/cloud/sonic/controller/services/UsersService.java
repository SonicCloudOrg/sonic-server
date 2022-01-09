package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.controller.models.domain.Users;
import org.cloud.sonic.controller.models.http.ChangePwd;
import org.cloud.sonic.controller.models.http.UserInfo;

public interface UsersService extends IService<Users> {
    void register(Users users) throws SonicException;

    String login(UserInfo userInfo);

    Users getUserInfo(String token);

    RespModel<String> resetPwd(String token, ChangePwd changePwd);

    Users findByUserName(String userName);
}
