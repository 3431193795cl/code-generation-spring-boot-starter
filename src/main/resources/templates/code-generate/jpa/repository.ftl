<#if isWithPackage?exists && isWithPackage==true>package ${packageName}.repository;</#if>
<#if isAutoImport?exists && isAutoImport == true>
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ${packageName}.entity.${classInfo.className};
</#if>

/**
 * @description ${classInfo.classComment}
 * @author ${author}
 * @date ${.now?date}
 */
@Repository
public interface ${classInfo.className}Repository extends JpaRepository<${classInfo.className},Integer> {

}