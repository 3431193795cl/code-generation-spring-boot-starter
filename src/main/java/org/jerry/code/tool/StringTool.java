package org.jerry.code.tool;

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
}
