/*
 *  Copyright (C) [SonicCloudOrg] Sonic Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.cloud.sonic.controller.config.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceSchemaCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 清除历史遗留的外键
 *
 * @author JayWenStar, Eason
 * @date 2021/12/26 1:39 上午
 */
@Component
@Slf4j
public class ClearForeignKey implements ApplicationListener<DataSourceSchemaCreatedEvent> {

    @Override
    public void onApplicationEvent(DataSourceSchemaCreatedEvent event) {
//        DataSource dataSource = (DataSource) event.getSource();
//        String dataBase = "";
//        String findFKSql = "SELECT CONCAT('ALTER TABLE ', TABLE_NAME,' DROP FOREIGN KEY ',CONSTRAINT_NAME) as ddl " +
//                "FROM information_schema.TABLE_CONSTRAINTS c " +
//                "WHERE c.TABLE_SCHEMA='%s' AND c.CONSTRAINT_TYPE='FOREIGN KEY'";
//        String transSql = "UPDATE QRTZ_JOB_DETAILS set JOB_CLASS_NAME='org.cloud.sonic.controller.quartz.QuartzJob'";
//        List<String> deleteSqlList = new ArrayList<>();
//        try (Connection connection = dataSource.getConnection()) {
//            try (Statement statement = connection.createStatement()) {
//
//                // 获取当前数据库名
//                ResultSet resultSet1 = statement.executeQuery("select DATABASE() db");
//                if (resultSet1.next()) {
//                    dataBase = resultSet1.getString("db");
//                }
//
//                // 查询所有外键索引，并拼装成删除sql
//                ResultSet resultSet2 = statement.executeQuery(String.format(findFKSql, dataBase));
//                while (resultSet2.next()) {
//                    deleteSqlList.add(resultSet2.getString("ddl"));
//                }
//
//                // 版本兼容sql
//                Boolean resultSet3 = statement.execute(String.format(transSql, dataBase));
//                if (!resultSet3) {
//                    log.info(String.format("transfer sql %s failed, ignore...", transSql));
//                }
//
//                // 执行删除外键sql
//                for (String deleteSql : deleteSqlList) {
//                    statement.executeUpdate(deleteSql);
//                }
//
//                // 删除 test_suites_devices 表的主键
//                try {
//                    statement.executeUpdate("Alter table test_suites_devices Drop primary key");
//                } catch (Exception e) {
//                    // 无视错误
//                }
//            }
//        } catch (Exception e) {
//            log.error("clear foreign key failed.");
//            e.printStackTrace();
//        }
    }
}
