package org.jerry.code.business.vo;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

@Data
public class DMLVo {
    /**
     * dml
     */
   private String dml;

    /**
     * 头信息
     */
    private JSONArray headDmlList;


    /**
     * dmlList
     */
    private JSONArray dmlList;

    /**
     * dml_json
     */
    private String dmlJson;
}
