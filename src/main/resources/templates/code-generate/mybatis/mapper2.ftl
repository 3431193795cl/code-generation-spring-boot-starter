<#if packagingType?exists && packagingType==true>package ${packageName}.mapper;</#if>\

<#if automaticPackage?exists && automaticPackage == true>
import java.util.List;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ${packageName}.entity.${classInfo.className};
</#if>


/**
 * @description ${classInfo.classComment}
 * @author ${author}
 * @date ${.now?date}
 */
 @Mapper
 @Repository
 public interface ${classInfo.className}Mapper {

     /**
      * 新增
      * @author ${author}
      * @date ${.now?string('yyyy/MM/dd')}
      */
      @Options(useGeneratedKeys=true,keyProperty="${classInfo.className?uncap_first}Id")
      @Insert("insert into ${classInfo.originTableName}" +
      " (<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0><#list classInfo.dynamicItem as fieldItem >${fieldItem.columnName}<#if fieldItem_has_next>,</#if></#list></#if>)" +
      " values(<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0><#list classInfo.dynamicItem as fieldItem >${fieldItem.fieldName}<#if fieldItem_has_next>,</#if></#list></#if>))")
     int insert(${classInfo.className} ${classInfo.className?uncap_first});

     /**
     * 刪除
     * @author ${author}
     * @date ${.now?string('yyyy/MM/dd')}
     */
     @Delete(value = "delete from ${classInfo.originTableName} where ${classInfo.tableName}_id=井{${classInfo.className?uncap_first}Id}")
     int delete(int id);

     /**
     * 更新
     * @author ${author}
     * @date ${.now?string('yyyy/MM/dd')}
     */
     @Update(value = "update ${classInfo.tableName} set "
         <#list classInfo.dynamicItem as fieldItem >
             <#if fieldItem.columnName != "id">+" ${fieldItem.columnName}=井{${fieldItem.fieldName}}<#if fieldItem_has_next>,</#if>"</#if>
         </#list>
             +" where ${classInfo.tableName}_id=井{${classInfo.className?uncap_first}Id} ")
     int update(${classInfo.className} ${classInfo.className?uncap_first});

<#--     /**-->
<#--      * 查询 根据主键 id 查询-->
<#--      * @author ${author}-->
<#--      * @date ${.now?string('yyyy/MM/dd')}-->
<#--      */-->
<#--     @Select("select * from ${classInfo.originTableName} where <#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0><#list classInfo.dynamicItem as fieldItem ><#if fieldItem_index = 0>${fieldItem.columnName}=?<#break ></#if></#list></#if> = #{id}")-->
<#--     ${classInfo.className} load(int id);-->

     @Results(value = {
     <#list classInfo.dynamicItem as fieldItem >
         @Result(property = "${fieldItem.fieldName}", column = "${fieldItem.columnName}")<#if fieldItem_has_next>,</#if>
     </#list>
     })
     @Select(value = "select * from ${classInfo.tableName} where ${classInfo.tableName}_id=井{queryParam}")
     ${classInfo.className} selectOne(String queryParam);

     @Results(value = {
     <#list classInfo.dynamicItem as fieldItem >
         @Result(property = "${fieldItem.fieldName}", column = "${fieldItem.columnName}")<#if fieldItem_has_next>,</#if>
     </#list>
     })
     @Select(value = "select * from ${classInfo.tableName} where "
     <#list classInfo.dynamicItem as fieldItem >
         +" ${fieldItem.columnName}=井{${fieldItem.fieldName}}<#if fieldItem_has_next> or </#if>"
     </#list>
     )
     List<${classInfo.className}> selectList(${classInfo.className} ${classInfo.className?uncap_first});

 }