package org.jerry.code.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mifmif.common.regex.Generex;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.jerry.code.config.CodeGenerationProperties;
import org.jerry.code.constant.SimulationType;
import org.jerry.code.constant.SimulationValue;
import org.jerry.code.dto.DynamicItemDTO;
import org.jerry.code.dto.GenerateDTO;
import org.jerry.code.service.ISaveTableService;
import org.jerry.code.tool.DDLAnalysisTool;
import org.jerry.code.tool.RandDataTool;
import org.jerry.code.tool.RandomIDGenerator;
import org.jerry.code.vo.DMLVo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SaveTableServiceImpl implements ISaveTableService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private CodeGenerationProperties conf;


    @Override
    public Boolean saveTable(String tableName) {
        log.info(tableName);
        return null;
    }

    @Override
    public DMLVo generateDML(GenerateDTO generateDTO) {
        JSONArray jsonArray = new JSONArray();
        JSONArray headList = new JSONArray();
        StringBuffer dml = new StringBuffer("```sql\n");
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
                if (j < generateDTO.getDynamicItem().size() - 1) {
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
                if (y < generateDTO.getDynamicItem().size() - 1) {
                    dml.append(",");
                } else {
                    dml.append(");\n");
                }
            }
            jsonArray.add(jsonObject);
        }
        dml.append("```");
        DMLVo dmlVo = new DMLVo();
        dmlVo.setDml(dml.toString());
        dmlVo.setDmlList(jsonArray);
        dmlVo.setHeadDmlList(headList);
//        dmlVo.setDmlJson("```json\n"+ JSON.toJSONString(jsonArray)+"\n```");
        dmlVo.setDmlJson(JSON.toJSONString(jsonArray));
        return dmlVo;
    }

    @Override
    public String generateDDL(GenerateDTO generateDTO) {
        StringBuffer ddl = new StringBuffer("```sql\n");
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
                ddl.append(" DEFAULT '" + column.getFieldDefault() + "'");
            }
            // 如果当前列不允许为空，则添加NOT NULL约束
            if (column.getIsNull()) {
                ddl.append(" NOT NULL");
            } else {
                ddl.append(" NULL");
            }
            // 如果当前列有更新时的行为，则添加ON UPDATE约束
            if (!StringUtils.isEmpty(column.getFieldOnUpdate())) {
                ddl.append(" ON UPDATE '" + column.getFieldOnUpdate() + "'");
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
        ddl.append(") COMMENT '" + generateDTO.getTableComment() + "' charset = utf8;\n```");
        // 完成表定义，添加字
        return ddl.toString();
    }


    @Override
    public JSONObject parsingSql(String sqlDdl) {

        return new JSONObject();
    }


    public static void main(String[] args) {
        String sql = "--用户信息\n" +
                "CREATE TABLE IF NOT EXISTS user_info (\n" +
                "\tid varchar(30) NOT NULL PRIMARY KEY COMMENT '主键',\n" +
                "\tcreate_by varchar(30) NULL COMMENT '创建人',\n" +
                "\tcreate_time datetime DEFAULT 'CURRENT_TIMESTAMP' NOT NULL COMMENT '创建时间',\n" +
                "\tupdate_by varchar(30) NULL COMMENT '更新人',\n" +
                "\tupdate_time datetime DEFAULT 'CURRENT_TIMESTAMP' NOT NULL ON UPDATE 'CURRENT_TIMESTAMP' COMMENT '更新时间',\n" +
                "\tis_deleted int DEFAULT '0' NOT NULL COMMENT '是否删除：(0:未删 1：删除 )',\n" +
                "\tuser_name varchar(30) NULL COMMENT '用户姓名',\n" +
                "\taddress varchar(225) NULL COMMENT '用户住址'\n" +
                ") COMMENT = '用户信息' charset = utf8;";
        try {
            getCreateTableInfo(sql);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据SQL语句获取表名和建表参数
     *
     * @param sql sql语句
     * @return
     * @throws JSQLParserException
     */
    private static GenerateDTO getCreateTableInfo(String sql) throws JSQLParserException {
        GenerateDTO generateDTO = new GenerateDTO();
        CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(sql);
        Table table = createTable.getTable();
        System.out.println("Table Name: " + table.getName());
        generateDTO.setTableName(table.getName());
        String tableComment = DDLAnalysisTool.getTableComment(createTable);
        generateDTO.setTableName(tableComment);
        List<DynamicItemDTO> dynamicItemDTOS = new ArrayList<>();
        List<ColumnDefinition> list = createTable.getColumnDefinitions();
        for (ColumnDefinition d : list) {
            DynamicItemDTO dynamicItemDTO = new DynamicItemDTO();
            dynamicItemDTO.setColumnName(d.getColumnName());
            String length = DDLAnalysisTool.getFieldLength(d);
            dynamicItemDTO.setFieldType(d.getColDataType().getDataType() + length);
            System.out.println("列名:" + d.getColumnName() + "  ,列类型:" + d.getColDataType().getDataType() + "  长度:"
                    + length);
            // 获取列的注释
            String comment = DDLAnalysisTool.getFieldAnnotate(d.getColumnSpecs());
            String def = DDLAnalysisTool.getFieldDefault(d.getColumnSpecs());
            System.out.println("Comment: " + comment);
            System.out.println("def: " + def);
            dynamicItemDTO.setFieldAnnotate(comment);
            dynamicItemDTO.setFieldDefault(def);
        }
        return generateDTO;
    }

    /**
     * 检查指定名称的表是否存在
     *
     * @param tableName 要检查的表名称
     * @return 如果表存在返回true，否则返回false
     */
    private Boolean isTableExist(String tableName) {
        // 定义查询语句，用于查询指定名称的表是否存在
        String sql = "SHOW TABLES LIKE ?";
        try {
            // 使用JdbcTemplate执行查询，并通过ResultSetExtractor提取结果
            return jdbcTemplate.query(sql, new Object[]{tableName}, new ResultSetExtractor<Boolean>() {
                @Override
                public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
                    // 如果查询结果集有下一行，说明表存在，返回true，否则返回false
                    return rs.next();
                }
            });
        } catch (Exception e) {
            // 记录异常信息
            System.err.println("Error checking table existence: " + e.getMessage());
            throw e; // 或者可以根据具体情况进行处理
        }
    }


}
