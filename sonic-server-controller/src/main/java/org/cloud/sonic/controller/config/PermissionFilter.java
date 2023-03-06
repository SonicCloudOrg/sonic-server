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
package org.cloud.sonic.controller.config;

import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.common.config.CommonResultControllerAdvice;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.cloud.sonic.controller.models.domain.Resources;
import org.cloud.sonic.controller.models.interfaces.UrlType;
import org.cloud.sonic.controller.services.ResourcesService;
import org.cloud.sonic.controller.services.RolesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class PermissionFilter extends OncePerRequestFilter {

    /**
     * 是否开启权限管理
     */
    @Value("${sonic.permission.enable}")
    private boolean permissionEnable;
    /**
     * 内置超管账号
     */
    @Value("${sonic.permission.superAdmin}")
    private String superAdmin;

    @Autowired
    private JWTTokenTool jwtTokenTool;

    @Autowired
    private RolesServices rolesServices;

    @Autowired
    private ResourcesService resourcesService;

    public static final String TOKEN = "SonicToken";

    @Resource
    private MessageSource messageSource;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(TOKEN);

        if (permissionEnable && token != null && !request.getMethod().equalsIgnoreCase("options")) {
            String userName = jwtTokenTool.getUserName(token);

            if (Objects.equals(superAdmin, userName)) {
                filterChain.doFilter(request, response);
                return;
            }

            String resourceName = request.getServletPath();
            String method = request.getMethod();
            log.info("PermissionFilter: {}", resourceName);

            Resources resources = resourcesService.search(resourceName, method);

            if (resources == null) {
                response.setContentType("text/plain;charset=UTF-8");
                JSONObject re = (JSONObject) JSONObject.toJSON(process(request, new RespModel(RespEnum.RESOURCE_NOT_FOUND)));
                response.getWriter().write(re.toJSONString());
                return;
            }

            if (resources.getWhite() == UrlType.WHITE || resources.getNeedAuth() == UrlType.WHITE) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!rolesServices.checkUserHasResourceAuthorize(userName, resourceName, method)) {
                response.setContentType("text/plain;charset=UTF-8");
                JSONObject re = (JSONObject) JSONObject.toJSON(process(request, new RespModel(RespEnum.PERMISSION_DENIED)));
                response.getWriter().write(re.toJSONString());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


    private RespModel process(HttpServletRequest request, RespModel respModel) {
        String l = request.getHeader("Accept-Language");
        CommonResultControllerAdvice.process(l, respModel, messageSource);

        return respModel;
    }


}
