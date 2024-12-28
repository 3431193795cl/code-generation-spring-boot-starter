package org.jerry.code.toolkit.tool;

import java.io.Serializable;

public class StringTool implements Serializable {
    private static final long serialVersionUID = 1L;

    private StringTool(){}

    /**
     * 字符串自动换行
     * @param text 原字符串
     * @param maxWidth 每行最大宽度
     * @return
     */
    public static String wrapText(String text, int maxWidth) {
        StringBuilder wrappedText = new StringBuilder();
        int length = text.length();
        int start = 0;

        while (start < length) {
            // 如果剩余的字符数小于或等于最大宽度，直接添加到结果中
            if (start + maxWidth >= length) {
                wrappedText.append(text.substring(start));
                break;
            } else {
                // 否则，找到最后一个空格的位置作为换行点
                int end = start + maxWidth;
                while (end > start && text.charAt(end) != ' ') {
                    end--;
                }
                if (end == start) { // 如果没有找到空格，则强制在maxWidth处换行
                    end = start + maxWidth;
                }
                wrappedText.append(text.substring(start, end)).append(System.lineSeparator());
                start = end + 1; // 更新起始位置到下一个单词的开始
            }
        }
        return wrappedText.toString();
    }

    public static String wrapTextBySymbol(String text, char delimiter) {
        StringBuilder wrappedText = new StringBuilder();
        String[] parts = text.split(Character.toString(delimiter));

        for (int i = 0; i < parts.length; i++) {
            wrappedText.append(parts[i]);
            if (i < parts.length - 1) {
                wrappedText.append(delimiter).append(System.lineSeparator());
            }
        }
        return wrappedText.toString();
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String upperCaseFirst(String str) {
        if (str == null || str.trim().isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public static String lowerCaseFirst(String str) {
        //2019-2-10 解决StringUtils.lowerCaseFirst潜在的NPE异常@liutf
        return (str != null && str.length() > 1) ? str.substring(0, 1).toLowerCase() + str.substring(1) : "";
    }

    /**
     * 下划线，转换为驼峰式
     *
     * @param underscoreName
     * @return
     */
    public static String underlineToCamelCase(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.trim().length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ('_' == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 转 user_name 风格
     *
     * 不管原始是什么风格
     */
    public static String toUnderline(String str, boolean upperCase) {
        if (str == null || str.trim().isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        boolean preIsUnderscore = false;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '_') {
                preIsUnderscore = true;
            } else if (ch == '-') {
                ch = '_';
                preIsUnderscore = true; // -A -> _a
            } else if (ch >= 'A' && ch <= 'Z') {
                // A -> _a
                if (!preIsUnderscore && i > 0) { // _A -> _a
                    result.append("_");
                }
                preIsUnderscore = false;
            } else {
                preIsUnderscore = false;
            }
            result.append(upperCase ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
        }

        return result.toString();
    }

    /**
     * any str ==> lowerCamel
     */
    public static String toLowerCamel(String str) {
        if (str == null || str.trim().isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        char pre = '\0';
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '-' || ch == '—' || ch == '_') {
                ch = '_';
                pre = ch;
                continue;
            }
            char ch2 = ch;
            if (pre == '_') {
                ch2 = Character.toUpperCase(ch);
                pre = ch2;
            } else if (pre >= 'A' && pre <= 'Z') {
                pre = ch;
                ch2 = Character.toLowerCase(ch);
            } else {
                pre = ch;
            }
            result.append(ch2);
        }

        return lowerCaseFirst(result.toString());
    }

    public static boolean isNotNull(String str) {
        return org.apache.commons.lang3.StringUtils.isNotEmpty(str);
    }
}
