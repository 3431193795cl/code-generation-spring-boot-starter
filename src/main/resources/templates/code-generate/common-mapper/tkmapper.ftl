<#if isWithPackage?exists && isWithPackage==true>package ${packageName}.mapper;</#if>

<#if isAutoImport?exists && isAutoImport==true>
import tk.mybatis.mapper.common.Mapper;
import org.apache.ibatis.annotations.Mapper;
import ${packageName}.entity.${classInfo.className};
</#if>


/**
 * @description ${classInfo.className}Mapper
 * @author ${author}
 * @date ${.now?date}
 */
@Mapper
public interface ${classInfo.className}Mapper extends Mapper<${classInfo.className}> {

}