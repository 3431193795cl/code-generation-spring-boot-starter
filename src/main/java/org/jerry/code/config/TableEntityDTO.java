package org.jerry.code.config;

import lombok.Data;

@Data
public class TableEntityDTO {
    /**
     * 列名类型（数据库类型）
     */
    private String columnType;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列名备注
     */
    private String columnComment;

    /**
     * 列名是否为主键
     */
    private Boolean isPk;

    /**
     * 列名长度
     */
    private Integer columnLength;

    /**
     * 默认值
     */
    private String defaultValue;
}
