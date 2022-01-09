package org.cloud.sonic.folder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author ZhouYiXun
 * @des 静态资源重定向配置
 * @date 2021/8/18 20:26
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/keepFiles/**")
                .addResourceLocations("file:///" + System.getProperty("user.dir") + "/keepFiles/");
        registry.addResourceHandler("/imageFiles/**")
                .addResourceLocations("file:///" + System.getProperty("user.dir") + "/imageFiles/");
        registry.addResourceHandler("/recordFiles/**")
                .addResourceLocations("file:///" + System.getProperty("user.dir") + "/recordFiles/");
        registry.addResourceHandler("/logFiles/**")
                .addResourceLocations("file:///" + System.getProperty("user.dir") + "/logFiles/");
        registry.addResourceHandler("/packageFiles/**")
                .addResourceLocations("file:///" + System.getProperty("user.dir") + "/packageFiles/");
        super.addResourceHandlers(registry);
    }
}