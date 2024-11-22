package org.jerry.code.business.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

public interface IOperateTableService {

    public JSONArray getTables();

    String getTableDDL(String tableName);

    Boolean deleteTable(String tableName);

    Object runSql(String sql);
}
