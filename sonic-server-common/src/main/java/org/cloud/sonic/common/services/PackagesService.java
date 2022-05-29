package org.cloud.sonic.common.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.common.models.base.CommentPage;
import org.cloud.sonic.common.models.domain.Packages;
import org.cloud.sonic.common.models.dto.PackageDTO;

/**
 * @author yaming116
 * @des 安装包管理辑层
 * @date 2022/5/26
 */
public interface PackagesService extends IService<Packages> {

    /**
     * 根据项目获取所有安装包
     * @return
     */
    CommentPage<PackageDTO> findByProjectId(int projectId, String branch, String platform, Page<Packages> pageable);
}
