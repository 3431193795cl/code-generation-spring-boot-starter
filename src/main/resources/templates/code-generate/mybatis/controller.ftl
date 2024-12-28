<#if isWithPackage?exists && isWithPackage==true>package ${packageName}.controller;</#if>

<#if isAutoImport?exists && isAutoImport==true>
import com.alibaba.fastjson.JSON;
import ${packageName}.entity.${classInfo.className};
import ${packageName}.service.${classInfo.className}Service;
import ${packageName}.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Map;
</#if>

/**
* @description ${classInfo.classComment}控制器
* @author ${author}
* @date ${.now?date}
*/
@Slf4j
@RestController
@RequestMapping("/${classInfo.className?uncap_first}")
public class ${classInfo.className}Controller {

    @Autowired
    private ${classInfo.className}Service ${classInfo.className?uncap_first}Service;

    /**
        * 新增或编辑
        */
        @PostMapping("/save")
        public Object save(@RequestBody ${classInfo.className} ${classInfo.className?uncap_first}){
            log.info("${classInfo.className?uncap_first}:"+JSON.toJSONString(${classInfo.className?uncap_first}));
            ${classInfo.className} old${classInfo.className} = ${classInfo.className?uncap_first}Service.selectOne(new QueryWrapper<${classInfo.className}>().eq("${classInfo.className?uncap_first}_id",${classInfo.className?uncap_first}.get${classInfo.className}Id()));
            ${classInfo.className?uncap_first}.setUpdateTime(new Date());
            if(old${classInfo.className}!=null){
                ${classInfo.className?uncap_first}Service.updateById(${classInfo.className?uncap_first});
            }else{
            if(${classInfo.className?uncap_first}Service.selectOne(new QueryWrapper<${classInfo.className}>().eq("${classInfo.className?uncap_first}_name",${classInfo.className?uncap_first}.get${classInfo.className}Name()))!=null){
                return ${returnError}("保存失败，名字重复");
            }
            ${classInfo.className?uncap_first}.setCreateTime(new Date());
            ${classInfo.className?uncap_first}Service.insert(${classInfo.className?uncap_first});
            }
            return ${returnSuccess}("保存成功");
        }

        /**
        * 删除
        */
        @PostMapping("/delete")
        public Object delete(int id){
        ${classInfo.className} ${classInfo.className?uncap_first} = ${classInfo.className?uncap_first}Service.selectOne(new QueryWrapper<${classInfo.className}>().eq("${classInfo.className?uncap_first}_id",id));
            if(${classInfo.className?uncap_first}!=null){
                ${classInfo.className?uncap_first}Service.deleteById(id);
                return ${returnSuccess}("删除成功");
            }else{
                return ${returnError}("没有找到该对象");
            }
        }

        /**
        * 查询
        */
        @PostMapping("/find")
        public Object find(int id){
        ${classInfo.className} ${classInfo.className?uncap_first} = ${classInfo.className?uncap_first}Service.selectOne(new QueryWrapper<${classInfo.className}>().eq("${classInfo.className?uncap_first}_id",id));
            if(${classInfo.className?uncap_first}!=null){
                return ${returnSuccess}(${classInfo.className?uncap_first});
            }else{
                return ${returnError}("没有找到该对象");
            }
        }

        /**
        * 自动分页查询
        */
        @PostMapping("/list")
        public Object list(String searchParams,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int limit) {
            log.info("page:"+page+"-limit:"+limit+"-json:"+ JSON.toJSONString(searchParams));
            //分页构造器
            Page<${classInfo.className}> buildPage = new Page<${classInfo.className}>(page,limit);
            //条件构造器
            QueryWrapper<${classInfo.className}> queryWrapper = new QueryWrapper<${classInfo.className}>();
            if(StringUtils.isNotEmpty(searchParams)&&JSON.isValid(searchParams)) {
                ${classInfo.className} ${classInfo.className?uncap_first} = JSON.parseObject(searchParams, ${classInfo.className}.class);
                queryWrapper.eq(StringUtils.isNoneEmpty(${classInfo.className?uncap_first}.get${classInfo.className}Name()), "${classInfo.className?uncap_first}_name", ${classInfo.className?uncap_first}.get${classInfo.className}Name());
            }
            //执行分页
            IPage<${classInfo.className}> pageList = ${classInfo.className?uncap_first}Service.selectPage(buildPage, queryWrapper);
            //返回结果
            return ${returnSuccess}.PAGE(pageList.getRecords(),pageList.getTotal());
        }
        /**
        * 手工分页查询(按需使用)
        */
        /*@PostMapping("/list2")
        public Object list2(String searchParams,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int limit) {
            log.info("searchParams:"+ JSON.toJSONString(searchParams));
            //通用模式
            ${classInfo.className} queryParamDTO = JSON.parseObject(searchParams, ${classInfo.className}.class);
            List<${classInfo.className}> itemList = ${classInfo.className?uncap_first}Service.pageAll(queryParamDTO,(page - 1)* limit,limit);
            Integer itemCount = ${classInfo.className?uncap_first}Service.countAll(queryParamDTO);
            //返回结果
            return ${returnSuccess}.PAGE(itemList,itemCount);
        }*/
        
        @GetMapping("/list")
        public ModelAndView listPage(){
            return new ModelAndView("${classInfo.className?uncap_first}-list");
        }

        @GetMapping("/edit")
        public ModelAndView editPage(int id){
            ${classInfo.className} ${classInfo.className?uncap_first} = ${classInfo.className?uncap_first}Service.selectOne(new QueryWrapper<${classInfo.className}>().eq("${classInfo.className?uncap_first}_id",id));
            return new ModelAndView("${classInfo.className?uncap_first}-edit","${classInfo.className?uncap_first}",${classInfo.className?uncap_first});
        }


}