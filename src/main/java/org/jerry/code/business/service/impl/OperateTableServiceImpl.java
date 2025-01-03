package org.jerry.code.business.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jerry.code.business.dao.OperateTableDao;
import org.jerry.code.business.service.IOperateTableService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class OperateTableServiceImpl implements IOperateTableService {

    @Resource
    private OperateTableDao operateTableDao;

    @Override
    public JSONArray getTables() {
        return operateTableDao.selectTables();
    }

    @Override
    public String getTableDDL(String tableName) {
        String ddl = operateTableDao.selectTableDDL(tableName);
        log.info("ddl:{}", ddl);
        return ddl;
    }

    @Override
    public Boolean deleteTable(String tableName) {
        return operateTableDao.deleteTable(tableName);
    }


    @Override
    public Object runSql(String sql) {
        return operateTableDao.runSql(sql);
    }
}
