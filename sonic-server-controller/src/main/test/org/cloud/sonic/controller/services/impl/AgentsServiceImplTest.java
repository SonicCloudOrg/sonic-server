package org.cloud.sonic.controller.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cloud.sonic.controller.mapper.AgentsMapper;
import org.cloud.sonic.controller.models.domain.Agents;
import org.cloud.sonic.controller.models.interfaces.AgentStatus;
import org.cloud.sonic.controller.services.AgentsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AgentsServiceImplTest {

    @Autowired
    private AgentsServiceImpl agentsService;




    @Test
    void saveAgents() {

        Agents agents = new Agents();
        agents.setName("test");
        agents.setStatus(AgentStatus.ONLINE);
        agents.setHost("127.0.0.1");
        agents.setPort(8080);
        agents.setVersion("5.0.0");
        agents.setSystemType("1");
        Assertions.assertTrue(agentsService.save(agents));

    }

    @Test
    void findAgents() {
        Assertions.assertNotNull(agentsService.findAgents());
    }

    @Test
    @Transactional
    void updateName() {
    }

    @Test
    void resetDevice() {
    }



    @Test
    void testSaveAgents() {
    }

    @Test
    void updateAgentsByLockVersion() {
    }

    @Test
    void offLine() {
    }

    @Test
    void testOffLine() {
    }

    @Test
    void auth() {
    }

    @Test
    void findKeyById() {
    }

    @Test
    void findById() {
    }

    @Test
    void findBySecretKey() {
    }

    @Test
    void findByCabinetIdAndStorey() {
    }

    @Test
    void findByCabinetForDetail() {
    }

    @Test
    void findByCabinetId() {
    }
}