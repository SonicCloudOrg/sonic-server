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
package org.cloud.sonic.controller.tools;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

@MappedTypes(int[].class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class NullableIntArrayTypeHandler implements TypeHandler<int[]> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, int[] param, JdbcType jdbcType) throws SQLException {
        if (null == param) {
            preparedStatement.setNull(i, jdbcType.TYPE_CODE);
        } else {
            preparedStatement.setString(i, Arrays.stream(param).mapToObj(Integer::toString).collect(Collectors.joining(",")));
        }
    }

    @Override
    public int[] getResult(ResultSet resultSet, String s) throws SQLException {
        return parseString(resultSet.getString(s));
    }

    @Override
    public int[] getResult(ResultSet resultSet, int i) throws SQLException {
        return parseString(resultSet.getString(i));
    }

    @Override
    public int[] getResult(CallableStatement callableStatement, int i) throws SQLException {
        return parseString(callableStatement.getString(i));
    }

    private int[] parseString(String ret) {
        if (ret == null) return null;
        try {
            return Arrays.stream(ret.split(",", 0)).mapToInt(Integer::valueOf).toArray();
        } catch (NumberFormatException e) {
            return new int[0];
        }
    }
}
