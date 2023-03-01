package org.cloud.sonic.controller.config;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Slf4j
@Order(-90000)
@Component
public class ProjectPermissionAop {

    @Pointcut("execution(* org.cloud.sonic.controller.controller.project.*.*(..)) && @annotation(io.swagger.annotations.ApiOperation)")
    public void projectAspect() {
    }

    @Before("projectAspect()")
    public void execute(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("cut ........................,class: {}", joinPoint.getTarget().getClass());

        if (joinPoint.getTarget() instanceof ProjectPermissionHandler) {
            ProjectPermissionHandler handler = (ProjectPermissionHandler) joinPoint.getTarget();
            handler.getProjectId(joinPoint.getSignature().getName(), joinPoint.getArgs());
//            request.g
            // 权限判断
        }else {

        }


    }
}
