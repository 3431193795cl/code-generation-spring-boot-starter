<#if isWithPackage?exists && isWithPackage==true>package ${packageName}.mapper;</#if>

<#if isAutoImport?exists && isAutoImport == true>
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
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
     int insert(${classInfo.className} ${classInfo.className?uncap_first});

     /**
     * 刪除
     * @author ${author}
     * @date ${.now?string('yyyy/MM/dd')}
     */
     int delete(int id);

     /**
     * 更新
     * @author ${author}
     * @date ${.now?string('yyyy/MM/dd')}
     */
     int update(${classInfo.className} ${classInfo.className?uncap_first});

     /**
     * 查询 根据主键 id 查询
     * @author ${author}
     * @date ${.now?string('yyyy/MM/dd')}
     */
     ${classInfo.className} load(int id);

     /**
     * 查询 分页查询
     * @author ${author}
     * @date ${.now?string('yyyy/MM/dd')}
     */
     List<${classInfo.className}> pageList(@Param("offset")int offset,@Param("pagesize")int pagesize);

     /**
     * 查询 分页查询 count
     * @author ${author}
     * @date ${.now?string('yyyy/MM/dd')}
     */
     int pageListCount(@Param("offset")int offset,@Param("pagesize")int pagesize);
 }