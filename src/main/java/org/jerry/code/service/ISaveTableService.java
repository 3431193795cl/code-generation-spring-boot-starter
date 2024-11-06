package org.jerry.code.service;

import org.jerry.code.dto.GenerateDTO;
import org.jerry.code.vo.DMLVo;

import java.util.List;

public interface ISaveTableService {

    Boolean saveTable(String tableName);
    /**
     * 生成插入语句
     * @param generateDTO
     * @return
     */
    DMLVo generateDML(GenerateDTO generateDTO);

    /**
     * 生成DDL语句
     * @param generateDTO
     * @return
     */
    String generateDDL(GenerateDTO generateDTO);
}
