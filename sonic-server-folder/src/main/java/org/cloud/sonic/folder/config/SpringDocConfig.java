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
package org.cloud.sonic.folder.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Value("${spring.version}")
    private String version;

    @Bean
    public OpenAPI springDocOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Controller REST API")
                        .version(version)
                        .contact(new Contact().name("SonicCloudOrg")
                                .email("soniccloudorg@163.com"))
                        .description("Folder of Sonic Cloud Real Machine Platform")
                        .termsOfService("https://github.com/SonicCloudOrg/sonic-server/blob/main/LICENSE")
                        .license(new License().name("AGPL v3")
                                .url("https://github.com/SonicCloudOrg/sonic-server/blob/main/LICENSE")));
    }
}
