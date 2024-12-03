package org.cloud.sonic.services;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.http.RespModel;
import org.cloud.sonic.models.base.CommentPage;
import org.cloud.sonic.models.domain.Users;
import org.cloud.sonic.models.dto.UsersDTO;
import org.cloud.sonic.models.http.ChangePwd;
import org.cloud.sonic.models.http.UserInfo;

public interface UsersService extends IService<Users> {
    JSONObject getLoginConfig();

    void register(Users users) throws SonicException;

    String login(UserInfo userInfo);

    Users getUserInfo(String token);

    RespModel<String> resetPwd(String token, ChangePwd changePwd);

    Users findByUserName(String userName);

    /**
     * 获取用户列表
     *
     * @param page
     * @return
     */
    CommentPage<UsersDTO> listUsers(Page<Users> page, String userName);

    /**
     * 更新用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    boolean updateUserRole(Integer userId, Integer roleId);
}
