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
