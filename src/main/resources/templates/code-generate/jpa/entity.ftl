<#if packagingType?exists && packagingType==true>package ${packageName}.entity;</#if>

<#if automaticPackage?exists && automaticPackage == true>
    import java.io.Serializable;
    <#if lombok?exists && lombok==true>import lombok.Data;</#if>
    import java.util.Date;
    import java.util.List;
    <#if swaggerUI?exists && swaggerUI == true>
        import io.swagger.annotations.ApiModel;
        import io.swagger.annotations.ApiModelProperty;
    </#if>
    import javax.persistence.Column;
    import javax.persistence.Entity;
    import javax.persistence.Id;
    import javax.persistence.Table;
    import javax.persistence.GeneratedValue;
</#if>
/**
 * @description ${classInfo.classComment}
 * @author ${author}
 * @date ${.now?date}
 */
<#if lombok?exists && lombok==true>@Data</#if>
<#if swaggerUI?exists && swaggerUI == true>
@ApiModel("${classInfo.classComment}")
</#if>
@Table(name="${classInfo.tableName}")
public class ${classInfo.className} implements Serializable {

private static final long serialVersionUID = 1L;

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0>
    <#list classInfo.dynamicItem as fieldItem >
        <#if fieldComment?exists && fieldComment==true>
            /**
            * ${fieldItem.fieldAnnotate}
            */
        </#if>
        <#if swaggerUI?exists && swaggerUI == true>
            @ApiModelProperty("${classInfo.classComment}")
        </#if>
        @Column(name="${fieldItem.columnName}")
        private ${fieldItem.fieldClass} ${fieldItem.fieldName};
    </#list>
    <#if lombok?exists && lombok==false>
        <#list classInfo.dynamicItem as fieldItem>
            public ${fieldItem.fieldClass} get${fieldItem.fieldName?cap_first}() {
                return ${fieldItem.fieldName};
            }

            public void set${fieldItem.fieldName?cap_first}(${fieldItem.fieldClass} ${fieldItem.fieldName}) {
                this.${fieldItem.fieldName} = ${fieldItem.fieldName};
            }
        </#list>
    </#if>
    public ${classInfo.className}(){
    }

</#if>

}