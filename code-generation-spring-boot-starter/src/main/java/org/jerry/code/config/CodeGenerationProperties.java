package org.jerry.code.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "generation")
public class CodeGenerationProperties {

    /**
     * 是否开启
     */
    private Boolean enable = false;


}
