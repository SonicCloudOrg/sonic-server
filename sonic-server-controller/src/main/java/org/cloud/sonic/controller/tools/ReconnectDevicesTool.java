package org.cloud.sonic.controller.tools;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.Session;
import java.util.Map;

/**
 * @author wunanfang
 * @des 设备重连工具类，服务端重启后，agent可以重新连接设备而无需拔插或者重启agent
 * @date 2022/9/02 19:32
 */
@Component
@Slf4j
public class ReconnectDevicesTool implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // agent与服务端断开后，会以30秒为周期重连服务端，设置为40s，确保agent都连接上了服务端
        Thread.sleep(40 * 1000);

        Map<Integer, Session> agentSessionMap = BytesTool.agentSessionMap;
        if (CollectionUtils.isEmpty(agentSessionMap)){
            log.info("agentMap为空,不执行后续操作");
            return;
        }
        for (Map.Entry<Integer, Session> map : agentSessionMap.entrySet()){
            log.info("agent缓存元素---key: {}, value: {}", map.getKey(), map.getValue());
            Session agentSession = map.getValue();
            if (agentSession != null) {
                JSONObject wakeup = new JSONObject();
                wakeup.put("msg", "reconnect");
                BytesTool.sendText(agentSession, wakeup.toJSONString());
            }
        }
    }
}
