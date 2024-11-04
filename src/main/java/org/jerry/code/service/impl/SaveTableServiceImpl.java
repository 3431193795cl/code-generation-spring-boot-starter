package org.jerry.code.service.impl;

import org.jerry.code.config.CodeGenerationProperties;
import org.jerry.code.config.TableEntityDTO;
import org.jerry.code.service.ISaveTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    /**
     * 根据给定的表名和列信息生成创建表的DDL语句
     *
     * @param tableName 表名
     * @param columns   表的列信息，每个列包含列名、列键、是否为主键和列注释等信息
     * @return 创建表的DDL语句
     */
    private String generateDDL(String tableName, List<TableEntityDTO> columns) {
        // 初始化DDL语句构建器
        StringBuilder ddl = new StringBuilder();
        // 开始拼接创建表语句，包括表名
        ddl.append("CREATE TABLE ").append(tableName).append(" (\n");

        // 遍历列信息，构建每个列的定义
        for (int i = 0; i < columns.size(); i++) {
            // 获取当前列信息
            TableEntityDTO column = columns.get(i);
            // 拼接列名和列键
            ddl.append("\t").append(column.getColumnName()).append(" ");
            // 如果当前列是主键，则添加PRIMARY KEY和NOT NULL约束
            if (column.getIsPk()) {
                ddl.append(" PRIMARY KEY");
                ddl.append(" NOT NULL");
            } else {
                // 如果不是主键，则允许为空
                ddl.append(" NULL");
            }
            // 添加列的注释
            ddl.append(" COMMENT '").append(column.getColumnComment()).append("'");
            // 如果不是最后一个列，则添加逗号分隔
            if (i < columns.size() - 1) {
                ddl.append(",");
            }
            // 每个列定义后换行
            ddl.append("\n");
        }
        // 完成表定义，添加字符集信息
        ddl.append(") charset = utf8;");
        // 返回生成的DDL语句
        return ddl.toString();
    }


    /**
     * 根据表名和列信息生成INSERT INTO SQL语句
     *
     * @param tableName 表名
     * @param columns   表列信息的列表
     * @return 生成的INSERT INTO SQL语句
     */
    private String generateSQL(String tableName, List<TableEntityDTO> columns) {
        // 使用StringBuilder来拼接SQL语句，提高效率
        StringBuilder sql = new StringBuilder();
        // 开始拼接SQL语句，首先添加INSERT INTO关键字和表名
        sql.append("INSERT INTO ").append(tableName).append(" (");
        // 遍历列信息列表，拼接列名
        for (int i = 0; i < columns.size(); i++) {
            // 获取当前列信息
            TableEntityDTO column = columns.get(i);
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
            TableEntityDTO column = columns.get(i);
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
