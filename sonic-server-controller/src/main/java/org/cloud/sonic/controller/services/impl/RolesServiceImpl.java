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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.cloud.sonic.controller.mapper.RoleResourcesMapper;
import org.cloud.sonic.controller.mapper.RolesMapper;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.RoleResources;
import org.cloud.sonic.controller.models.domain.Roles;
import org.cloud.sonic.controller.models.dto.RolesDTO;
import org.cloud.sonic.controller.services.RolesServices;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RolesServiceImpl extends SonicServiceImpl<RolesMapper, Roles> implements RolesServices {

    @Resource
    private RoleResourcesMapper roleResourcesMapper;

    @Override
    @Transactional
    public void save(RolesDTO rolesDTO) {
        Roles roles = rolesDTO.convertTo();
        if (rolesDTO.getId() == null) {
            save(roles);
        } else {
            lambdaUpdate().eq(Roles::getId, roles.getId())
                    .update(roles);
        }

    }

    @Override
    public CommentPage<RolesDTO> listRoles(Page<Roles> page, String roleName) {
        Page<Roles> roles = lambdaQuery()
                .like(!StringUtils.isEmpty(roleName), Roles::getRoleName, roleName)
                .orderByDesc(Roles::getId)
                .page(page);
        List<RolesDTO> rolesDTOList = roles.getRecords().stream()
                .map(TypeConverter::convertTo).collect(Collectors.toList());
        return CommentPage.convertFrom(page, rolesDTOList);
    }

    @Override
    public Map<Integer, Roles> mapRoles() {
        return lambdaQuery()
                .orderByDesc(Roles::getId)
                .list()
                .stream()
                .collect(Collectors.toMap(Roles::getId, Function.identity(), (a, b) -> a));
    }

    @Override
    public Roles findById(Integer roleId) {
        return getById(roleId);
    }

    @Transactional
    public void editResourceRoles(Integer roleId, Integer resId, boolean hasAuth) {
        if (hasAuth) {
            roleResourcesMapper.insert(RoleResources.builder().roleId(roleId).resId(resId).build());
        } else {
            roleResourcesMapper.delete(new LambdaQueryWrapper<RoleResources>()
                    .eq(RoleResources::getRoleId, roleId)
                    .eq(RoleResources::getResId, resId));
        }
    }

    @Override
    @Transactional
    public void saveResourceRoles(Integer roleId, List<Integer> resId) {
        // 先查询目前角色下所有权限
        List<RoleResources> roleResourcesList = lambdaQuery(roleResourcesMapper).eq(RoleResources::getRoleId, roleId).list();

        List<Integer> roleResourceIds = roleResourcesList.stream().map(RoleResources::getResId).collect(Collectors.toList());
        List<Integer> roleResourceCopyIds = roleResourceIds.stream().collect(Collectors.toList());
        //移除当前角色不需要资源
        roleResourceIds.removeAll(resId);
        roleResourcesMapper.delete(new LambdaQueryWrapper<RoleResources>().eq(RoleResources::getRoleId, roleId).in(RoleResources::getResId, roleResourceIds));
        //添加当前角色下新权限
        resId.removeAll(roleResourceCopyIds);
        resId.stream().map(id -> RoleResources.builder().roleId(roleId).resId(id).build())
                .forEach(e -> roleResourcesMapper.insert(e));
    }

    @Transactional
    @Override
    public void delete(Integer roleId) {
        getBaseMapper().deleteById(roleId);
    }

    @Override
    public boolean checkUserHasResourceAuthorize(String userName, String path, String method) {
        int count = roleResourcesMapper.checkUserHasResourceAuthorize(userName, path, method);
        return count > 0;
    }
}
