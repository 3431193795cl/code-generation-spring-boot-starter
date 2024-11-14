package org.jerry.code.toolkit.tool;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestTool {
    private RequestTool() {
    }

    /**
     * 将参数数组转换为JSON字符串表示形式。
     *
     * @param paramsArray 输入的对象数组
     * @return 字符串形式的JSON表示
     */
    public static String argsArrayToString(Object[] paramsArray) {
        // 初始化StringBuilder用于构建JSON字符串
        StringBuilder params = new StringBuilder();
        // 检查数组是否非空且长度大于0
        if (paramsArray != null && paramsArray.length > 0) {
            // 遍历数组中的每个对象
            for (Object o : paramsArray) {
                // 忽略null对象
                if (o != null) {
                    try {
                        // 将对象转换为JSON格式，并追加到StringBuilder中
                        Object jsonObj = JSON.toJSON(o);
                        params.append(jsonObj).append(" ");
                    } catch (Exception e) {
                        // 使用日志框架记录异常信息
                        log.error("Error converting object to JSON: {}", e.getMessage());
                    }
                }
            }
        }
        // 返回转换后的JSON字符串，去除尾部空格
        return params.toString().trim();
    }

    /**
     * 转换异常信息为字符串
     * @param exceptionName 异常名称
     * @param exceptionMessage 异常消息
     * @param elements 堆栈跟踪元素
     * @param maxLength 最大长度
     * @return 字符串形式的异常信息
     */
    public static String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements, int maxLength) {
        // 检查参数是否为null，确保异常信息的完整性和准确性
        if (elements == null || exceptionName == null || exceptionMessage == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        // 使用StringBuilder高效拼接堆栈跟踪信息
        StringBuilder strbuff = new StringBuilder();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet).append("\n");
        }

        // 使用StringBuilder进行高效的字符串拼接
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(exceptionName)
                .append(":")
                .append(exceptionMessage)
                .append("\n\t")
                .append(strbuff);

        // 将拼接好的异常信息转换为字符串，并截取到指定的最大长度
        String message = messageBuilder.toString();
        message = substring(message, 0, maxLength);
        return message;
    }



    /**
     * 截取字符串
     * 此方法用于截取字符串的一个子串
     * 如果结束位置超过源字符串的长度，则截取到字符串末尾
     *
     * @param s 源字符串
     * @param start 开始位置
     * @param end 结束位置
     * @return 截取后的字符串
     */
    private static String substring(String s, int start, int end) {
        // 检查结束位置是否超过字符串长度
        if (s.length() > end) {
            // 如果结束位置没有超过字符串长度，则按照指定范围截取子串
            return s.substring(start, end);
        } else {
            // 如果结束位置超过字符串长度，则从开始位置截取到字符串末尾
            return s.substring(start);
        }
    }


}
