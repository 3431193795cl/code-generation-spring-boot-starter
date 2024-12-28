<#if isWithPackage?exists && isWithPackage==true>package ${packageName}.dao;</#if>

<#if isAutoImport?exists && isAutoImport==true>
import ${packageName}.entity.${classInfo.className};
import java.util.List;
</#if>
/**
 * @description ${classInfo.className}Mapper
 * @author ${author}
 * @date ${.now?date}
 */
public interface I${classInfo.className}DAO {

    int add(${classInfo.className} userInfo);

    int update(${classInfo.className} userInfo);

    int delete(int id);

    ${classInfo.className} findById(int id);

    List<${classInfo.className}> findAllList(Map<String,Object> param);

}