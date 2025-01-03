package org.jerry.code.toolkit.tool;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MysqlJavaTypeChangeTool implements Serializable {

    private static final long serialVersionUID = 1L;

    private MysqlJavaTypeChangeTool(){}

    public static final Map<String, String> mysqlJavaTypeMap = new HashMap<String, String>();
    public static final  Map<String, String> mysqlSwaggerTypeMap =new HashMap<String, String>();

    static{
        mysqlJavaTypeMap.put("bigint","Long");
        mysqlJavaTypeMap.put("int","Integer");
        mysqlJavaTypeMap.put("tinyint","Integer");
        mysqlJavaTypeMap.put("smallint","Integer");
        mysqlJavaTypeMap.put("mediumint","Integer");
        mysqlJavaTypeMap.put("integer","Integer");
        //小数
        mysqlJavaTypeMap.put("float","Float");
        mysqlJavaTypeMap.put("double","Double");
        mysqlJavaTypeMap.put("decimal","Double");
        //bool
        mysqlJavaTypeMap.put("bit","Boolean");
        //字符串
        mysqlJavaTypeMap.put("char","String");
        mysqlJavaTypeMap.put("varchar","String");
        mysqlJavaTypeMap.put("tinytext","String");
        mysqlJavaTypeMap.put("text","String");
        mysqlJavaTypeMap.put("mediumtext","String");
        mysqlJavaTypeMap.put("longtext","String");
        //日期
        mysqlJavaTypeMap.put("date","Date");
        mysqlJavaTypeMap.put("datetime","Date");
        mysqlJavaTypeMap.put("timestamp","Date");


        mysqlSwaggerTypeMap.put("bigint","integer");
        mysqlSwaggerTypeMap.put("int","integer");
        mysqlSwaggerTypeMap.put("tinyint","integer");
        mysqlSwaggerTypeMap.put("smallint","integer");
        mysqlSwaggerTypeMap.put("mediumint","integer");
        mysqlSwaggerTypeMap.put("integer","integer");
        mysqlSwaggerTypeMap.put("boolean","boolean");
        mysqlSwaggerTypeMap.put("float","number");
        mysqlSwaggerTypeMap.put("double","number");
        mysqlSwaggerTypeMap.put("decimal","Double");
    }

    public static Map<String, String> getMysqlJavaTypeMap() {
        return mysqlJavaTypeMap;
    }

    public static Map<String, String> getMysqlSwaggerTypeMap() {
        return mysqlSwaggerTypeMap;
    }
}
