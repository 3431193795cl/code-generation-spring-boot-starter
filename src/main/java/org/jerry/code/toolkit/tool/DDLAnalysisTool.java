package org.jerry.code.toolkit.tool;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

public class DDLAnalysisTool implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DDL_FIELD_NOT = "NOT";

    private static final String DDL_FIELD_NULL = "NULL";

    private static final String DDL_AUTO_INCREMENT = "AUTO_INCREMENT";

    private static final String DDL_FIELD_PRIMARY = "PRIMARY";

    private static final String DDL_FIELD_KEY = "KEY";

    private DDLAnalysisTool() {
    }

    /**
     * 获取表注释
     *
     * @param createTable
     * @return
     */
    public static String getTableComment(CreateTable createTable) {
        List<String> stringList = createTable.getTableOptionsStrings();
        String tableComment = null;
        for (int i = 0; i < stringList.size(); i++) {
            String spec = stringList.get(i);
            if (spec.toUpperCase().startsWith("COMMENT")) {
                tableComment = stringList.get(i + 1);
                int start = tableComment.indexOf('\'');
                int end = tableComment.lastIndexOf('\'');
                if (start != -1 && end != -1 && start < end) {
                    tableComment = tableComment.substring(start + 1, end);
                } else {
                    tableComment = ""; // 或者其他默认值
                }
            }
        }
        return tableComment;
    }

    /**
     * 获取字段注释
     *
     * @param columnSpecs
     * @return
     */
    public static String getFieldAnnotate(List<String> columnSpecs) {
        String comment = null;
        for (int i = 0; i < columnSpecs.size(); i++) {
            String spec = columnSpecs.get(i);
            if (spec.toUpperCase().startsWith("COMMENT")) {
                comment = columnSpecs.get(columnSpecs.size() - 1);
                int start = comment.indexOf('\'');
                int end = comment.lastIndexOf('\'');
                if (start != -1 && end != -1 && start < end) {
                    comment = comment.substring(start + 1, end);
                } else {
                    comment = ""; // 或者其他默认值
                }
            }
        }
        return comment;
    }

    /**
     * 获取字段默认值
     *
     * @param columnSpecs
     * @return
     */
    public static String getFieldDefault(List<String> columnSpecs) {
        String defaultValue = null;
        for (int i = 0; i < columnSpecs.size(); i++) {
            String spec = columnSpecs.get(i);
            if (spec.toUpperCase().startsWith("DEFAULT")) {
                defaultValue = columnSpecs.get(i + 1);
                int start = defaultValue.indexOf('\'');
                int end = defaultValue.lastIndexOf('\'');
                if (start != -1 && end != -1 && start < end) {
                    defaultValue = defaultValue.substring(start + 1, end);
                } else {
                    defaultValue = ""; // 或者其他默认值
                }
            }
        }
        return defaultValue;
    }

    /**
     * 获取字段长度
     *
     * @param columnDefinition
     * @return
     */
    public static String getFieldLength(ColumnDefinition columnDefinition) {
        String length = null;
        if (!CollectionUtils.isEmpty(columnDefinition.getColDataType().getArgumentsStringList())) {
            // 使用 String.join() 方法将列表转换为以逗号分隔的字符串
            length = "(" + String.join(", ", columnDefinition.getColDataType().getArgumentsStringList()) + ")";
        }
        return length;
    }

    /**
     * 获取字段是否为空
     *
     * @param columnSpecs
     * @return
     */
    public static Boolean getFieldIsNull(List<String> columnSpecs) {
        Boolean isNull = false;
        if (columnSpecs.contains(DDL_FIELD_NOT) && columnSpecs.contains(DDL_FIELD_NULL)) {
            isNull = true;
        } else if (columnSpecs.contains(DDL_FIELD_NULL)) {
            isNull = false;
        }
        return isNull;
    }

    /**
     * 获取字段自动更新值
     *
     * @param columnSpecs
     * @return
     */
    public static String getFieldOnUpdate(List<String> columnSpecs) {
        String onUpdate = null;
        if (columnSpecs.contains("ON") && columnSpecs.contains("UPDATE")) {
            for (int i = 0; i < columnSpecs.size(); i++) {
                String spec = columnSpecs.get(i);
                if (spec.toUpperCase().startsWith("UPDATE")) {
                    onUpdate = columnSpecs.get(i + 1);
                    int start = onUpdate.indexOf('\'');
                    int end = onUpdate.lastIndexOf('\'');
                    if (start != -1 && end != -1 && start < end) {
                        onUpdate = onUpdate.substring(start + 1, end);
                    } else {
                        onUpdate = ""; // 或者其他默认值
                    }
                }
            }
        }
        return onUpdate;
    }

    /**
     * 获取字段是否自增
     * @param columnSpecs
     * @return
     */
    public static Boolean getFieldAutoIncrement(List<String> columnSpecs) {
        return columnSpecs.contains(DDL_AUTO_INCREMENT);
    }

    /**
     * 获取字段是否为主键
     * @param columnSpecs
     * @return
     */
    public static Boolean getFieldPrimaryKey(List<String> columnSpecs) {
        Boolean isPrimaryKey = false;
        if (columnSpecs.contains(DDL_FIELD_PRIMARY) && columnSpecs.contains(DDL_FIELD_KEY)) {
            isPrimaryKey = true;
        }
        return isPrimaryKey;
    }
}
