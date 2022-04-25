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

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.common.models.domain.Cabinet;
import org.cloud.sonic.common.services.CabinetService;
import org.cloud.sonic.controller.mapper.CabinetMapper;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CabinetServiceImpl extends SonicServiceImpl<CabinetMapper, Cabinet> implements CabinetService {
    @Resource
    private CabinetMapper cabinetMapper;

    @Override
    public List<Cabinet> findCabinets() {
        return list();
    }

    @Override
    public void saveCabinet(Cabinet cabinet) {
        save(cabinet);
    }

    @Override
    public Cabinet getIdByKey(String key) {
        return lambdaQuery().eq(Cabinet::getSecretKey, key).one();
    }
}
