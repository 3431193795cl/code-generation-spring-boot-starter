package org.jerry.code.config;

import lombok.extern.slf4j.Slf4j;
import org.jerry.code.business.controller.OperateTableController;
import org.jerry.code.business.controller.SaveTableController;
import org.jerry.code.business.service.IOperateTableService;
import org.jerry.code.business.service.ISaveTableService;
import org.jerry.code.business.service.impl.OperateTableServiceImpl;
import org.jerry.code.business.service.impl.SaveTableServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Slf4j
@Configuration
@ConditionalOnClass(SaveTableController.class)
@EnableConfigurationProperties(CodeGenerationProperties.class)//ioc注入
public class CodeGenerationAutoConfigure implements WebMvcConfigurer {

    @Resource
    private CodeGenerationProperties dbScanClass;

    @Resource
    private Environment environment;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
        dataSourceBuilder.url(environment.getRequiredProperty("spring.datasource.url"));
        dataSourceBuilder.username(environment.getRequiredProperty("spring.datasource.username"));
        dataSourceBuilder.password(environment.getRequiredProperty("spring.datasource.password"));
        return dataSourceBuilder.build();
    }

    @Bean
    public ISaveTableService saveTableService() {
        return new SaveTableServiceImpl();
    }

    @Bean
    public IOperateTableService operateTableService() {
        return new OperateTableServiceImpl();
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
        registry.addViewController("/code/index.html").setViewName("index");
        registry.addViewController("/code/generate.html").setViewName("generate");
        registry.addViewController("/code/data_source.html").setViewName("data_source");
    }

    @Bean
    @ConditionalOnMissingBean
    public SaveTableController saveTableController() {
        return new SaveTableController(saveTableService());
    }

    @Bean
    @ConditionalOnMissingBean
    public OperateTableController operateTableController() {
        return new OperateTableController(operateTableService());
    }

}
