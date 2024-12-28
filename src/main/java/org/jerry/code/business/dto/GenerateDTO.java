package org.jerry.code.business.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GenerateDTO implements Serializable {

    private static final long serialVersionUID = 5834621481472374139L;

    /**
     * 数据库名
     */
    private String dataBase;

    /**
     * 原始表名
     */
    private String originTableName;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 类名
     */
    private String className;

    /**
     * 生成数量
     */
    private Integer analogNumber;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 类注释
     */
    private String classComment;

    /**
     * 动态字段
     */
    private List<DynamicItemDTO> dynamicItem;
}
