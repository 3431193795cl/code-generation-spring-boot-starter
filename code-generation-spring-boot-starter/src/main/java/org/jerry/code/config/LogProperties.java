package org.jerry.code.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "generation")
public class LogProperties {

    /**
     * 是否开启
     */
    private Boolean enable = false;


}
