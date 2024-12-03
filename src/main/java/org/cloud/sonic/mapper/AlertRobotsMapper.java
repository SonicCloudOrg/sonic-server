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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cloud.sonic.controller.models.domain.AlertRobots;
import org.springframework.lang.Nullable;

import java.util.List;

public interface AlertRobotsMapper extends BaseMapper<AlertRobots> {
    @Select("""
            select ifnull(alert_robot_ids,(select testsuite_alert_robot_ids from projects p where p.id = ts.project_id))
            as alert_robot_ids from test_suites ts where id = #{suiteId}
            """)
    @Nullable String getIdsForTestsuite(@Param("suiteId") int suiteId);

    @Select("""
            <script>
            select * from alert_robots r where scene = 'testsuite'
            and (project_id is null or project_id = (select project_id from test_suites ts where id = #{suiteId}))
            <if test="ids != null">
              and id in
              <foreach collection="ids.split(',')" item="id" separator="," open="(" close=")">
                #{id}
              </foreach>
            </if>
            </script>
            """)
    List<AlertRobots> listTestsuiteRobotsFromIds(@Param("ids") @Nullable String ids, @Param("suiteId") int suiteId);

    default List<AlertRobots> computeTestsuiteRobots(int suiteId) {
        var ids = getIdsForTestsuite(suiteId);
        if ("".equals(ids)) return List.of();
        return listTestsuiteRobotsFromIds(ids, suiteId);
    }

    @Select("select alert_robot_ids from agents where id = #{agentId}")
    @Nullable String getIdsForAgent(@Param("agentId") int agentId);

    @Select("""
            <script>
            select r.* from alert_robots r where scene = 'agent'
            <if test="ids != null">
              and id in
              <foreach collection="ids.split(',')" item="id" separator="," open="(" close=")">
                #{id}
              </foreach>
            </if>
            </script>""")
    List<AlertRobots> listAgentRobotsFromIds(@Param("ids") @Nullable String ids);

    default List<AlertRobots> computeAgentRobots(int agentId) {
        String ids = getIdsForAgent(agentId);
        if ("".equals(ids)) return List.of();
        return listAgentRobotsFromIds(ids);
    }

}
