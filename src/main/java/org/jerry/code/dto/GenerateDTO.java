package org.jerry.code.dto;

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
     * 表名
     */
    private String tableName;

    /**
     * 生成数量
     */
    private Integer analogNumber;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 动态字段
     */
    private List<DynamicItemDTO> dynamicItem;
}
