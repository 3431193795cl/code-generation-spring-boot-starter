package org.jerry.code.config;

import org.jerry.code.service.IOperLogService;
import org.jerry.code.service.impl.OperLogServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
@ConditionalOnClass(value = {IOperLogService.class, OperLogServiceImpl.class})
@EnableConfigurationProperties(CodeGenerationProperties.class)//ioc注入
public class CodeGenerationAutoConfigure implements WebMvcConfigurer {

    @Resource
    private CodeGenerationProperties dbScanClass;

    @Bean
    @ConditionalOnMissingBean
    IOperLogService startService() {
        return new OperLogServiceImpl();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
    }

    @Bean
    public String customBean() {
        return "This is a custom bean";
    }

}
