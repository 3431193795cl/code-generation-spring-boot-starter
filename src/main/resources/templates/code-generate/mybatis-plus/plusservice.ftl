<#if packagingType?exists && packagingType==true>package ${packageName}.service;</#if>
<#if automaticPackage?exists && automaticPackage==true>
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