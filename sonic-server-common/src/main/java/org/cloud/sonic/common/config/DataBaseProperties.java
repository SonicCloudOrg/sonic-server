package org.cloud.sonic.common.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author sinsy
 * @date 2022-06-27
 */
@Component
@ConfigurationProperties(prefix = "sonic.database")
@Setter
@Getter
public class DataBaseProperties {

    private String dialect;

    private String initScript;

}
