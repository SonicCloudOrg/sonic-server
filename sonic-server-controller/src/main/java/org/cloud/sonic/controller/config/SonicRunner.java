package org.cloud.sonic.controller.config;

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.services.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SonicRunner implements ApplicationRunner {

    @Autowired
    private ResourcesService resourcesService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        resourceInit();
    }

    /**
     * 首次启动对项目里面的 url 资源进行初始化
     */
    private void resourceInit() {
        try {
            if (resourcesService.count() == 0) {
                resourcesService.init();
                log.info("resource init finish!");
            }else {
                log.info("resource has been init...");
            }

        }catch (Exception e) {
            log.error("init resource error", e);
        }
    }
}
