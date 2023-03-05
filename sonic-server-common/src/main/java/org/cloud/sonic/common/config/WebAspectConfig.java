package org.cloud.sonic.common.config;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

/**
 * @author ZhouYiXun
 * @des AOP框架搭配注解类，生成对应web请求日志
 * @date 2021/8/15 18:26
 */
@Aspect
@Component
public class WebAspectConfig {
    private final Logger logger = LoggerFactory.getLogger(WebAspectConfig.class);

    /**
     * @return void
     * @author ZhouYiXun
     * @des 定义切点，注解类webAspect
     * @date 2021/8/15 23:08
     */
    @Pointcut("@annotation(WebAspect)")
    public void webAspect() {
    }

    /**
     * @param joinPoint
     * @return void
     * @author ZhouYiXun
     * @des 请求前获取所有信息
     * @date 2021/8/15 23:08
     */
    @Before("webAspect()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //默认打印为json格式，接入elasticsearch等会方便查看
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", request.getRequestURL().toString());
        jsonObject.put("method", request.getMethod());
        jsonObject.put("auth", request.getHeader("SonicToken"));
        jsonObject.put("class", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        jsonObject.put("request", Arrays.toString(joinPoint.getArgs()));
        logger.info(jsonObject.toJSONString());
    }

    /**
     * @param ret
     * @return void
     * @author ZhouYiXun
     * @des 请求完毕后打印结果
     * @date 2021/8/15 23:10
     */
    @AfterReturning(returning = "ret", pointcut = "webAspect()")
    public void doAfterReturning(Object ret) throws Throwable {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", ret);
        logger.info(jsonObject.toJSONString());
    }

    /**
     * @param joinPoint
     * @param ex
     * @return void
     * @author ZhouYiXun
     * @des 报错的话打印错误信息
     * @date 2021/8/15 23:11
     */
    @AfterThrowing(throwing = "ex", pointcut = "webAspect()")
    public void error(JoinPoint joinPoint, Exception ex) {
        logger.info("error : " + ex.getMessage());
    }

}