<#if isWithPackage?exists && isWithPackage==true>package ${packageName}.service;</#if>
<#if isAutoImport?exists && isAutoImport==true>
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.IService;
</#if>
/**
 * @description ${classInfo.classComment}服务层
 * @author ${author}
 * @date ${.now?date}
 */
@Service
public interface ${classInfo.className}Service extends IService<${classInfo.className}> {



}