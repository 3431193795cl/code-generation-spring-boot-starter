<#if packagingType?exists && packagingType==true>package ${packageName}.controller;</#if>

<#if automaticPackage?exists && automaticPackage == true>
import ${packageName}.entity.${classInfo.className};
import ${packageName}.repository.${classInfo.className}Repository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
<#if swaggerUI?exists && swaggerUI == true>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>
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
@RequestMapping("/${classInfo.className?uncap_first}")
public class ${classInfo.className}Controller {

    private final ${classInfo.className}Repository ${classInfo.className?uncap_first}Repository;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    <#if swaggerUI?exists && swaggerUI == true>
    @ApiOperation(value = "新增或编辑")
    </#if>
    public Object save(${classInfo.className} ${classInfo.className?uncap_first}){
        return ${classInfo.className?uncap_first}Repository.save(${classInfo.className?uncap_first});
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    <#if swaggerUI?exists && swaggerUI == true>
    @ApiOperation(value = "删除")
    </#if>
    public Object delete(int id){
        Optional<${classInfo.className}> ${classInfo.className?uncap_first} = ${classInfo.className?uncap_first}Repository.findById(id);
        if(${classInfo.className?uncap_first}.isPresent()){
            ${classInfo.className?uncap_first}Repository.deleteById(id);
            return ReturnT.success("删除成功");
        }else{
            return ReturnT.error("没有找到该对象");
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
        Optional<${classInfo.className}> ${classInfo.className?uncap_first} = ${classInfo.className?uncap_first}Repository.findById(id);
        if(${classInfo.className?uncap_first}.isPresent()){
            return ReturnT.success(${classInfo.className?uncap_first}.get());
        }else{
            return ReturnT.error("没有找到该对象");
        }
    }

    /**
     * 分页查询
     */
    <#if swaggerUI?exists && swaggerUI == true>
    @ApiOperation(value = "分页查询")
    </#if>
    @PostMapping("/list")
    public Object list(${classInfo.className} ${classInfo.className?uncap_first},
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @RequestParam(required = false, defaultValue = "10") int pageSize) {
            //创建匹配器，需要查询条件请修改此处代码
            ExampleMatcher matcher = ExampleMatcher.matchingAll();
            //创建实例
            Example<${classInfo.className}> example = Example.of(${classInfo.className?uncap_first}, matcher);
            //分页构造
            Pageable pageable = PageRequest.of(pageNumber,pageSize);
            return ${classInfo.className?uncap_first}Repository.findAll(example, pageable);
    }
}