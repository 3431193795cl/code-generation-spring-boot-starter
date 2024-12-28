package org.jerry.code.toolkit.tool;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

import static sun.jvm.hotspot.oops.OopUtilities.escapeString;

@Slf4j
@Component
public class FreemarkerTool implements Serializable {

    private static final long serialVersionUID = -4206024041219227546L;

    private Configuration configuration;
    private final ResourceLoader resourceLoader;



    public FreemarkerTool() {
        try {
            //创建配置实例
            configuration = new Configuration(Configuration.VERSION_2_3_31);
            //设置编码
            configuration.setDefaultEncoding("utf-8");
            //ftl模板文件
            configuration.setClassForTemplateLoading(this.getClass(), "/templates/code-generate");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.resourceLoader = new org.springframework.core.io.DefaultResourceLoader();
    }

    public String generateFromTemplate(String templatePath, Map<String, Object> dataModel) throws IOException, TemplateException {
        //获取模板
        Template template = configuration.getTemplate(templatePath);
        return processTemplateIntoString(template, dataModel);
    }

    /**
     * process Template Into String
     *
     * @param template
     * @param model
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public static String processTemplateIntoString(Template template, Object model)
            throws IOException, TemplateException {
        StringWriter result = new StringWriter();
        template.process(model, result);
        return result.toString();
    }

}
