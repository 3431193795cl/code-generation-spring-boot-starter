<#if packagingType?exists && packagingType==true>package ${packageName}.service;</#if>

<#if automaticPackage?exists && automaticPackage==true>
import java.util.Map;
import java.util.List;
import ${packageName}.entity.${classInfo.className};
</#if>

/**
 * @description ${classInfo.classComment}
 * @author ${author}
 * @date ${.now?date}
 */
public interface ${classInfo.className}Service {
    /**
     * 新增
     */
    public Object insert(${classInfo.className} ${classInfo.className?uncap_first});

    /**
     * 删除
     */
    public Object delete(int id);

    /**
     * 更新
     */
    public Object update(${classInfo.className} ${classInfo.className?uncap_first});

    /**
     * 根据主键 id 查询
     */
    public ${classInfo.className} load(int id);

    /**
     * 分页查询
     */
    public Map<String,Object> pageList(int offset, int pagesize);

}