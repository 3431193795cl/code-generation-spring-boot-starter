<#if packagingType?exists && packagingType==true>package ${packageName}.dao.impl;</#if>

<#if automaticPackage?exists && automaticPackage==true>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import ${packageName}.entity.${classInfo.className};
</#if>

/**
* @description ${classInfo.classComment}
* @author ${author}
* @date ${.now?date}
*/
@Repository
public class ${classInfo.className}DaoImpl implements I${classInfo.className}Dao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int add(${classInfo.className} ${classInfo.className?uncap_first}) {
        return jdbcTemplate.update("insert into ${classInfo.originTableName} (<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0><#list classInfo.dynamicItem as fieldItem >${fieldItem.columnName}<#if fieldItem_has_next>,</#if></#list></#if> ) values (<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0><#list classInfo.dynamicItem as fieldItem >?<#if fieldItem_has_next>,</#if></#list></#if> )",
        <#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0><#list classInfo.dynamicItem as fieldItem >${classInfo.className?uncap_first}.get${fieldItem.fieldName?cap_first}()<#if fieldItem_has_next>,</#if></#list></#if>);
    }

    @Override
    public int update(${classInfo.className} ${classInfo.className?uncap_first}) {
        return jdbcTemplate.update("UPDATE  ${classInfo.originTableName}  SET <#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0><#list classInfo.dynamicItem as fieldItem ><#if fieldItem_index gt 0 >${fieldItem.columnName}=?<#if fieldItem_has_next>,</#if></#if></#list></#if>"
            +" where <#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0><#list classInfo.dynamicItem as fieldItem ><#if fieldItem_index = 0>${fieldItem.columnName}=?<#break ></#if></#list></#if>",
        <#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0>
            <#list classInfo.dynamicItem as fieldItem ><#if fieldItem_index gt 0 >${classInfo.className?uncap_first}.get${fieldItem.fieldName?cap_first}(),</#if></#list>
            <#list classInfo.dynamicItem as fieldItem ><#if fieldItem_index = 0 >${classInfo.className?uncap_first}.get${fieldItem.fieldName?cap_first}()</#if></#list>
        </#if>);
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update("DELETE from ${classInfo.originTableName} where <#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0><#list classInfo.dynamicItem as fieldItem ><#if fieldItem_index = 0>${fieldItem.columnName}=?<#break ></#if></#list></#if>",id);
    }

    @Override
    public ${classInfo.className} findById(int id) {
        List<${classInfo.className}> list = jdbcTemplate.query("select * from ${classInfo.originTableName} where <#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0><#list classInfo.dynamicItem as fieldItem ><#if fieldItem_index = 0>${fieldItem.columnName}=?<#break ></#if></#list></#if>", new Object[]{id}, new BeanPropertyRowMapper<${classInfo.className}>(${classInfo.className}.class));
        if(list!=null && !list.isEmpty() ){
            return  list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<${classInfo.className}> findAllList(Map<String,Object> params) {
        List<${classInfo.className}> list = jdbcTemplate.query("select * from ${classInfo.originTableName}", new Object[]{}, new BeanPropertyRowMapper<${classInfo.className}>(${classInfo.className}.class));
        if(list!=null && !list.isEmpty() ){
            return list;
        }else{
            return Collections.emptyList();
        }
    }

}