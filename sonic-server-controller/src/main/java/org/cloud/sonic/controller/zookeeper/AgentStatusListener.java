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
package org.cloud.sonic.controller.zookeeper;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.cloud.sonic.common.models.domain.Agents;
import org.cloud.sonic.common.services.AgentsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注入的报错无视就好，实际上是能找到的
 *
 * @author JayWenStar
 * @date 2022/4/10 12:20 上午
 */
@Configuration
@Slf4j
public class AgentStatusListener {

    @Bean
    public boolean agentsStatusListener(CuratorFramework curatorFramework, AgentsService agentsService) {
        CuratorCache curatorCache = CuratorCache.build(curatorFramework, "/sonic-agent");
        curatorCache.listenable().addListener((type, oldData, data) -> {
            if (CuratorCacheListener.Type.NODE_DELETED != type) {
                return;
            }
            String agentJson = new String(oldData.getData());
            Agents agents = JSON.parseObject(agentJson, Agents.class);
            agentsService.offLine(agents);
        });
        curatorCache.start();
        log.info("start watch /sonic-agent");
        return true;
    }


}
