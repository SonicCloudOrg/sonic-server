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
package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.domain.Cabinet;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des Cabinet逻辑层
 * @date 2022/4/26 22:51
 */
public interface CabinetService extends IService<Cabinet> {
    List<Cabinet> findCabinets();

    void saveCabinet(Cabinet cabinet);

    Cabinet getIdByKey(String key);

    void errorCall(Cabinet cabinet,String udId,int tem,int type);
}
