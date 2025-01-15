<#if packagingType?exists && packagingType==true>package ${packageName}.controller;</#if>

<#if automaticPackage?exists && automaticPackage==true>
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
<#if swaggerUI?exists && swaggerUI == true>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>
import ${packageName}.entity.${classInfo.className};
</#if>

/**
 * @description ${classInfo.classComment}
 * @author ${author}
 * @date ${.now?date}
 */
@RestController
<#if swaggerUI?exists && swaggerUI == true>
@Api(tags="${classInfo.classComment}")
</#if>
@RequiredArgsConstructor
@RequestMapping("/${classInfo.className?uncap_first}")
public class ${classInfo.className}Controller {

    private final SQLManager sqlManager;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    <#if swaggerUI?exists && swaggerUI == true>
    @ApiOperation(value = "新增或编辑")
    </#if>
    public Object save(${classInfo.className} ${classInfo.className?uncap_first}){
        ${classInfo.className} ${classInfo.className?uncap_first}=sqlManager.unique(${classInfo.className}.class,${classInfo.className?uncap_first}.getId());
        if(${classInfo.className?uncap_first}!=null){
            sqlManager.updateById(${classInfo.className?uncap_first});
            return ${returnSuccess}("编辑成功");
        }else{
            sqlManager.insert(${classInfo.className?uncap_first});
            return ${returnError}("保存成功");
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    <#if swaggerUI?exists && swaggerUI == true>
    @ApiOperation(value = "删除")
    </#if>
    public Object delete(int id){
        ${classInfo.className} ${classInfo.className?uncap_first}=sqlManager.unique(${classInfo.className}.class,id);
        if(${classInfo.className?uncap_first}!=null){
            sqlManager.deleteById(id);
            return ${returnSuccess}("删除成功");
        }else{
            return ${returnError}("没有找到该对象");
        }
    }

    /**
     * 查询
     */
    @PostMapping("/find")
    <#if swaggerUI?exists && swaggerUI == true>
    @ApiOperation(value = "查询")
    </#if>
    public Object find(int id){
        ${classInfo.className} ${classInfo.className?uncap_first}=sqlManager.unique(${classInfo.className}.class,id);
        if(${classInfo.className?uncap_first}!=null){
            return ${returnSuccess}(${classInfo.className?uncap_first});
        }else{
            return ${returnError}("没有找到该对象");
        }
    }

    /**
     * 分页查询
     */
    @PostMapping("/list")
    <#if swaggerUI?exists && swaggerUI == true>
    @ApiOperation(value = "分页查询")
    </#if>
    public Object list(${classInfo.className} ${classInfo.className?uncap_first},
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<${classInfo.className}> list = sqlManager.query(${classInfo.className}.class).select();
            return ${returnSuccess}(list);
    }

}