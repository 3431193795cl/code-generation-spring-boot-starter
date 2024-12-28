<#if isWithPackage?exists && isWithPackage==true>package ${packageName}.service.impl;</#if>

<#if isAutoImport?exists && isAutoImport==true>
import java.util.Map;
import java.util.List;
import ${packageName}.entity.${classInfo.className};
import ${packageName}.mapper.${classInfo.className}Mapper;
import ${packageName}.service.${classInfo.className}Service;
import lombok.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
</#if>

/**
 * @description ${classInfo.classComment}
 * @author ${author}
 * @date ${.now?date}
 */
@Slf4j
@Service
public class ${classInfo.className}ServiceImpl implements ${classInfo.className}Service {

    @Autowired
    private ${classInfo.className}Mapper ${classInfo.className?uncap_first}Mapper;

    /**
     * 新增
     */
    public Object insert(${classInfo.className} ${classInfo.className?uncap_first}) {
        return ${classInfo.className?uncap_first}Mapper.insert(${classInfo.className?uncap_first});
    }

   /**
     * 删除
     */
     public Object delete(int id) {
        return ${classInfo.className?uncap_first}Mapper.delete(id);
    }

    /**
     * 更新
     */
     public Object update(${classInfo.className} ${classInfo.className?uncap_first}) {
        return ${classInfo.className?uncap_first}Mapper.update(${classInfo.className?uncap_first});
    }

    /**
     * 根据主键 id 查询
     */
     public ${classInfo.className} load(int id) {
        return ${classInfo.className?uncap_first}Mapper.load(id);
    }

    @Override
    public Map<String,Object> pageList(int offset, int pagesize) {

        List<${classInfo.className}> pageList = ${classInfo.className?uncap_first}Mapper.pageList(offset, pagesize);
        int totalCount = ${classInfo.className?uncap_first}Mapper.pageListCount(offset, pagesize);

        // result
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("pageList", pageList);
        result.put("totalCount", totalCount);

        return result;
    }
}