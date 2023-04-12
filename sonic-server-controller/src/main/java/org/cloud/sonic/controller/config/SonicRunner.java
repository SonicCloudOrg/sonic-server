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
package org.cloud.sonic.controller.config;

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.models.domain.ConfList;
import org.cloud.sonic.controller.models.interfaces.ConfType;
import org.cloud.sonic.controller.services.ConfListService;
import org.cloud.sonic.controller.services.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class SonicRunner implements ApplicationRunner {

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private ConfListService confListService;

    @Value("${spring.version}")
    private String version;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        resourceInit();
        remoteInit();
        idleInit();
    }

    /**
     * 每次启动对信息进行版本对比，不一致进行一起更新
     */
    private void resourceInit() {
        try {

            ConfList conf = confListService.searchByKey(ConfType.RESOURCE);
            if (conf != null && Objects.equals(conf.getContent(), version)) {
                log.info("version: {}, resource has been init...", version);
                return;
            }
            resourcesService.init();
            log.info("version: {}, resource init finish!", version);
            confListService.save(ConfType.RESOURCE, version, null);

        } catch (Exception e) {
            log.error("init resource error", e);
        }
    }

    private void remoteInit() {
        try {
            ConfList conf = confListService.searchByKey(ConfType.REMOTE_DEBUG_TIMEOUT);
            if (conf != null) {
                log.info("remote conf has been init...");
                return;
            }

            confListService.save(ConfType.REMOTE_DEBUG_TIMEOUT, "480", null);
            log.info("remote conf init finish!");

        } catch (Exception e) {
            log.error("init remote conf error", e);
        }
    }

    private void idleInit() {
        try {
            ConfList conf = confListService.searchByKey(ConfType.IDEL_DEBUG_TIMEOUT);
            if (conf != null) {
                log.info("idle conf has been init...");
                return;
            }

            confListService.save(ConfType.IDEL_DEBUG_TIMEOUT, "480", null);
            log.info("idle conf init finish!");

        } catch (Exception e) {
            log.error("init idle conf error", e);
        }
    }

}
