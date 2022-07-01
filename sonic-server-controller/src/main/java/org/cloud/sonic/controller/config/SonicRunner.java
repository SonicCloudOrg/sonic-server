package org.cloud.sonic.controller.config;

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.models.domain.ConfList;
import org.cloud.sonic.controller.models.interfaces.ConfType;
import org.cloud.sonic.controller.services.ConfListService;
import org.cloud.sonic.controller.services.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class SonicRunner implements ApplicationRunner {

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private ConfListService confListService;

    @Value("${spring.version}")
    private String version;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        resourceInit();
    }

    /**
     * 每次启动对信息进行版本对比，不一致进行一起更新
     */
    private void resourceInit() {
        try {

            ConfList conf = confListService.searchByKey(ConfType.RESOURCE);
            if (conf != null && Objects.equals(conf.getContent(), version)) {
                log.info("version: {},resource has been init...", version);
                return;
            }
            resourcesService.init();
            log.info("version: {}, resource  init finish!", version);
            confListService.save(ConfType.RESOURCE, version, null);

        }catch (Exception e) {
            log.error("init resource error", e);
        }
    }
}
