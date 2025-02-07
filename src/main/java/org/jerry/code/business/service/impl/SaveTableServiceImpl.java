package org.jerry.code.business.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mifmif.common.regex.Generex;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.jerry.code.business.dto.GenerateCodeDTO;
import org.jerry.code.business.dto.TableInfoDTO;
import org.jerry.code.business.service.IOperateTableService;
import org.jerry.code.config.CodeGenerationProperties;
import org.jerry.code.toolkit.constant.SimulationType;
import org.jerry.code.toolkit.constant.SimulationValue;
import org.jerry.code.business.dto.DynamicItemDTO;
import org.jerry.code.business.dto.GenerateDTO;
import org.jerry.code.business.service.ISaveTableService;
import org.jerry.code.toolkit.tool.*;
import org.jerry.code.business.vo.DMLVo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SaveTableServiceImpl implements ISaveTableService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private FreemarkerTool freemarkerTool;

    @Override
    public Boolean saveTable(String tableName) {
        log.info(tableName);
        try {
            jdbcTemplate.execute(tableName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public DMLVo generateDML(GenerateDTO generateDTO) {
        JSONArray jsonArray = new JSONArray();
        JSONArray headList = new JSONArray();
        StringBuffer dml = new StringBuffer("");
        for (int j = 0; j < generateDTO.getDynamicItem().size(); j++) {
            DynamicItemDTO itemDTO = generateDTO.getDynamicItem().get(j);
            JSONObject head = new JSONObject();
            if (itemDTO.getSimulationType().equals(SimulationType.noSimulation.getCode())) {
                continue;
            }
            head.put("prop", itemDTO.getColumnName());
            head.put("label", itemDTO.getFieldAnnotate() + "(" + (itemDTO.getColumnName()) + ")");
            headList.add(head);
        }
        for (int i = 0; i < generateDTO.getAnalogNumber(); i++) {
            JSONObject jsonObject = new JSONObject();
            dml.append("INSERT INTO ").append(generateDTO.getTableName()).append(" (");
            for (int j = 0; j < generateDTO.getDynamicItem().size(); j++) {
                DynamicItemDTO itemDTO = generateDTO.getDynamicItem().get(j);
                if (itemDTO.getSimulationType().equals(SimulationType.noSimulation.getCode())) {
                    continue;
                }

                dml.append(itemDTO.getColumnName());
                // 如果不是最后一个列，则添加逗号分隔
                if (j == generateDTO.getDynamicItem().size() - 1) {
                    dml.append(",");
                }
            }
            dml.append(") VALUE (");
            for (int y = 0; y < generateDTO.getDynamicItem().size(); y++) {
                DynamicItemDTO itemDTO = generateDTO.getDynamicItem().get(y);
                if (itemDTO.getSimulationType().equals(SimulationType.noSimulation.getCode())) {
                    continue;
                }
                switch (Objects.requireNonNull(SimulationType.getByCode(itemDTO.getSimulationType()))) {
                    case fixed:
                        jsonObject.put(itemDTO.getColumnName(), itemDTO.getSimulationValue());
                        dml.append("'").append(itemDTO.getSimulationValue()).append("'");
                        break;
                    case random:
                        switch (SimulationValue.getByCode(itemDTO.getSimulationValue())) {
                            case name:
                                String name = RandDataTool.name();
                                jsonObject.put(itemDTO.getColumnName(), name);
                                dml.append("'" + name + "'");
                                break;
                            case adderess:
                                String road = RandDataTool.getRoad();
                                jsonObject.put(itemDTO.getColumnName(), road);
                                dml.append("'" + road + "'");
                                break;
                            case idCard:
                                String idCard = RandomIDGenerator.generateRandomID();
                                jsonObject.put(itemDTO.getColumnName(), idCard);
                                dml.append("'" + idCard + "'");
                                break;
                            case email:
                                String email = RandDataTool.email(9, 17);
                                jsonObject.put(itemDTO.getColumnName(), email);
                                dml.append("'" + email + "'");
                                break;
                            case phone:
                                String tel = RandDataTool.tel();
                                jsonObject.put(itemDTO.getColumnName(), tel);
                                dml.append("'" + tel + "'");
                                break;
                            case garbage:
                                String gar = RandDataTool.getRandomHanZiNoSpace(30);
                                jsonObject.put(itemDTO.getColumnName(), gar);
                                dml.append("'" + gar + "'");
                                break;
                        }
                        break;
                    case incremental:
                        int incremental = Integer.parseInt(itemDTO.getSimulationValue()) + i;
                        jsonObject.put(itemDTO.getColumnName(), incremental);
                        dml.append("'" + incremental + "'");
                        break;
                    case rule:
                        Generex generex = new Generex(itemDTO.getSimulationValue());
                        String randomed = generex.random();
                        jsonObject.put(itemDTO.getColumnName(), randomed);
                        dml.append("'" + randomed + "'");
                }
                // 如果不是最后一个列，则添加逗号分隔
                if (y == generateDTO.getDynamicItem().size() - 1) {
                    dml.append(",");
                } else {
                    dml.append(");\n");
                }
            }
            jsonArray.add(jsonObject);
        }
        DMLVo dmlVo = new DMLVo();
        dmlVo.setDml(dml.toString());
        dmlVo.setDmlList(jsonArray);
        dmlVo.setHeadDmlList(headList);
        dmlVo.setDmlJson(JSON.toJSONString(jsonArray));
        return dmlVo;
    }

    @Override
    public String generateDDL(GenerateDTO generateDTO) {
        StringBuffer ddl = new StringBuffer("");
        ddl.append("--" + generateDTO.getTableComment() + "\n");
        // 开始拼接创建表语句，包括表名
        ddl.append("CREATE TABLE IF NOT EXISTS " + generateDTO.getTableName() + " (\n");
        // 遍历列信息，构建每个列的定义
        for (int i = 0; i < generateDTO.getDynamicItem().size(); i++) {
            // 获取当前列信息
            DynamicItemDTO column = generateDTO.getDynamicItem().get(i);
            // 拼接列名和列键
            ddl.append("\t" + column.getColumnName() + " " + column.getFieldType());
            // 如果当前列有默认值，则添加DEFAULT约束
            if (!StringUtils.isEmpty(column.getFieldDefault())) {
                ddl.append(" DEFAULT " + column.getFieldDefault());
            }
            // 如果当前列不允许为空，则添加NOT NULL约束
            if (column.getIsNull()) {
                ddl.append(" NOT NULL");
            } else {
                ddl.append(" NULL");
            }
            // 如果当前列不允许为空，则添加NOT NULL约束
            if (column.getIncrement()) {
                ddl.append(" AUTO_INCREMENT ");
            }
            // 如果当前列有更新时的行为，则添加ON UPDATE约束
            if (!StringUtils.isEmpty(column.getFieldOnUpdate())) {
                ddl.append(" ON UPDATE " + column.getFieldOnUpdate());
            }
            // 如果当前列是主键，则添加PRIMARY KEY约束
            if (column.getPrimaryKey()) {
                ddl.append(" PRIMARY KEY");
            }
            // 添加列的注释
            ddl.append(" COMMENT '" + column.getFieldAnnotate() + "'");
            // 如果不是最后一个列，则添加逗号分隔
            if (i < generateDTO.getDynamicItem().size() - 1) {
                ddl.append(",");
            }
            // 每个列定义后换行
            ddl.append("\n");
        }
        ddl.append(") COMMENT '" + generateDTO.getTableComment() + "' charset = utf8;");
        // 完成表定义，添加字
        return ddl.toString();
    }


    @Override
    public GenerateDTO parsingSql(String sqlDdl) {
        try {
            JSONObject jsonObject = JSON.parseObject(sqlDdl);
            return getCreateTableInfo(jsonObject.getString("sql"));
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject getPackageInfo() {
        //版本信息
        String version = "1.0.1";
        // 获取Java版本
        String javaVersion = System.getProperty("java.version");
        // 获取JVM名称
        String jvmName = System.getProperty("java.vm.name");
        // 获取Classpath路径
        String classPath = StringTool.wrapTextBySymbol(System.getProperty("java.class.path"), ';');
        // 操作系统名称
        String osName = System.getProperty("os.name");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", version);
        jsonObject.put("java_version", javaVersion);
        jsonObject.put("jvm_name", jvmName);
        jsonObject.put("class_path", classPath);
        jsonObject.put("os_name", osName);
        return jsonObject;
    }


    /**
     * 根据SQL语句获取表名和建表参数
     *
     * @param sql sql语句
     * @return
     * @throws JSQLParserException
     */
    private GenerateDTO getCreateTableInfo(String sql) throws JSQLParserException {
        GenerateDTO generateDTO = new GenerateDTO();
        CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(sql);
        Table table = createTable.getTable();
        String tableComment = DDLAnalysisTool.getTableComment(createTable);
        System.out.println("表名: " + table.getName() + "，表注释：" + tableComment);
        List<DynamicItemDTO> dynamicItemDTOS = new ArrayList<>();
        List<ColumnDefinition> list = createTable.getColumnDefinitions();
        generateDTO.setTableName(table.getName());
        generateDTO.setTableComment(tableComment);
        for (ColumnDefinition columnDefinition : list) {

            String length = DDLAnalysisTool.getFieldLength(columnDefinition);
            String comment = DDLAnalysisTool.getFieldAnnotate(columnDefinition.getColumnSpecs());
            String def = DDLAnalysisTool.getFieldDefault(columnDefinition.getColumnSpecs());
            Boolean isNull = DDLAnalysisTool.getFieldIsNull(columnDefinition.getColumnSpecs());
            String onUpdate = DDLAnalysisTool.getFieldOnUpdate(columnDefinition.getColumnSpecs());
            Boolean autoIncrement = DDLAnalysisTool.getFieldAutoIncrement(columnDefinition.getColumnSpecs());
            Boolean primaryKey = DDLAnalysisTool.getFieldPrimaryKey(columnDefinition.getColumnSpecs());

            DynamicItemDTO dynamicItemDTO = new DynamicItemDTO();
            dynamicItemDTO.setColumnName(columnDefinition.getColumnName());
            dynamicItemDTO.setFieldType(StringUtils.isEmpty(length) ? columnDefinition.getColDataType().getDataType() : columnDefinition.getColDataType().getDataType() + length);
            dynamicItemDTO.setFieldAnnotate(comment);
            dynamicItemDTO.setFieldDefault(def);
            dynamicItemDTO.setIsNull(isNull);
            dynamicItemDTO.setIncrement(autoIncrement);
            dynamicItemDTO.setFieldOnUpdate(onUpdate);
            dynamicItemDTO.setPrimaryKey(primaryKey);

            dynamicItemDTOS.add(dynamicItemDTO);
        }
        generateDTO.setDynamicItem(dynamicItemDTOS);
        return generateDTO;
    }

    @Override
    public Map<String, String> generateCode(GenerateCodeDTO generateCodeDTO) {
        log.info("generateCodeDTO:{}", JSON.toJSONString(generateCodeDTO));
        if (StringUtils.isEmpty(generateCodeDTO.getTableSql())) {
            throw new RuntimeException("请输入建表语句");
        }
        //1.表结构解析
        GenerateDTO generateDTO = TableParseTool.processTableIntoGenerateDTO(generateCodeDTO);

        //2.Set the params 设置表格参数

        generateCodeDTO.getOptions().setClassInfo(generateDTO);
        generateCodeDTO.getOptions().setTableName(generateDTO == null ? String.valueOf(System.currentTimeMillis()) : generateDTO.getTableName());

        //3.generate the code by freemarker templates with parameters . Freemarker根据参数和模板生成代码\
        try {
            return getResultByParams(generateCodeDTO.getOptions());
        } catch (Exception e) {
            log.error("generateCode error:{}", e.getMessage(), e);
            return null;
        }
    }

    public Map<String, String> getResultByParams(TableInfoDTO params) throws IOException, TemplateException {
        Map<String, String> result = new HashMap<>(32);
        result.put("tableName", params.getTableName());
        JSONArray parentTemplates = JSONArray.parseArray(getTemplateConfig());
        for (int i = 0; i < parentTemplates.size(); i++) {
            JSONObject parentTemplateObj = parentTemplates.getJSONObject(i);
            for (int x = 0; x < parentTemplateObj.getJSONArray("templates").size(); x++) {
                JSONObject childTemplate = parentTemplateObj.getJSONArray("templates").getJSONObject(x);
                result.put(childTemplate.getString("name"), freemarkerTool.generateFromTemplate(parentTemplateObj.getString("group") + "/" + childTemplate.getString("name") + ".ftl", JSON.parseObject(JSON.toJSONString(params), Map.class)));
            }
        }
        return result;
    }

    public String getTemplateConfig() throws IOException {
        String templateCpnfig = null;
        if (templateCpnfig != null) {
        } else {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template.json");
            templateCpnfig = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            inputStream.close();
        }
        //log.info(JSON.toJSONString(templateCpnfig));
        return templateCpnfig;
    }
}
