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
package org.cloud.sonic.controller.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

/**
 * @see org.cloud.sonic.controller.config.mybatis.RobotConfigMigrate
 * @deprecated
 */
@Deprecated
public interface AlertRobotsMigrateMapper {

    @Insert("""
            INSERT into alert_robots (robot_secret, robot_token, robot_type, project_id, name, scene)
            SELECT robot_secret, robot_token, robot_type, id, project_name, 'summary'
            FROM projects
            WHERE robot_type > 0 and robot_token != ''
            UNION ALL
            SELECT robot_secret, robot_token, robot_type, id, project_name, 'testsuite'
            FROM projects
            WHERE robot_type > 0 and robot_token != ''
            """)
    int migrateProjectRobot();
    @Update("update projects set robot_type = -robot_type where robot_type > 0")
    void clearProjectRobot();

    @Insert("""
            INSERT into alert_robots (robot_secret, robot_token, robot_type, project_id, name, scene)
            SELECT robot_secret, robot_token, robot_type, null, 'agent', 'agent'
            FROM agents
            WHERE robot_type > 0 and robot_token != ''
            """)
    int migrateAgentRobot();

    @Update("update agents set robot_type = -robot_type where robot_type > 0")
    void clearAgentRobot();
}
