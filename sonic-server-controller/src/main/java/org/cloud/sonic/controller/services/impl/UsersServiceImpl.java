/**
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.config.annotation.DubboService;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.cloud.sonic.controller.mapper.UsersMapper;
import org.cloud.sonic.common.models.domain.Users;
import org.cloud.sonic.common.models.http.ChangePwd;
import org.cloud.sonic.common.models.http.UserInfo;
import org.cloud.sonic.common.models.interfaces.UserLoginType;
import org.cloud.sonic.common.services.UsersService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/10/13 11:26
 */
@Service
@DubboService
public class UsersServiceImpl extends SonicServiceImpl<UsersMapper, Users> implements UsersService {
    private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    private JWTTokenTool jwtTokenTool;

    @Autowired
    private UsersMapper usersMapper;

    @Value("${sonic.user.ldap.enable}")
    private boolean ldapEnable;

    @Value("${sonic.user.normal.enable}")
    private boolean normalEnable;

    @Value("${sonic.user.register.enable}")
    private boolean registerEnable;

    @Value("${sonic.user.ldap.userId}")
    private String userId;

    @Value("${sonic.user.ldap.userBaseDN}")
    private String userBaseDN;

    @Autowired
    @Lazy
    private LdapTemplate ldapTemplate;

    @Override
    public JSONObject getLoginConfig() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("registerEnable",registerEnable);
        jsonObject.put("normalEnable",normalEnable);
        jsonObject.put("ldapEnable",ldapEnable);
        return jsonObject;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(Users users) throws SonicException {
        if (registerEnable) {
            try {
                users.setPassword(DigestUtils.md5DigestAsHex(users.getPassword().getBytes()));
                save(users);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SonicException("注册失败！用户名已存在！");
            }
        } else {
            throw new SonicException("注册入口已关闭！");
        }
    }

    @Override
    public String login(UserInfo userInfo) {
        Users users = findByUserName(userInfo.getUserName());
        String token = null;
        if (users == null) {
            if (checkLdapAuthenticate(userInfo, true)) {
                token = jwtTokenTool.getToken(userInfo.getUserName());
            }
        }else if (normalEnable && UserLoginType.LOCAL.equals(users.getSource()) && DigestUtils.md5DigestAsHex(userInfo.getPassword().getBytes()).equals(users.getPassword())) {
            token = jwtTokenTool.getToken(users.getUserName());
            users.setPassword("");
            logger.info("用户：" + userInfo.getUserName() + "登入! token:" + token);
        } else {
            if (checkLdapAuthenticate(userInfo, false)) {
                token = jwtTokenTool.getToken(users.getUserName());
                logger.info("ldap 用户：" + userInfo.getUserName() + "登入! token:" + token);
            }
        }
        return token;
    }

    private boolean checkLdapAuthenticate(UserInfo userInfo, boolean create) {
        if (!ldapEnable) return false;
        String username = userInfo.getUserName();
        String password = userInfo.getPassword();
        logger.info("login check content username {}", username);
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person")).and(new EqualsFilter(userId, username));
        try {
            boolean authResult = ldapTemplate.authenticate(userBaseDN, filter.toString(), password);
            if (create) {
                save(buildUser(userInfo));
            }
            return authResult;
        } catch (Exception e) {
            logger.error("ldap 登录异常，{}", e);
            return false;
        }
    }

    private Users buildUser(UserInfo userInfo) {
        Users users = new Users();
        users.setUserName(userInfo.getUserName());
        users.setPassword("");
        users.setRole(2);
        users.setSource(UserLoginType.LDAP);
        return users;
    }

    @Override
    public Users getUserInfo(String token) {
        String name = jwtTokenTool.getUserName(token);
        if (name != null) {
            Users users = findByUserName(name);
            users.setPassword("");
            return users;
        } else {
            return null;
        }
    }

    @Override
    public RespModel<String> resetPwd(String token, ChangePwd changePwd) {
        String name = jwtTokenTool.getUserName(token);
        if (name != null) {
            Users users = findByUserName(name);
            if (users != null) {
                if (DigestUtils.md5DigestAsHex(changePwd.getOldPwd().getBytes()).equals(users.getPassword())) {
                    users.setPassword(DigestUtils.md5DigestAsHex(changePwd.getNewPwd().getBytes()));
                    save(users);
                    return new RespModel(2000, "修改密码成功！");
                } else {
                    return new RespModel(4001, "旧密码错误！");
                }
            } else {
                return new RespModel(RespEnum.UNAUTHORIZED);
            }
        } else {
            return new RespModel(RespEnum.UNAUTHORIZED);
        }
    }

    @Override
    public Users findByUserName(String userName) {
        Assert.hasText(userName, "userName must not be null");
        return lambdaQuery().eq(Users::getUserName, userName).one();
    }
}
