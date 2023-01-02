package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.Resources;
import org.cloud.sonic.controller.models.dto.ResourcesDTO;

import java.util.List;


public interface ResourcesService extends IService<Resources> {

    int insert(Resources resources);

    Resources searchByPath(String path, Integer parentId);

    /**
     * 根据路径查和方法找对应资源
     *
     * @param path
     * @param method
     * @return
     */
    Resources search(String path, String method);

    /**
     * 初始化或全量更新
     */
    void init();

    /**
     * @param id       资源 id
     * @param needAuth 是否需要鉴权
     */
    void updateResourceAuth(Integer id, Boolean needAuth);

    /**
     * @param page
     * @param isAll 是否查询全量，全量查询不分页
     * @param path  资源路径
     * @return
     */
    CommentPage<ResourcesDTO> listResource(Page<Resources> page, String path, boolean isAll);

    /**
     * 获取详情角色下的资源情况
     *
     * @param roleId
     * @return
     */
    List<ResourcesDTO> listRoleResource(Integer roleId);
}
