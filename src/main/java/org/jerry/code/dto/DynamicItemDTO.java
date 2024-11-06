package org.jerry.code.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DynamicItemDTO implements Serializable {

    private static final long serialVersionUID = 7614682531768492747L;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 默认值
     */
    private String fieldDefault;

    /**
     * 注释
     */
    private String fieldAnnotate;

    /**
     * 更新时间
     */
    private String fieldOnUpdate;

    /**
     * 是否为空
     */
    private Boolean isNull;

    /**
     * 是否主键
     */
    private Boolean primaryKey;

    /**
     * 是否自增
     */
    private Boolean increment;

    /**
     * 模拟数据类型
     */
    private Integer simulationType;

    /**
     * 模拟数据值
     */
    private String simulationValue;

}
