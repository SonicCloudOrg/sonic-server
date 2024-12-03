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
package org.cloud.sonic.controller.config.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.mapper.AlertRobotsMigrateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @deprecated 保留此类一段时间，在ActableConfig完成之后执行，
 * 如果发现旧的robot_token配置数据，则自动迁移到新的表结构中，并清空robot_token，
 * 表结构保留原字段需要暂时保留以免ActableConfig后数据丢失，
 * 后续完成过渡后可直接删除此类。
 * 此过程需在ActableConfig完成后才可进行，
 * Order等注解某些打包方式可能出现不生效问题，通过直接依赖config确保执行顺序
 */
@Configuration
@Slf4j
@Deprecated
public class RobotConfigMigrate {
    public RobotConfigMigrate(
            @Autowired AlertRobotsMigrateMapper robotsMapper,
            @Autowired ActableConfig config
    ) {
        if (null == config) return;
        int n;
        if ((n = robotsMapper.migrateProjectRobot()) > 0) {
            log.warn("legacy project robot found! migrated to {} alert robots", n);
            robotsMapper.clearProjectRobot();
        }
        if ((n = robotsMapper.migrateAgentRobot()) > 0) {
            log.warn("legacy project robot found! migrated to {} alert robots", n);
            robotsMapper.clearAgentRobot();
        }
    }
}
