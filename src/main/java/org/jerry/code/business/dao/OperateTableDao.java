package org.jerry.code.business.dao;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OperateTableDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public JSONArray selectTables() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("label", jdbcTemplate.queryForObject("SELECT DATABASE()", String.class));
        jsonObject.put("children", jdbcTemplate.queryForList("show tables", String.class).stream().map(e -> {
            JSONObject json = new JSONObject();
            json.put("label", e);
            return json;
        }).collect(Collectors.toList()));
        jsonArray.add(jsonObject);
        return jsonArray;
    }
}
