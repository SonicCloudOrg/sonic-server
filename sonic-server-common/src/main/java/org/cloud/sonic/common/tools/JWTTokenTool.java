package org.cloud.sonic.common.tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author ZhouYiXun
 * @des Token加解密工具类
 * @date 2021/8/15 18:26
 */
@Configuration
public class JWTTokenTool {
    @Value("${gateway.secret}")
    private String TOKEN_SECRET;
    @Value("${gateway.expireDay}")
    private int EXPIRE_DAY;

    /**
     * @param username
     * @return java.lang.String
     * @author ZhouYiXun
     * @des 通过用户名生成token
     * @date 2021/8/15 23:05
     */
    public String getToken(String username) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, EXPIRE_DAY);
        Date nowTime = now.getTime();
        return JWT.create().withAudience(username, UUID.randomUUID().toString())
                .withExpiresAt(nowTime)
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    /**
     * @param token
     * @return java.lang.String
     * @author ZhouYiXun
     * @des 由token获取生成时的用户信息
     * @date 2021/8/15 23:05
     */
    public String getUserName(String token) {
        try {
            return JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * @param token
     * @return boolean
     * @author ZhouYiXun
     * @des 校验token的签名是否合法
     * @date 2021/8/15 23:05
     */
    public boolean verify(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
        try {
            jwtVerifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}