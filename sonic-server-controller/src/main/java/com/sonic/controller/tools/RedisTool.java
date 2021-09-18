package com.sonic.controller.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ZhouYiXun
 * @des redis工具类
 * @date 2021/8/15 18:26
 */
@Component
public class RedisTool {
    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisTool.redisTemplate = redisTemplate;
    }

    /**
     * @param key
     * @return java.lang.Object
     * @author ZhouYiXun
     * @des 根据key获取value
     * @date 2021/8/15 22:13
     */
    public static Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * @param key
     * @param value
     * @return boolean
     * @author ZhouYiXun
     * @des 存放key和value
     * @date 2021/8/15 22:13
     */
    public static boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param key
     * @param value
     * @param time
     * @return boolean
     * @author ZhouYiXun
     * @des 设置过期时间
     * @date 2021/8/15 22:15
     */
    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.DAYS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param key
     * @param time
     * @return boolean
     * @author ZhouYiXun
     * @des 重新设置过期时间
     * @date 2021/8/15 22:16
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.DAYS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}