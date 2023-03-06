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
package org.cloud.sonic.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhouYiXun
 * @des swagger配置
 * @date 2021/8/15 18:26
 */
@Configuration
public class APIDocumentConfig {
    @Value("${spring.application.name}")
    private String name;
    @Value("${spring.application.des}")
    private String title;
    @Value("${spring.version}")
    private String version;
    @Value("${knife4j.setting.enableHostText}")
    private String host;

    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            openApi.addExtension("x-test123", "333");
        };
    }
//
//    /**
//     * @param openApiExtensionResolver
//     * @return 开启knife4j接口插件
//     * @author ZhouYiXun
//     * @des
//     * @date 2021/8/15 23:22
//     */
//    @Autowired
//    public APIDocumentConfig(OpenApiExtensionResolver openApiExtensionResolver) {
//        this.openApiExtensionResolver = openApiExtensionResolver;
//    }
//
//    /**
//     * @return springfox.documentation.spring.web.plugins.Docket
//     * @author ZhouYiXun
//     * @des 生成最终文档的配置，默认所有路径
//     * @date 2021/8/15 22:55
//     */
//    @Bean
//    public Docket createRestAPIDocket() {
//        host = host.replace(":80/", "/");
//        return new Docket(DocumentationType.SWAGGER_2)
//                .host(host)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.any())
//                .build()
//                .extensions(openApiExtensionResolver.buildSettingExtensions())
//                .useDefaultResponseMessages(false)
//                .securitySchemes(Arrays.asList(
//                        new ApiKey("SonicToken", "SonicToken", "header")))
//                .securityContexts(securityContexts());
//    }
//
//    /**
//     * @return java.util.List<springfox.documentation.spi.service.contexts.SecurityContext>
//     * @author ZhouYiXun
//     * @des 放开Login的token校验
//     * @date 2021/8/15 22:53
//     */
//    private List<SecurityContext> securityContexts() {
//        return Arrays.asList(
//                SecurityContext..builder()
//                        .securityReferences(auth())
//                        .forPaths(PathSelectors.regex("^((?!(register|login)).)*$"))
//                        .build()
//        );
//    }
//
//    /**
//     * @return java.util.List<springfox.documentation.service.SecurityReference>
//     * @author ZhouYiXun
//     * @des 设置SonicToken在接口文档页面
//     * @date 2021/8/15 22:54
//     */
//    private List<SecurityReference> auth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Arrays.asList(
//                new SecurityReference("SonicToken", authorizationScopes));
//    }
//
//    /**
//     * @return springfox.documentation.service.ApiInfo
//     * @author ZhouYiXun
//     * @des 接口文档信息，从配置文件获取
//     * @date 2021/8/15 22:54
//     */
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title(title + " Open API")
//                .termsOfServiceUrl("Please visit: https://github.com/SonicCloudOrg/sonic-server")
//                .contact(new Contact("SonicCloudOrg  https://github.com/SonicCloudOrg", "", ""))
//                .version(version)
//                .description(name + " service Open API.")
//                .build();
//    }


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("XXX用户系统API")
                        .version("1.0")

                        .description("Knife4j集成springdoc-openapi示例")
                        .termsOfService("http://doc.xiaominfo.com")
                        .license(new License().name("Apache 2.0")
                                .url("http://doc.xiaominfo.com")));
    }
}