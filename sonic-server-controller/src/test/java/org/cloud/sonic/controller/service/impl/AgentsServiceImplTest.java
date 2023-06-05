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
package org.cloud.sonic.controller.service.impl;

import org.cloud.sonic.controller.mapper.AgentsMapper;
import org.cloud.sonic.controller.models.domain.Agents;
import org.cloud.sonic.controller.models.interfaces.AgentStatus;
import org.cloud.sonic.controller.models.interfaces.RobotType;
import org.cloud.sonic.controller.services.impl.AgentsServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AgentsServiceImplTest {

    @InjectMocks
    private AgentsServiceImpl agentsService;

    @Mock
    private AgentsMapper agentsMapper;

    @Test
    public void testFindAgents() {
        Agents agents = new Agents()
                .setRobotSecret("world")
                .setHasHub(0)
                .setId(1)
                .setStatus(AgentStatus.ONLINE)
                .setRobotToken("hello")
                .setHighTemp(45)
                .setHighTempTime(5000)
                .setRobotType(RobotType.DingTalk)
                .setHost("192.168.1.1")
                .setName("Agent Name")
                .setPort(7777)
                .setSecretKey("xxxxx")
                .setSystemType("windows")
                .setVersion("v2.x");

        Mockito.when(agentsMapper.selectList(Mockito.any()))
                .thenReturn(Arrays.asList(agents));

        List<Agents> list = agentsService.findAgents();

        Assert.assertEquals(1, list.size());
        Assert.assertEquals(agents, list.get(0));
    }

    @Test
    public void testUpdate() {
        int id = 0;
        String name = "test";
        int highTemp = 0;
        int highTempTime = 0;
        int robotType = RobotType.DingTalk;
        String robotToken = "hello";
        String robotSecret = "world";

        Agents agents = new Agents()
                .setRobotSecret(robotSecret)
                .setHasHub(0)
                .setId(id)
                .setStatus(AgentStatus.OFFLINE)
                .setRobotToken(robotToken)
                .setHighTemp(highTemp)
                .setHighTempTime(highTempTime)
                .setRobotType(RobotType.DingTalk)
                .setHost("unknown")
                .setName(name)
                .setPort(0)
                .setSecretKey(Mockito.any())
                .setSystemType("unknown")
                .setVersion("unknown");

        Mockito.when(agentsMapper.insert(agents))
                .thenReturn(1);

        agentsService.update(id, name, highTemp, highTempTime, robotType, robotToken, robotSecret, null);

    }


}
