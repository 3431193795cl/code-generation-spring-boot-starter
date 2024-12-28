package org.jerry.code.toolkit.tool;

import org.apache.commons.lang3.StringUtils;
import org.jerry.code.business.dto.DynamicItemDTO;
import org.jerry.code.business.dto.GenerateCodeDTO;
import org.jerry.code.business.dto.GenerateDTO;
import org.jerry.code.toolkit.constant.NonCaseString;
import org.jerry.code.toolkit.constant.ParamInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableParseTool implements Serializable {

    private static final long serialVersionUID = 6269591332794438066L;

    private TableParseTool() {
    }

    public static GenerateDTO processTableIntoGenerateDTO(GenerateCodeDTO generateCodeDTO) {
        //表名
        NonCaseString tableSql = NonCaseString.of(generateCodeDTO.getTableSql());
        //命名类型
        Integer nameCaseType = generateCodeDTO.getOptions().getNameType();
        //是否包装类
        Boolean isPackageType = generateCodeDTO.getOptions().getPackagingType();
        if (tableSql == null && tableSql.trim().length() == 0) {
            throw new RuntimeException("Table structure can not be empty. 表结构不能为空。");
        }
        //处理特殊字符
        tableSql.trim()
                .replaceAll("'","`")
                .replaceAll("\"","`")
                .replaceAll("，",",");
        //处理java字符串复制\n "
        tableSql.trim()
                .replaceAll("\\\\n`", "")
                .replaceAll("\\+", "")
                .replaceAll("``", "`")
                .replaceAll("\\\\", "");
        //表名
        String tableName = null;
        int tableKwIx = tableSql.indexOf("TABLE"); // 包含判断和位置一次搞定
        if (tableKwIx > -1 && tableSql.contains("(")) {
            tableName = tableSql.substring(tableKwIx + 5, tableSql.indexOf("(")).get();
        } else {
            throw new RuntimeException("Table structure incorrect.表结构不正确。");
        }
        //新增处理create table if not exists members情况
        if (tableName.contains("if not exists")) {
            tableName = tableName.replaceAll("if not exists", "");
        }

        if (tableName.contains("`")) {
            tableName = tableName
                    .substring(tableName.indexOf("`") + 1, tableName.lastIndexOf("`"));
        } else {
            //空格开头的，需要替换掉\n\t空格
            tableName = tableName
                    .replaceAll(" ", "")
                    .replaceAll("\n", "")
                    .replaceAll("\t", "");
        }
        //优化对byeas`.`ct_bd_customerdiscount这种命名的支持
        if (tableName.contains("`.`")) {
            tableName = tableName.substring(tableName.indexOf("`.`") + 3);
        } else if (tableName.contains(".")) {
            //优化对likeu.members这种命名的支持
            tableName = tableName.substring(tableName.indexOf(".") + 1);
        }
        String originTableName = tableName;
        //ignore prefix
        if(tableName!=null && StringTool.isNotNull(generateCodeDTO.getOptions().getIgnorePrefix())){
            tableName = tableName.replaceAll(generateCodeDTO.getOptions().getIgnorePrefix(),"");
        }
        // class Name
        String className = StringTool.upperCaseFirst(StringTool.underlineToCamelCase(tableName));
        if (className.contains("_")) {
            className = className.replaceAll("_", "");
        }

        // class Comment
        String classComment = null;
        //mysql是comment=,pgsql/oracle是comment on table,
        //2020-05-25 优化表备注的获取逻辑
        if (tableSql.containsAny("comment=", "comment on table")) {
            int ix = tableSql.lastIndexOf("comment=");
            String classCommentTmp = (ix > -1) ?
                    tableSql.substring(ix + 8).trim().get() :
                    tableSql.substring(tableSql.lastIndexOf("comment on table") + 17).trim().get();
            if (classCommentTmp.contains("`")) {
                classCommentTmp = classCommentTmp.substring(classCommentTmp.indexOf("`") + 1);
                classCommentTmp = classCommentTmp.substring(0, classCommentTmp.indexOf("`"));
                classComment = classCommentTmp;
            } else {
                //非常规的没法分析
                classComment = className;
            }
        } else {
            //修复表备注为空问题
            classComment = tableName;
        }

        //如果备注跟;混在一起，需要替换掉
        classComment = classComment.replaceAll(";", "");
        // field List
        List<DynamicItemDTO> fieldList = new ArrayList<>();

        // 正常( ) 内的一定是字段相关的定义。
        String fieldListTmp = tableSql.substring(tableSql.indexOf("(") + 1, tableSql.lastIndexOf(")")).get();

        // 匹配 comment，替换备注里的小逗号, 防止不小心被当成切割符号切割
        String commentPattenStr1 = "comment `(.*?)\\`";
        Matcher matcher1 = Pattern.compile(commentPattenStr1).matcher(fieldListTmp);
        while (matcher1.find()) {

            String commentTmp = matcher1.group();
            //2018-9-27 zhengk 不替换，只处理，支持COMMENT评论里面多种注释
            //commentTmp = commentTmp.replaceAll("\\ comment `|\\`", " ");      // "\\{|\\}"

            if (commentTmp.contains(",")) {
                String commentTmpFinal = commentTmp.replaceAll(",", "，");
                fieldListTmp = fieldListTmp.replace(matcher1.group(), commentTmpFinal);
            }
        }
        //2018-10-18 zhengkai 新增支持double(10, 2)等类型中有英文逗号的特殊情况
        String commentPattenStr2 = "\\`(.*?)\\`";
        Matcher matcher2 = Pattern.compile(commentPattenStr2).matcher(fieldListTmp);
        while (matcher2.find()) {
            String commentTmp2 = matcher2.group();
            if (commentTmp2.contains(",")) {
                String commentTmpFinal = commentTmp2.replaceAll(",", "，").replaceAll("\\(", "（").replaceAll("\\)", "）");
                fieldListTmp = fieldListTmp.replace(matcher2.group(), commentTmpFinal);
            }
        }
        //2018-10-18 zhengkai 新增支持double(10, 2)等类型中有英文逗号的特殊情况
        String commentPattenStr3 = "\\((.*?)\\)";
        Matcher matcher3 = Pattern.compile(commentPattenStr3).matcher(fieldListTmp);
        while (matcher3.find()) {
            String commentTmp3 = matcher3.group();
            if (commentTmp3.contains(",")) {
                String commentTmpFinal = commentTmp3.replaceAll(",", "，");
                fieldListTmp = fieldListTmp.replace(matcher3.group(), commentTmpFinal);
            }
        }
        String[] fieldLineList = fieldListTmp.split(",");
        if (fieldLineList.length > 0) {
            int i = 0;
            //i为了解决primary key关键字出现的地方，出现在前3行，一般和id有关
            for (String columnLine0 : fieldLineList) {
                NonCaseString columnLine = NonCaseString.of(columnLine0);
                i++;
                columnLine = columnLine.replaceAll("\n", "").replaceAll("\t", "").trim();
                // `userid` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                // 2018-9-18 zhengk 修改为contains，提升匹配率和匹配不按照规矩出牌的语句
                // 2018-11-8 zhengkai 修复tornadoorz反馈的KEY FK_permission_id (permission_id),KEY FK_role_id (role_id)情况
                // 2019-2-22 zhengkai 要在条件中使用复杂的表达式
                // 2019-4-29 zhengkai 优化对普通和特殊storage关键字的判断（感谢@AhHeadFloating的反馈 ）
                // 2020-10-20 zhengkai 优化对fulltext/index关键字的处理（感谢@WEGFan的反馈）
                // 2023-8-27 L&J 改用工具方法判断, 且修改变量名(非特殊标识), 方法抽取
                boolean notSpecialFlag = isNotSpecialColumnLine(columnLine, i);

                if (notSpecialFlag) {
                    //如果是oracle的number(x,x)，可能出现最后分割残留的,x)，这里做排除处理
                    if (columnLine.length() < 5) {
                        continue;
                    }
                    //2018-9-16 zhengkai 支持'符号以及空格的oracle语句// userid` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                    String columnName = "";
                    columnLine = columnLine.replaceAll("`", " ").replaceAll("\"", " ").replaceAll("'", "").replaceAll("  ", " ").trim();
                    //如果遇到username varchar(65) default '' not null,这种情况，判断第一个空格是否比第一个引号前
                    try {
                        columnName = columnLine.substring(0, columnLine.indexOf(" ")).get();
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.println("err happened: " + columnLine);
                        throw e;
                    }

                    // field Name
                    // 2019-09-08 yj 添加是否下划线转换为驼峰的判断
                    // 2023-8-27 L&J 支持原始列名任意命名风格, 不依赖用户是否输入下划线
                    String fieldName = null;
                    if (ParamInfo.NAME_CASE_TYPE.CAMEL_CASE.equals(nameCaseType)) {
                        // 2024-1-27 L&J 适配任意(maybe)原始风格转小写驼峰
                        fieldName = StringTool.toLowerCamel(columnName);
                    } else if (ParamInfo.NAME_CASE_TYPE.UNDER_SCORE_CASE.equals(nameCaseType)) {
                        fieldName = StringTool.toUnderline(columnName, false);
                    } else if (ParamInfo.NAME_CASE_TYPE.UPPER_UNDER_SCORE_CASE.equals(nameCaseType)) {
                        fieldName = StringTool.toUnderline(columnName.toUpperCase(), true);
                    } else {
                        fieldName = columnName;
                    }
                    columnLine = columnLine.substring(columnLine.indexOf("`") + 1).trim();
                    String mysqlType = columnLine.split("\\s+")[1];
                    if(mysqlType.contains("(")){
                        mysqlType = mysqlType.substring(0, mysqlType.indexOf("("));
                    }
                    //swagger class
                    String swaggerClass = "string" ;
                    if(MysqlJavaTypeChangeTool.getMysqlSwaggerTypeMap().containsKey(mysqlType)){
                        swaggerClass = MysqlJavaTypeChangeTool.getMysqlSwaggerTypeMap().get(mysqlType);
                    }
                    // field class
                    // int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                    String fieldClass = "String";
                    //2018-9-16 zhengk 补充char/clob/blob/json等类型，如果类型未知，默认为String
                    //2018-11-22 lshz0088 处理字段类型的时候，不严谨columnLine.contains(" int") 类似这种的，可在前后适当加一些空格之类的加以区分，否则当我的字段包含这些字符的时候，产生类型判断问题。
                    //2020-05-03 MOSHOW.K.ZHENG 优化对所有类型的处理
                    //2020-10-20 zhengkai 新增包装类型的转换选择
                    if(MysqlJavaTypeChangeTool.getMysqlJavaTypeMap().containsKey(mysqlType)){
                        fieldClass = MysqlJavaTypeChangeTool.getMysqlJavaTypeMap().get(mysqlType);
                    }
                    // field comment，MySQL的一般位于field行，而pgsql和oralce多位于后面。
                    String fieldComment = null;
                    if (tableSql.contains("comment on column") && (tableSql.contains("." + columnName + " is ") || tableSql.contains(".`" + columnName + "` is"))) {
                        //新增对pgsql/oracle的字段备注支持
                        //COMMENT ON COLUMN public.check_info.check_name IS '检查者名称';
                        //2018-11-22 lshz0088 正则表达式的点号前面应该加上两个反斜杠，否则会认为是任意字符
                        //2019-4-29 zhengkai 优化对oracle注释comment on column的支持（@liukex）
                        tableSql = tableSql.replaceAll(".`" + columnName + "` is", "." + columnName + " is");
                        Matcher columnCommentMatcher = Pattern.compile("\\." + columnName + " is `").matcher(tableSql);
                        fieldComment = columnName;
                        while (columnCommentMatcher.find()) {
                            String columnCommentTmp = columnCommentMatcher.group();
                            //System.out.println(columnCommentTmp);
                            fieldComment = tableSql.substring(tableSql.indexOf(columnCommentTmp) + columnCommentTmp.length()).trim().get();
                            fieldComment = fieldComment.substring(0, fieldComment.indexOf("`")).trim();
                        }
                    } else if (columnLine.contains(" comment")) {
                        //20200518 zhengkai 修复包含comment关键字的问题
                        String commentTmp = columnLine.substring(columnLine.lastIndexOf("comment") + 7).trim().get();
                        // '用户ID',
                        if (commentTmp.contains("`") || commentTmp.indexOf("`") != commentTmp.lastIndexOf("`")) {
                            commentTmp = commentTmp.substring(commentTmp.indexOf("`") + 1, commentTmp.lastIndexOf("`"));
                        }
                        //解决最后一句是评论，无主键且连着)的问题:album_id int(3) default '1' null comment '相册id：0 代表头像 1代表照片墙')
                        if (commentTmp.contains(")")) {
                            commentTmp = commentTmp.substring(0, commentTmp.lastIndexOf(")") + 1);
                        }
                        fieldComment = commentTmp;
                    } else {
                        //修复comment不存在导致报错的问题
                        fieldComment = columnName;
                    }

                    DynamicItemDTO fieldInfo = new DynamicItemDTO();
                    //
                    fieldInfo.setColumnName(columnName);
                    fieldInfo.setFieldName(fieldName);
                    fieldInfo.setFieldClass(fieldClass);
                    fieldInfo.setSwaggerClass(swaggerClass);
                    fieldInfo.setFieldAnnotate(fieldComment);

                    fieldList.add(fieldInfo);
                }
            }
        }

        if (fieldList.size() < 1) {
            throw new RuntimeException("表结构分析失败，请检查语句或者提交issue给我");
        }

        GenerateDTO codeJavaInfo = new GenerateDTO();
        codeJavaInfo.setTableName(tableName);
        codeJavaInfo.setClassName(className);
        codeJavaInfo.setTableComment(classComment);
        codeJavaInfo.setClassComment(classComment);
        codeJavaInfo.setDynamicItem(fieldList);
        codeJavaInfo.setOriginTableName(originTableName);
        return codeJavaInfo;
    }


    private static boolean isNotSpecialColumnLine(NonCaseString columnLine, int lineSeq) {
        return (
                !columnLine.containsAny(
                        "key ",
                        "constraint",
                        " using ",
                        "unique ",
                        "fulltext ",
                        "index ",
                        "pctincrease",
                        "buffer_pool",
                        "tablespace"
                )
                        && !(columnLine.contains("primary ") && columnLine.indexOf("storage") + 3 > columnLine.indexOf("("))
                        && !(columnLine.contains("primary ") && lineSeq > 3)
        );
    }
}
