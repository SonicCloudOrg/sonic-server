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
package org.cloud.sonic.controller.tools.robot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class Message {
    public final Calendar now = Calendar.getInstance();
    public Object ext = null;
    public SimpleDateFormat format = null;

    public SimpleDateFormat getFormat(String pattern) {
        if (null == format) {
            format = new SimpleDateFormat(pattern);
        } else {
            format.applyPattern(pattern);
        }
        return format;
    }

    public SimpleDateFormat getFormat() {
        if (null == format) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return format;
    }

}


