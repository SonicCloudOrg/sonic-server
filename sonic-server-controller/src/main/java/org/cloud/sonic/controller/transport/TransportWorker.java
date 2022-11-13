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
package org.cloud.sonic.controller.transport;

import com.alibaba.fastjson.JSONObject;
import org.cloud.sonic.controller.tools.SpringTool;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TransportWorker {
    private static DiscoveryClient discoveryClient = SpringTool.getBean(DiscoveryClient.class);
    private static RestTemplate restTemplate = SpringTool.getBean(RestTemplate.class);

    public static void send(int agentId, JSONObject jsonObject) {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("sonic-server-controller");
        for (ServiceInstance i : serviceInstanceList) {
            restTemplate.postForEntity(
                    String.format("http://%s:%d/exchange/send?id=%d", i.getHost(), i.getPort(), agentId),
                    jsonObject, JSONObject.class);
        }
    }
}
