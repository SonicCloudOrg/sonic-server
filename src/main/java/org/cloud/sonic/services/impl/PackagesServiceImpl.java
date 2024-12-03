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


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.cloud.sonic.controller.mapper.PackagesMapper;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.base.TypeConverter;
import org.cloud.sonic.controller.models.domain.Packages;
import org.cloud.sonic.controller.models.dto.PackageDTO;
import org.cloud.sonic.controller.services.PackagesService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yaming116
 * @des
 * @date 2022/5/26 1:22
 */

@Service
public class PackagesServiceImpl extends SonicServiceImpl<PackagesMapper, Packages> implements PackagesService {

    @Override
    public String findOne(int projectId, String branch, String platform) {
        Packages packages = lambdaQuery().eq(Packages::getProjectId, projectId)
                .eq(StringUtils.isNotBlank(platform), Packages::getPlatform, platform)
                .like(StringUtils.isNotBlank(branch), Packages::getBranch, branch)
                .orderByDesc(Packages::getId).last("LIMIT 1").one();
        if (packages != null) {
            return packages.getUrl();
        } else {
            return "";
        }
    }

    @Override
    public CommentPage<PackageDTO> findByProjectId(int projectId, String branch, String platform, String packageName,
                                                   Page<Packages> pageable) {
        Page<Packages> page = lambdaQuery().eq(Packages::getProjectId, projectId)
                .eq(StringUtils.isNotBlank(platform), Packages::getPlatform, platform)
                .like(StringUtils.isNotBlank(branch), Packages::getBranch, branch)
                .like(StringUtils.isNotBlank(packageName), Packages::getPkgName, packageName)
                .orderByDesc(Packages::getId)
                .page(pageable);

        List<PackageDTO> packageDTOList = page.getRecords()
                .stream().map(TypeConverter::convertTo).collect(Collectors.toList());

        return CommentPage.convertFrom(page, packageDTOList);
    }
}
