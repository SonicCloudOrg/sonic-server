/**
 * Copyright (C) [SonicCloudOrg] Sonic Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cloud.sonic.controller.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
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
public class AgentStatusListener {

    @Bean
    public boolean agentsStatusListener(CuratorFramework curatorFramework, AgentsService agentsService) {
        CuratorCache curatorCache = CuratorCache.build(curatorFramework, "/sonic-agent");
        curatorCache.listenable().addListener((type, oldData, data) -> {
            if (CuratorCacheListener.Type.NODE_DELETED != type) {
                return;
            }
            String path = oldData.getPath();
            int agentId = Integer.parseInt(path.split("/")[2]);
            System.out.println(path);
            agentsService.offLine(agentId);
        });
        curatorCache.start();
        return true;
    }


}
