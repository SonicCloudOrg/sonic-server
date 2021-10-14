package com.sonic.common.tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;

import java.util.UUID;

/**
 * @author ZhouYiXun
 * @des Token加解密工具类
 * @date 2021/8/15 18:26
 */
public class JWTTokenTool {
    //token密钥，生产环境下应该暴露给外部配置文件，例如.env，由有权限的运维同事去管理
    private static final String TOKEN_SECRET = "sonic";

    /**
     * @param username
     * @return java.lang.String
     * @author ZhouYiXun
     * @des 通过用户名生成token
     * @date 2021/8/15 23:05
     */
    public static String getToken(String username) {
        return JWT.create().withAudience(username, UUID.randomUUID().toString())
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    /**
     * @param token
     * @return boolean
     * @author ZhouYiXun
     * @des 校验token的签名是否合法
     * @date 2021/8/15 23:05
     */
    public static boolean verify(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
        try {
            jwtVerifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}