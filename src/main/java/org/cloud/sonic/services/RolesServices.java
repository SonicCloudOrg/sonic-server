package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Roles;
import org.cloud.sonic.controller.models.dto.RolesDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RolesServices extends IService<Roles> {

    /**
     * 保存或编辑角色
     *
     * @param rolesDTO
     */
    void save(RolesDTO rolesDTO);

    /**
     * 获取所有角色
     *
     * @return
     */
    CommentPage<RolesDTO> listRoles(Page<Roles> page, String roleName);

    /**
     * 获取所有角色
     *
     * @return
     */
    Map<Integer, Roles> mapRoles();

    /**
     * 根据 id 获取角色信息
     *
     * @param roleId
     * @return
     */
    Roles findById(Integer roleId);

    /**
     * 保存当前角色对应的资源 id
     *
     * @param roleId
     * @param resId
     */
    void saveResourceRoles(Integer roleId, List<Integer> resId);

    /**
     * 删除角色
     *
     * @param roleId 角色 id
     */
    void delete(Integer roleId);

    /**
     * 编辑资源位对应权限
     *
     * @param roleId
     * @param resId
     * @param hasAuth
     */
    void editResourceRoles(Integer roleId, Integer resId, boolean hasAuth);

    /**
     * 校验用户是否有对应资源权限
     *
     * @param userName
     * @param path
     * @param method
     * @return
     */
    boolean checkUserHasResourceAuthorize(String userName, String path, String method);

}
