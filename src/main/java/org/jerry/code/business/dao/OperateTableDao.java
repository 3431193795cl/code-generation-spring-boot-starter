package org.jerry.code.business.dao;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class OperateTableDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public JSONObject selectTables() {
        JSONObject jsonObject = jdbcTemplate.queryForObject("SELECT DATABASE()", JSONObject.class);;
        JSONArray showTables = jdbcTemplate.queryForObject("show tables", JSONArray.class);
        jsonObject.put("tables", showTables);
        return jsonObject;
    }
}
