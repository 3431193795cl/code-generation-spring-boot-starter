package org.jerry.code.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mifmif.common.regex.Generex;
import org.jerry.code.config.CodeGenerationProperties;
import org.jerry.code.constant.SimulationType;
import org.jerry.code.constant.SimulationValue;
import org.jerry.code.dto.DynamicItemDTO;
import org.jerry.code.dto.GenerateDTO;
import org.jerry.code.service.ISaveTableService;
import lombok.extern.slf4j.Slf4j;
import org.jerry.code.tool.RandDataTool;
import org.jerry.code.tool.RandomIDGenerator;
import org.jerry.code.vo.DMLVo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
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
            if (itemDTO.getSimulationType().equals(SimulationType.noSimulation.getCode())){
                continue;
            }
            head.put("prop",itemDTO.getColumnName());
            head.put("label",itemDTO.getFieldAnnotate()+"("+(itemDTO.getColumnName())+")");
            headList.add(head);
        }
        for (int i = 0; i < generateDTO.getAnalogNumber(); i++) {
            JSONObject jsonObject = new JSONObject();
            dml.append("INSERT INTO ").append(generateDTO.getTableName()).append(" (");
            for (int j = 0; j < generateDTO.getDynamicItem().size(); j++) {
                DynamicItemDTO itemDTO = generateDTO.getDynamicItem().get(j);
                if (itemDTO.getSimulationType().equals(SimulationType.noSimulation.getCode())){
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
                if (itemDTO.getSimulationType().equals(SimulationType.noSimulation.getCode())){
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
            // 添加列的注释
            ddl.append(" COMMENT '" + column.getFieldAnnotate() + "'");
            // 如果当前列是主键，则添加PRIMARY KEY约束
            if (column.getPrimaryKey()) {
                ddl.append(" PRIMARY KEY");
            }
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


    /**
     * 根据表名和列信息生成INSERT INTO SQL语句
     *
     * @param tableName 表名
     * @param columns   表列信息的列表
     * @return 生成的INSERT INTO SQL语句
     */
    private String generateSQL(String tableName, List<DynamicItemDTO> columns) {
        // 使用StringBuilder来拼接SQL语句，提高效率
        StringBuilder sql = new StringBuilder();
        // 开始拼接SQL语句，首先添加INSERT INTO关键字和表名
        sql.append("INSERT INTO ").append(tableName).append(" (");
        // 遍历列信息列表，拼接列名
        for (int i = 0; i < columns.size(); i++) {
            // 获取当前列信息
            DynamicItemDTO column = columns.get(i);
            // 拼接列名和列键
            sql.append(" ").append(column.getColumnName());
            // 如果不是最后一个列，添加逗号分隔
            if (i < columns.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(") VALUES (");
        for (int i = 0; i < columns.size(); i++) {
            // 获取当前列信息
            DynamicItemDTO column = columns.get(i);
            // 拼接列值
            sql.append(" ").append("?");
            // 如果不是最后一个列，添加逗号分隔
            if (i < columns.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(");");
        // 返回拼接完成的SQL语句
        return sql.toString();
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
