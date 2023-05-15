package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.common.exception.SonicException;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.ProjectsMembers;
import org.cloud.sonic.controller.models.dto.ProjectsMembersDTO;

/**
 * @author liulijun
 * @des 项目成员逻辑层
 * @date 2023/4/30 20:51
 */
public interface ProjectsMembersService extends IService<ProjectsMembers> {

    void deleteById(int id) throws SonicException;

    void deleteByProjectId(int projectId) throws SonicException;

    void add(ProjectsMembers projectsMembers) throws SonicException;

     CommentPage<ProjectsMembersDTO> findByProjectId(Page<ProjectsMembers> page, int projectId);
}

