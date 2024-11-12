package org.jerry.code.config;

import org.jerry.code.controller.SaveTableController;
import org.jerry.code.service.ISaveTableService;
import org.jerry.code.service.impl.SaveTableServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
@ConditionalOnClass(SaveTableController.class)
@EnableConfigurationProperties(CodeGenerationProperties.class)//ioc注入
public class CodeGenerationAutoConfigure implements WebMvcConfigurer {

    @Resource
    private CodeGenerationProperties dbScanClass;

    @Bean
    public ISaveTableService tableService() {
        return new SaveTableServiceImpl();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp().prefix("/templates/").suffix(".html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/generate").setViewName("generate");
    }

    @Bean
    @ConditionalOnMissingBean
    public SaveTableController saveTableController() {
        return new SaveTableController(tableService());
    }

}
