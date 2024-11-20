package org.jerry.code.toolkit.tool;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DDLTools implements Serializable {
    private static final long serialVersionUID = 1L;

    private DDLTools(){}

    public static String generateDDL(Map<String, Object> tableInfo, List<Map<String, Object>> columnList){
        StringBuffer ddl = new StringBuffer("");
        ddl.append("CREATE TABLE ").append(tableInfo.get("tableName")).append(" ( \n");
        for(Map<String, Object> column : columnList){
            ddl.append("\t" + column.get("columnName") + " " + column.get("columnType"));
            // 判断是否有默认值
            if(column.containsKey("columnDefault")){
                ddl.append(" DEFAULT " + column.get("columnDefault"));
            }
            // 判断是否为空
            if(column.containsKey("isNullable")){
                String isNull = column.get("isNullable").toString();
                if ("NO".equals(isNull)){
                    ddl.append(" NOT NULL");
                } else if ("YES".equals(isNull)){
                    ddl.append(" NULL");
                }
            }
            // 判断是否自增

        }
        return ddl.toString();
    }
}
