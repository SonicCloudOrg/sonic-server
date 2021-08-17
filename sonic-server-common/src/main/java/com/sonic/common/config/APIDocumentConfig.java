package com.sonic.common.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Arrays;
import java.util.List;

/**
 * @author ZhouYiXun
 * @des swagger配置
 * @date 2021/8/15 18:26
 */
@Configuration
@EnableSwagger2WebMvc
public class APIDocumentConfig {
    //从配置文档获取接口文档信息
    @Value("${spring.version}")
    private String version;
    @Value("${knife4j.info.title}")
    private String title;
    @Value("${knife4j.info.description}")
    private String description;
    @Value("${knife4j.setting.enableHostText}")
    private String host;

    private final OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * @param openApiExtensionResolver
     * @return 开启knife4j接口插件
     * @author ZhouYiXun
     * @des
     * @date 2021/8/15 23:22
     */
    @Autowired
    public APIDocumentConfig(OpenApiExtensionResolver openApiExtensionResolver) {
        this.openApiExtensionResolver = openApiExtensionResolver;
    }

    /**
     * @return springfox.documentation.spring.web.plugins.Docket
     * @author ZhouYiXun
     * @des 生成最终文档的配置，默认所有路径
     * @date 2021/8/15 22:55
     */
    @Bean
    public Docket createRestAPIDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildSettingExtensions())
                .useDefaultResponseMessages(false)
                .securitySchemes(Arrays.asList(
                        new ApiKey("sonicToken", "sonicToken", "header")))
                .securityContexts(securityContexts());
    }

    /**
     * @return java.util.List<springfox.documentation.spi.service.contexts.SecurityContext>
     * @author ZhouYiXun
     * @des 放开Login的token校验
     * @date 2021/8/15 22:53
     */
    private List<SecurityContext> securityContexts() {
        return Arrays.asList(
                SecurityContext.builder()
                        .securityReferences(auth())
                        .forPaths(PathSelectors.regex("^((?!(login)).)*$"))
                        .build()
        );
    }

    /**
     * @return java.util.List<springfox.documentation.service.SecurityReference>
     * @author ZhouYiXun
     * @des 设置sonicToken在接口文档页面
     * @date 2021/8/15 22:54
     */
    private List<SecurityReference> auth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(
                new SecurityReference("sonicToken", authorizationScopes));
    }

    /**
     * @return springfox.documentation.service.ApiInfo
     * @author ZhouYiXun
     * @des 接口文档信息，从配置文件获取
     * @date 2021/8/15 22:54
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .contact(new Contact("ZhouYiXun",
                        "https://github.com/ZhouYixun/sonic-server", "zyx291028775@qq.com"))
                .version(version)
                .description(description)
                .licenseUrl("https://github.com/ZhouYixun/sonic-server/blob/main/LICENSE")
                .build();
    }
}