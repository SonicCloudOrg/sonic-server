/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.cloud.sonic.controller.mapper.UsersMapper;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Roles;
import org.cloud.sonic.controller.models.domain.Users;
import org.cloud.sonic.controller.models.dto.UsersDTO;
import org.cloud.sonic.controller.models.http.ChangePwd;
import org.cloud.sonic.controller.models.http.UserInfo;
import org.cloud.sonic.controller.models.interfaces.UserLoginType;
import org.cloud.sonic.controller.services.RolesServices;
import org.cloud.sonic.controller.services.UsersService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/10/13 11:26
 */
@Service
public class UsersServiceImpl extends SonicServiceImpl<UsersMapper, Users> implements UsersService {
    private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Resource
    private JWTTokenTool jwtTokenTool;

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private RolesServices rolesServices;

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

    @Value("${sonic.user.ldap.objectClass}")
    private String objectClass;

    @Resource
    @Lazy
    private LdapTemplate ldapTemplate;

    @Override
    public JSONObject getLoginConfig() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("registerEnable", registerEnable);
        jsonObject.put("normalEnable", normalEnable);
        jsonObject.put("ldapEnable", ldapEnable);
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
                throw new SonicException("register.repeat.username");
            }
        } else {
            throw new SonicException("register.disable");
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
        } else if (normalEnable && UserLoginType.LOCAL.equals(users.getSource()) && DigestUtils.md5DigestAsHex(userInfo.getPassword().getBytes()).equals(users.getPassword())) {
            token = jwtTokenTool.getToken(users.getUserName());
            users.setPassword("");
            logger.info("user: " + userInfo.getUserName() + " login! token:" + token);
        } else {
            if (checkLdapAuthenticate(userInfo, false)) {
                token = jwtTokenTool.getToken(users.getUserName());
                logger.info("ldap user: " + userInfo.getUserName() + "login! token:" + token);
            }
        }
        return token;
    }

    private boolean checkLdapAuthenticate(UserInfo userInfo, boolean create) {
        if (!ldapEnable) return false;
        String username = userInfo.getUserName();
        String password = userInfo.getPassword();
        if (password.isEmpty()) return false;
        logger.info("login check content username {}", username);
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", objectClass)).and(new EqualsFilter(userId, username));
        try {
            boolean authResult = ldapTemplate.authenticate(userBaseDN, filter.toString(), password);
            if (create && authResult) {
                save(buildUser(userInfo));
            }
            return authResult;
        } catch (Exception e) {
            logger.info("ldap login failed, cause: {}", e.getMessage());
            return false;
        }
    }

    private Users buildUser(UserInfo userInfo) {
        Users users = new Users();
        users.setUserName(userInfo.getUserName());
        users.setPassword("");
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
                    return new RespModel(2000, "password.change.ok");
                } else {
                    return new RespModel(4001, "password.auth.fail");
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

    @Override
    public CommentPage<UsersDTO> listUsers(Page<Users> page, String userName) {

        Page<Users> users = lambdaQuery()
                .like(!StringUtils.isEmpty(userName), Users::getUserName, userName)
                .orderByDesc(Users::getId)
                .page(page);

        Map<Integer, Roles> rolesMap = rolesServices.mapRoles();
        final Roles emptyRole = new Roles();
        List<UsersDTO> rolesDTOList = users.getRecords().stream()
                .map(e -> {
                    UsersDTO usersDTO = e.convertTo();
                    Roles role = rolesMap.getOrDefault(e.getUserRole(), emptyRole);
                    usersDTO.setRole(role.getId())
                            .setRoleName(role.getRoleName());
                    usersDTO.setPassword("");
                    return usersDTO;
                }).collect(Collectors.toList());
        return CommentPage.convertFrom(page, rolesDTOList);
    }

    @Override
    public boolean updateUserRole(Integer userId, Integer roleId) {
        return lambdaUpdate().eq(Users::getId, userId)
                .set(Users::getUserRole, roleId)
                .update();
    }


}
