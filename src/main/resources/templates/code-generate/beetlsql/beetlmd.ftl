sample
===

select #use("cols")# from ${classInfo.tableName} where #use("condition")#


cols
===
<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0>
    <#list classInfo.dynamicItem as fieldItem>
        `${fieldItem.columnName}`<#if fieldItem_has_next>,</#if>
    </#list>
</#if>

updateSample
===
<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0>
    <#list classInfo.dynamicItem as fieldItem>
        `${fieldItem.columnName} = #${fieldItem.fieldName}#`<#if fieldItem_has_next>,</#if>
    </#list>
</#if>

condition
===
	1=1
<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0>
    <#list classInfo.dynamicItem as fieldItem>
        @if(!isEmpty(${fieldItem.fieldName})){
        	and `${fieldItem.columnName}`=#${fieldItem.fieldName}#
        }
    </#list>
</#if>