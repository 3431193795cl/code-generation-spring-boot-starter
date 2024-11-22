package org.jerry.code.toolkit.tool;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLTool implements Serializable {
    private static final long serialVersionUID = 1L;

    private SQLTool(){}

    public static String getSQLType(String sql) {
        sql = sql.trim().toUpperCase();
        if (sql.startsWith("SELECT") || sql.startsWith("WITH")) {
            return "DQL"; // Data Query Language
        } else if (sql.startsWith("INSERT") || sql.startsWith("UPDATE") || sql.startsWith("DELETE")) {
            return "DML"; // Data Manipulation Language
        } else if (sql.startsWith("CREATE") || sql.startsWith("ALTER") || sql.startsWith("DROP")) {
            return "DDL"; // Data Definition Language
        } else {
            return "Unknown";
        }
    }

    public static String extractTableName(String sql) {
        // 正则表达式匹配 FROM 关键字后跟一个或多个空格，然后是表名（假设表名不包含特殊字符）
        Pattern pattern = Pattern.compile("FROM\\s+([^\\s]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            return matcher.group(1); // 返回第一个捕获组的内容，即表名
        }
        return null; // 如果没有找到匹配项，返回null
    }
}
