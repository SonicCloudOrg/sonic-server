package org.cloud.sonic.eureka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

/**
 * @author ZhouYiXun
 * @des 配置需要加密的内容
 * @date 2021/8/19 15:26
 */
@Configuration
public class WebSecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/eureka/**", "/actuator/**");
    }
}