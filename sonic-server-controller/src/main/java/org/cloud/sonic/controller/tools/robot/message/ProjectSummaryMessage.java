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
package org.cloud.sonic.controller.tools.robot.message;

import lombok.AllArgsConstructor;
import org.cloud.sonic.controller.tools.robot.Message;

import java.util.Date;

@AllArgsConstructor
public class ProjectSummaryMessage extends Message {
    public int projectId;
    public String projectName;
    public Date startDate;
    public Date endDate;
    public int pass;
    public int warn;
    public int fail;
    public double rate;
    public int total;
    public String url;
    public boolean isWeekly;
}
