package org.cloud.sonic.controller.tools;

import org.cloud.sonic.controller.quartz.QuartzHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class LaunchTool implements ApplicationRunner {
    @Autowired
    private QuartzHandler quartzHandler;

    @Override
    public void run(ApplicationArguments args) {
        quartzHandler.createSysTrigger();
    }
}
