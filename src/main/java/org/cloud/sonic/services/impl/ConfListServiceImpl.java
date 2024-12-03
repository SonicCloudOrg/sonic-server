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

import org.cloud.sonic.controller.mapper.ConfListMapper;
import org.cloud.sonic.controller.models.domain.ConfList;
import org.cloud.sonic.controller.services.ConfListService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ConfListServiceImpl extends SonicServiceImpl<ConfListMapper, ConfList> implements ConfListService {


    @Override
    public ConfList searchByKey(String key) {
        return lambdaQuery().eq(ConfList::getConfKey, key).last("limit 1").one();
    }

    @Override
    public void save(String key, String content, String extra) {
        ConfList conf = searchByKey(key);

        if (conf == null) {
            conf = new ConfList();
        }
        conf.setContent(content)
                .setConfKey(key)
                .setExtra(extra);
        saveOrUpdate(conf);
    }
}
