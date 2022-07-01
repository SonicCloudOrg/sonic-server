package org.cloud.sonic.common.spring;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.cloud.sonic.common.config.DataBaseProperties;
import org.cloud.sonic.common.exception.SonicException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Objects;

/**
 * @link https://github.com/apache/incubator-shenyu/blob/master/shenyu-admin/src/main/java/org/apache/shenyu/admin/spring/LocalDataSourceLoader.java
 * @author sinsy
 * @date 2022-06-24
 */
@Component
@Slf4j
@ConditionalOnExpression("'${sonic.database.dialect}' == 'mysql' or '${sonic.database.dialect}' == 'h2'")
public class LocalDataSourceLoader implements InstantiationAwareBeanPostProcessor {

    private static final String DELIMITER = ";";


    private static final String SQL_INSERT_REGEX = "\\s+";

    @Resource
    private DataBaseProperties dataBaseProperties;

    @Override
    public Object postProcessAfterInitialization(@NonNull final Object bean, String beanName) throws BeansException {

        if (bean instanceof DataSourceProperties) {
            this.init((DataSourceProperties) bean);
        }

        return bean;
    }

    protected void init(final DataSourceProperties properties) {
        try {
            String jdbcUrl = StringUtils.replace(properties.getUrl(), "/sonic?", "?");

            Connection connection = DriverManager.getConnection(jdbcUrl, properties.getUsername(), properties.getPassword());
            execute(connection, dataBaseProperties.getInitScript());
        } catch (Exception e) {
            log.error("Datasource init error.", e);
            throw new SonicException(e.getMessage());
        }


    }

    protected void execute(final Connection conn, final String script) throws Exception {
        ScriptRunner runner = new ScriptRunner(conn);
        try {
            // doesn't print logger
            runner.setLogWriter(null);
            runner.setAutoCommit(true);
            runner.setFullLineDelimiter(false);
            runner.setDelimiter(DELIMITER);
            runner.setSendFullScript(false);
            runner.setStopOnError(false);
            Resources.setCharset(StandardCharsets.UTF_8);
            List<String> initScripts = Splitter.on(";").splitToList(script);
            for (String sqlScript : initScripts) {
                Reader reader = toSqlFile(sqlScript);
                log.info("execute sonic schema sql: {}", sqlScript);
                runner.runScript(reader);
            }
        } finally {
            runner.closeConnection();
        }
    }

    private Reader toSqlFile(final String sqlScript) throws IOException {
        final BufferedReader reader;
        final StringBuilder builder = new StringBuilder();
        reader = new BufferedReader(Resources.getResourceAsReader(sqlScript));
        String str;

        while (Objects.nonNull(str = reader.readLine())) {
            str = str.trim().replaceAll(SQL_INSERT_REGEX, " ");
            builder.append(str).append(System.lineSeparator());
        }
        reader.close();
        return new StringReader(builder.toString());
    }


}
