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
package org.cloud.sonic.controller.services.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.mapper.AlertRobotsMapper;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.AlertRobots;
import org.cloud.sonic.controller.services.AlertRobotsService;
import org.cloud.sonic.controller.services.impl.base.SonicServiceImpl;
import org.cloud.sonic.controller.tools.robot.Message;
import org.cloud.sonic.controller.tools.robot.RobotFactory;
import org.cloud.sonic.controller.tools.robot.message.DeviceMessage;
import org.cloud.sonic.controller.tools.robot.message.ProjectSummaryMessage;
import org.cloud.sonic.controller.tools.robot.message.TestSuiteMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AlertRobotsServiceImpl extends SonicServiceImpl<AlertRobotsMapper, AlertRobots> implements AlertRobotsService {

    @Value("${robot.client.host}")
    private String clientHost;

    @Autowired
    private RobotFactory robotFactory;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public CommentPage<AlertRobots> findRobots(Page<AlertRobots> page, Integer projectId, String scene) {
        return CommentPage.convertFrom(findRobots(projectId, scene).page(page));
    }

    @Override
    public List<AlertRobots> findAllRobots(Integer projectId, String scene) {
        return findRobots(projectId, scene).list();
    }

    private LambdaQueryChainWrapper<AlertRobots> findRobots(Integer projectId, String scene) {
        var query = lambdaQuery();
        query.eq(null != scene, AlertRobots::getScene, scene);
        query.and(null != projectId,
                it -> it.eq(AlertRobots::getProjectId, projectId).or(
                        p -> p.apply("1 = (select global_robot from projects where id = {0})", projectId).isNull(AlertRobots::getProjectId)
                )
        );
        return query;
    }

    @Override
    public void sendResultFinishReport(int suitId, String suiteName, int pass, int warn, int fail, int projectId, int resultId) {
        var robots = baseMapper.computeTestsuiteRobots(suitId);
        if (robots.isEmpty()) return;
        var msg = new TestSuiteMessage(suiteName, pass, warn, fail, projectId, resultId, clientHost + "/Home/" + projectId + "/ResultDetail/" + resultId);
        send(robots, msg);
    }

    @Override
    public void sendProjectReportMessage(int projectId, String projectName, Date startDate, Date endDate, boolean isWeekly, int pass, int warn, int fail) {
        var robots = baseMapper.computeSummaryRobots(projectId);
        if (robots.isEmpty()) return;
        var total = pass + warn + fail;
        var rate = total > 0 ? BigDecimal.valueOf(((float) pass / total) * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0;
        var url = clientHost + "/Home/" + projectId;
        var msg = new ProjectSummaryMessage(projectId, projectName, startDate, endDate, pass, warn, fail, rate, total, url, isWeekly);
        send(robots, msg);
    }

    @Override
    public void sendErrorDevice(int agentId, int errorType, int tem, String udId) {
        var robots = baseMapper.computeAgentRobots(agentId);
        if (robots.isEmpty()) return;
        var msg = new DeviceMessage(errorType, tem, udId);
        send(robots, msg);
    }

    private void send(List<AlertRobots> robots, Message message) {
        for (var robot : robots) {
            try {
                var messenger = robotFactory.getRobotMessenger(robot.getRobotType(), robot.getMuteRule(), message);
                if (messenger == null) continue;
                var template = robot.getTemplate();
                messenger.sendMessage(restTemplate, robot.getRobotToken(), robot.getRobotSecret(), template, message);
            } catch (Exception e) {
                log.warn("send messaget to robot {} failed, skipping", robot, e);
            }
        }
    }

    @Override
    public String getDefaultNoticeTemplate(int type, String scene) {
        var messenger = robotFactory.getRobotMessenger(type);
        return switch (scene) {
            case SCENE_AGENT -> messenger.getDefaultDeviceMessageTemplate().getExpressionString();
            case SCENE_SUMMARY -> messenger.getDefaultProjectSummaryTemplate().getExpressionString();
            case SCENE_TESTSUITE -> messenger.getDefaultTestSuiteTemplate().getExpressionString();
            default -> "";
        };
    }
}
