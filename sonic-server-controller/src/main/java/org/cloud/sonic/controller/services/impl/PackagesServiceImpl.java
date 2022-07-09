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
    public CommentPage<PackageDTO> findByProjectId(int projectId, String branch, String platform, Page<Packages> pageable) {
        Page<Packages> page = lambdaQuery().eq(Packages::getProjectId, projectId)
                .eq(StringUtils.isNotBlank(platform), Packages::getPlatform, platform)
                .like(StringUtils.isNotBlank(branch), Packages::getBranch, branch)
                .orderByDesc(Packages::getId)
                .page(pageable);

        List<PackageDTO> packageDTOList = page.getRecords()
                .stream().map(TypeConverter::convertTo).collect(Collectors.toList());

        return CommentPage.convertFrom(page, packageDTOList);


    }
}
