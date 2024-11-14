package org.jerry.code.business.service;

import com.alibaba.fastjson2.JSONObject;
import org.jerry.code.business.dto.GenerateDTO;
import org.jerry.code.business.vo.DMLVo;

public interface ISaveTableService {

    Boolean saveTable(String tableName);

    /**
     * 生成插入语句
     *
     * @param generateDTO
     * @return
     */
    DMLVo generateDML(GenerateDTO generateDTO);

    /**
     * 生成DDL语句
     *
     * @param generateDTO
     * @return
     */
    String generateDDL(GenerateDTO generateDTO);


    GenerateDTO parsingSql(String sqlDdl);

    JSONObject getPackageInfo();
}
