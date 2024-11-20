package org.jerry.code.toolkit.tool;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DDLTools implements Serializable {
    private static final long serialVersionUID = 1L;

    private DDLTools() {
    }

    public static String generateDDL(Map<String, Object> tableInfo, List<Map<String, Object>> columnList) {
        StringBuffer ddl = new StringBuffer("");
        ddl.append("CREATE TABLE ").append(tableInfo.get("tableName")).append(" ( \n");
        for (Map<String, Object> column : columnList) {
            ddl.append("\t" + column.get("columnName") + " " + column.get("columnType"));
            // 判断是否有默认值
            if (column.containsKey("columnDefault")) {
                if (column.get("columnDefault") != null) {
                    ddl.append(" DEFAULT " + column.get("columnDefault"));
                }
            }
            // 判断是否为空
            if (column.containsKey("isNullable")) {
                String isNull = column.get("isNullable").toString();
                if ("NO".equals(isNull)) {
                    ddl.append(" NOT NULL");
                } else if ("YES".equals(isNull)) {
                    ddl.append(" NULL");
                }
            }
            // 判断是否自增
            if (column.containsKey("extra")) {
                ddl.append(" " + column.get("extra").toString());
            }
            // 判断是否是主键
            if (column.containsKey("columnKey")) {
                String columnKey = column.get("columnKey").toString();
                if ("PRI".equals(columnKey)) {
                    ddl.append(" PRIMARY KEY");
                }
            }
            // 添加注释
            if (column.containsKey("columnComment")) {
                ddl.append(" COMMENT '" + column.get("columnComment") + "'");
            }
            if (column.equals(columnList.get(columnList.size()-1))){
                ddl.append("\n");
            } else {
                ddl.append(",\n");
            }
        }
        if (tableInfo.containsKey("tableComment")) {
            ddl.append(") ENGINE=InnoDB COMMENT ='" + tableInfo.get("tableComment") + "'");
        }
        if (tableInfo.containsKey("tableCollation")) {
            ddl.append(" charset = utf8;");
        }
        return ddl.toString();
    }
}
