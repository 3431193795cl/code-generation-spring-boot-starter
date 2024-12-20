package org.jerry.code.business.dao;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jerry.code.toolkit.tool.DDLAnalysisTool;
import org.jerry.code.toolkit.tool.SQLTool;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class OperateTableDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_DATABASE = "SELECT DATABASE()";

    private static final String SELECT_TABLES = "show tables";

    private static final String SELECT_TABLE_INFO =
            "SELECT " +
                    "TABLE_NAME AS tableName, " +
                    "TABLE_COMMENT AS tableComment, " +
                    "TABLE_COLLATION AS tableCollation " +
                    "FROM INFORMATION_SCHEMA.TABLES " +
                    "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";

    private static final String SELECT_TABLE_COLUMNS =
            "SELECT " +
                    "COLUMN_NAME AS columnName, " +
                    "COLUMN_TYPE AS  columnType, " +
                    "COLUMN_COMMENT AS columnComment," +
                    " COLUMN_DEFAULT AS columnDefault, " +
                    "IS_NULLABLE AS isNullable, " +
                    "COLUMN_KEY AS columnKey, " +
                    "EXTRA AS extra " +
                    "FROM INFORMATION_SCHEMA.COLUMNS  " +
                    "WHERE  TABLE_SCHEMA = ? AND TABLE_NAME = ? " +
                    "ORDER BY ORDINAL_POSITION;";

    public JSONArray selectTables() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("label", jdbcTemplate.queryForObject(SELECT_DATABASE, String.class));
        jsonObject.put("children", jdbcTemplate.queryForList(SELECT_TABLES, String.class).stream().map(e -> {
            JSONObject json = new JSONObject();
            json.put("label", e);
            return json;
        }).collect(Collectors.toList()));
        jsonArray.add(jsonObject);
        return jsonArray;
    }

    public String selectTableDDL(String tableName) {
        try {
            String schemaName = jdbcTemplate.queryForObject(SELECT_DATABASE, String.class);
            Map<String, Object> tableInfo = jdbcTemplate.queryForMap(SELECT_TABLE_INFO, schemaName, tableName);
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(SELECT_TABLE_COLUMNS, schemaName, tableName);
            log.info("tableInfo:{},columns:{}", JSON.toJSONString(tableInfo), JSON.toJSONString(columns));
            return DDLAnalysisTool.generateDDL(tableInfo, columns);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public Boolean deleteTable(String tableName) {
        int updated = jdbcTemplate.update("drop table " + tableName);
        return updated == 0;
    }

    public Object runSql(String sql) {
        switch (SQLTool.getSQLType(sql)) {
            case "DQL":
                //获取表头
                String tableName = SQLTool.extractTableName(sql);
                String schemaName = jdbcTemplate.queryForObject(SELECT_DATABASE, String.class);
                List<Map<String, Object>> columns = jdbcTemplate.queryForList(SELECT_TABLE_COLUMNS, schemaName, tableName);
                List<JSONObject> columnNames = columns.stream().map(e -> {
                    JSONObject json = new JSONObject();
                    json.put("label", e.get("columnName"));
                    json.put("prop", e.get("columnName"));
                    return json;
                }).collect(Collectors.toList());
                //获取内容
                List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
                return new JSONObject() {{
                    put("columnNames", columnNames);
                    put("data", result);
                }};
            case "DML":
                return jdbcTemplate.update(sql);
            case "DDL":
                try {
                    jdbcTemplate.execute(sql);
                    return true;
                } catch (Exception e) {
                    return e.getMessage();
                }
            default:
                return null;
        }
    }
}
