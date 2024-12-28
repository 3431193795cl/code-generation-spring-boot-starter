<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.demo.mapper.${classInfo.className}Mapper">
    <resultMap id="BaseResultMap" type="${packageName}.entity.${classInfo.className}">
        <#list classInfo.dynamicItem as fieldItem>
                <result column="${fieldItem.columnName}" property="${fieldItem.fieldName}" />
        </#list>
    </resultMap>

    <sql id="Base_Column_List">
        <#list classInfo.dynamicItem as fieldItem>
             ${fieldItem.fieldName}<#if fieldItem_has_next>,</#if>
        </#list>
    </sql>

    <insert id="insert" useGeneratedKeys="true" keyColumn="id" keyProperty="id" parameterType="${packageName}.entity.${classInfo.className}">
        INSERT INTO ${classInfo.originTableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0>
                <#list classInfo.dynamicItem as fieldItem>
                    <if test="null != ${fieldItem.columnName} ">
                     ${fieldItem.fieldName}<#if fieldItem_has_next>,</#if>
                    ${r"</if>"}
                </#list>
            </#if>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0>
                <#list classInfo.dynamicItem as fieldItem>
                    <if test="null != ${fieldItem.columnName}">
                       ${r"#{"}${fieldItem.fieldName}${r"}"}<#if fieldItem_has_next>,</#if>
                   ${r"</if>"}
                </#list>
            </#if>
        </trim>
    </insert>

    <delete id="delete" >
        delete from ${classInfo.originTableName} where ${classInfo.tableName}_id=${r"#{id}"}
    </delete>

    <update id="update" parameterType="${packageName}.entity.${classInfo.className}">
        UPDATE ${classInfo.originTableName}
        <set>
            <#list classInfo.dynamicItem as fieldItem >
                <#if fieldItem.columnName != "id" && fieldItem.columnName != "AddTime" && fieldItem.columnName != "UpdateTime" >
                    <if test="null != ${fieldItem.fieldName} <#if fieldItem.fieldClass ="String">and '' != ${fieldItem.fieldName}</#if>">${fieldItem.columnName} = ${r"#{"}${fieldItem.fieldName}${r"}"}<#if fieldItem_has_next>,</#if>${r"</if>"}
                </#if>
            </#list>
        </set>
        WHERE id = ${r"#{"}id${r"}"}
    </update>

    <select id="pageList" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List" />
        FROM ${classInfo.originTableName}
        LIMIT ${r"#{offset}"}, ${r"#{pageSize}"}
    </select>

    <select id="pageListCount" resultType="java.lang.Integer">
        SELECT count(1)
        FROM ${classInfo.originTableName}
    </select>
</mapper>