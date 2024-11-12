package org.jerry.code.tool;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

public class DDLAnalysisTool implements Serializable {

    private static final long serialVersionUID = 1L;

    private DDLAnalysisTool() {
    }

    /**
     * 获取表注释
     * @param createTable
     * @return
     */
    public static String getTableComment(CreateTable createTable) {
        List<String> stringList = createTable.getTableOptionsStrings();
        String tableComment = null;
        for (int i = 0; i < stringList.size(); i++) {
            String spec = stringList.get(i);
            if (spec.toUpperCase().startsWith("COMMENT")) {
                tableComment = stringList.get(i + 2);
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

    public static String getFieldLength(ColumnDefinition columnDefinition) {
        String length = null;
        if (!CollectionUtils.isEmpty(columnDefinition.getColDataType().getArgumentsStringList())) {
            // 使用 String.join() 方法将列表转换为以逗号分隔的字符串
            length = "(" + String.join(", ", columnDefinition.getColDataType().getArgumentsStringList()) + ")";
        }
        return length;
    }
}
