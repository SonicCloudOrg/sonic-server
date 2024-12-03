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
package org.cloud.sonic.controller.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cloud.sonic.controller.models.base.CommentPage;
import org.cloud.sonic.controller.models.domain.AlertRobots;

import java.util.Date;
import java.util.List;

public interface AlertRobotsService extends IService<AlertRobots> {
    String SCENE_AGENT = "agent";
    String SCENE_TESTSUITE = "testsuite";
    String SCENE_SUMMARY = "summary";

    CommentPage<AlertRobots> findRobots(Page<AlertRobots> page, Integer projectId, String scene);

    List<AlertRobots> findAllRobots(Integer projectId, String scene);

    void sendResultFinishReport(int suitId, String suiteName, int pass, int warn, int fail, int projectId, int resultId);

    void sendProjectReportMessage(int projectId, String projectName, Date startDate, Date endDate, boolean isWeekly, int pass, int warn, int fail);

    void sendErrorDevice(int agentId, int errorType, int tem, String udId);

    String getDefaultNoticeTemplate(int type, String scene);
}
