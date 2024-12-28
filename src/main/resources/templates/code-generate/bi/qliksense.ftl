//***************************
//[${classInfo.classComment} - ${classInfo.tableName}]
//AUTHOR ${author}
//HISTORY ${.now?date}
//***************************

//***************************
//load all
[${classInfo.tableName}]:
LOAD * FROM ['LIB://QVD/Log.qvd'](qvd);

//***************************
//load columns
[${classInfo.tableName}]:
LOAD
<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0>
    <#list classInfo.dynamicItem as fieldItem>
        "${fieldItem.fieldName}" as "${fieldItem.fieldName}"<#if fieldItem_has_next>,</#if>
    </#list>
</#if>
FROM
['LIB://QVD/Log.qvd'](qvd);
;

//load inline
[${classInfo.tableName}]:
LOAD * INLINE
[
<#list classInfo.dynamicItem as fieldItem>${fieldItem.columnName}<#if fieldItem_has_next>,</#if></#list>
<#list classInfo.dynamicItem as fieldItem>${fieldItem.fieldName}<#if fieldItem_has_next>,</#if></#list>
<#list classInfo.dynamicItem as fieldItem>${fieldItem.fieldAnnotate}<#if fieldItem_has_next>,</#if></#list>
];

//***************************
//load from api data connection (wrap on)
LIB CONNECT TO '${classInfo.tableName}_api';

RestConnectorMasterTable:
SQL SELECT
"__KEY_root",
(SELECT
<#list classInfo.dynamicItem as fieldItem>"${fieldItem.columnName}"</#list>
"__FK_object"
FROM "object" FK "__FK_object")
FROM JSON (wrap on) "root" PK "__KEY_root"
//    WITH CONNECTION (
//    Url "https://localhost:8080/log_api",
//    QUERY "page" "1",
//    QUERY "size" "100",
//    HTTPHEADER "token" "123456",
//    BODY "Post body here")
;

[${classInfo.tableName}]:
LOAD
<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0>
    <#list classInfo.dynamicItem as fieldItem>
        [${fieldItem.fieldName}] as [${fieldItem.fieldName}]
    </#list>
</#if>
[__FK_object] AS [__KEY_root]
RESIDENT RestConnectorMasterTable
WHERE NOT IsNull([__FK_stores]);

DROP TABLE [${classInfo.tableName}];
DROP TABLE RestConnectorMasterTable;

//***************************
//load from api data connection (wrap off)
LIB CONNECT TO '${classInfo.tableName}_api';
[${classInfo.tableName}]:
SQL SELECT
<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0>
    <#list classInfo.dynamicItem as fieldItem>
        [${fieldItem.fieldName}] as [${fieldItem.fieldName}]<#if fieldItem_has_next>,</#if>
    </#list>
</#if>
FROM JSON(wrap off) "${classInfo.tableName}"
//    WITH CONNECTION (
//    Url "https://localhost:8080/log_api",
//    QUERY "page" "1",
//    QUERY "size" "100",
//    HTTPHEADER "token" "123456",
//    BODY "Post body here")
;

//***************************
//load from sql data connection
LIB CONNECT TO '${classInfo.tableName}_db';

SQL SELECT
<#if classInfo.dynamicItem?exists && classInfo.dynamicItem?size gt 0>
    <#list classInfo.dynamicItem as fieldItem>
        [${fieldItem.fieldName}] as [${fieldItem.fieldName}]<#if fieldItem_has_next>,</#if>
    </#list>
</#if>
FROM
    ${classInfo.tableName}
WHERE
    Create_Time > '2023-01-01 00:00:00';