package org.jerry.code.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TableInfoDTO implements Serializable {

    private static final long serialVersionUID = -309523472704389395L;

    /**
     * 作者
     */
    private String author;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 成功返回
     */
    private String returnSuccess;

    /**
     * 失败返回
     */
    private String returnError;

    /**
     * 忽略前缀
     */
    private String ignorePrefix;

    /**
     * 输入类型
     */
    private Integer inputType;

    /**
     * tinyInt转换
     */
    private Integer tinyIntChange;

    /**
     * date转换
     */
    private Integer dateChange;

    /**
     * 命名类型
     */
    private Integer nameType;

    /**
     * 包装类型
     */
    private Boolean packagingType;

    /**
     * swagger
     */
    private Boolean swaggerUI;

    /**
     * 注释打印
     */
    private Boolean fieldComment;

    /**
     * 自动打包
     */
    private Boolean automaticPackage;

    /**
     * 打包路径
     */
    private Boolean packedPath;

    /**
     * lombok
     */
    private Boolean lombok;


    /**
     * 表信息
     */
    private GenerateDTO classInfo;

    /**
     * 表名
     */
    private String tableName;
}
