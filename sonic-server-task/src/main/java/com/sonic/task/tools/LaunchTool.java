package com.sonic.task.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class LaunchTool implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(LaunchTool.class);
    @Autowired
    private EntityManager manager;

    @Override
    public void run(ApplicationArguments args) {
        String sqlScript = "";
        Query q = manager.createNativeQuery("BEGIN " + sqlScript + " END;");
        q.executeUpdate();
        logger.info("初始化Quartz完毕");
    }
}
