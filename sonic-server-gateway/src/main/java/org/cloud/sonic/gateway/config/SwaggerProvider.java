package org.cloud.sonic.gateway.config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> instanceList = Arrays.asList(
                "sonic-server-controller",
                "sonic-server-folder");
        instanceList.forEach(instance -> {
            String url = "/api/" + instance.substring(
                    instance.lastIndexOf("-") + 1) + "/v2/api-docs";
            SwaggerResource swaggerResource = new SwaggerResource();
            swaggerResource.setUrl(url);
            swaggerResource.setName(instance);
            resources.add(swaggerResource);
        });
        return resources;
    }
}
