package org.jerry.code.config;

import freemarker.template.TemplateExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jerry.code.business.controller.OperateTableController;
import org.jerry.code.business.controller.SaveTableController;
import org.jerry.code.business.dao.OperateTableDao;
import org.jerry.code.business.service.IOperateTableService;
import org.jerry.code.business.service.ISaveTableService;
import org.jerry.code.business.service.impl.OperateTableServiceImpl;
import org.jerry.code.business.service.impl.SaveTableServiceImpl;
import org.jerry.code.toolkit.tool.FreemarkerTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(SaveTableController.class)
@EnableConfigurationProperties(CodeGenerationProperties.class)//ioc注入
public class CodeGenerationAutoConfigure implements WebMvcConfigurer {

    private final CodeGenerationProperties dbScanClass;

    private final Environment environment;

    private final ResourceLoader resourceLoader;


    //    @Value("${code.template.path:classpath*:/templates/code-generate}")
    @Value("classpath:templates/code-generate")
    private Resource resource;


    @PostConstruct
    public void init() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // 或者进行其他处理
            }
        }
    }

    @Bean
    @ConfigurationProperties(prefix = "database")
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
        dataSourceBuilder.url(environment.getRequiredProperty("spring.datasource.url"));
        dataSourceBuilder.username(environment.getRequiredProperty("spring.datasource.username"));
        dataSourceBuilder.password(environment.getRequiredProperty("spring.datasource.password"));
        return dataSourceBuilder.build();
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
    public FreemarkerTool freemarkerTool() {
        return new FreemarkerTool();
    }

    // 注册dao

    @Bean
    public OperateTableDao operateTableDao() {
        return new OperateTableDao();
    }


    // 注册service

    @Bean
    public ISaveTableService saveTableService() {
        return new SaveTableServiceImpl();
    }

    @Bean
    public IOperateTableService operateTableService() {
        return new OperateTableServiceImpl();
    }


    // 注册controller

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
