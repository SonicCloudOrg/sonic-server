package com.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.sonic.common.exception.SonicException;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.common.tools.JWTTokenTool;
import com.sonic.controller.dao.UsersRepository;
import com.sonic.controller.models.Users;
import com.sonic.controller.models.http.ChangePwd;
import com.sonic.controller.models.http.UserInfo;
import com.sonic.controller.services.UsersService;
import com.sonic.controller.tools.RedisTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/10/13 11:26
 */
@Service
public class UsersServiceImpl implements UsersService {
    private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RedisTool redisTool;

    @Override
    @Transactional(rollbackFor = SonicException.class)
    public void register(Users users) throws SonicException {
        try {
            users.setPassword(DigestUtils.md5DigestAsHex(users.getPassword().getBytes()));
            usersRepository.save(users);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SonicException("注册失败！用户名已存在！");
        }
    }

    @Override
    public String login(UserInfo userInfo) {
        Users users = usersRepository.findByUserName(userInfo.getUserName());
        if (users != null && DigestUtils.md5DigestAsHex(userInfo.getPassword().getBytes()).equals(users.getPassword())) {
            String token = JWTTokenTool.getToken(userInfo.getUserName());
            users.setPassword("");
            redisTool.set("sonic:user:" + token, users, 7);
            logger.info("用户：" + userInfo.getUserName() + "登入! token:" + token);
            return token;
        } else {
            return null;
        }
    }

    @Override
    public Users getUserInfo(String token) {
        Object redisTokenObject = RedisTool.get("sonic:user:" + token);
        if (redisTokenObject != null) {
            return (Users) redisTokenObject;
        } else {
            return null;
        }
    }

    @Override
    public RespModel resetPwd(String token, ChangePwd changePwd) {
        Object redisTokenObject = RedisTool.get("sonic:user:" + token);
        if (redisTokenObject != null) {
            Users users = usersRepository.findByUserName(((Users) redisTokenObject).getUserName());
            if (users != null) {
                if (DigestUtils.md5DigestAsHex(changePwd.getOldPwd().getBytes()).equals(users.getPassword())) {
                    users.setPassword(DigestUtils.md5DigestAsHex(changePwd.getNewPwd().getBytes()));
                    usersRepository.save(users);
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
}
