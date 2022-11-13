/*
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
package org.cloud.sonic.controller.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.common.tools.JWTTokenTool;
import org.cloud.sonic.controller.models.domain.Resources;
import org.cloud.sonic.controller.models.interfaces.UrlType;
import org.cloud.sonic.controller.services.ResourcesService;
import org.cloud.sonic.controller.services.RolesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                JSONObject re = (JSONObject) JSONObject.toJSON(new RespModel(RespEnum.RESOURCE_NOT_FOUND));
                response.getWriter().write(re.toJSONString());
                return;
            }

            if (resources.getWhite() == UrlType.WHITE || resources.getNeedAuth() == UrlType.WHITE) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!rolesServices.checkUserHasResourceAuthorize(userName, resourceName, method)) {

                response.setContentType("text/plain;charset=UTF-8");
                JSONObject re = (JSONObject) JSONObject.toJSON(new RespModel(RespEnum.PERMISSION_DENIED));
                response.getWriter().write(re.toJSONString());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


}
