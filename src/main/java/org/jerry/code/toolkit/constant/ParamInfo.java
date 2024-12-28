package org.jerry.code.toolkit.constant;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ParamInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tableSql;
    private Map<String,Object> options;

    @Data
    public static class NAME_CASE_TYPE {
        public static String CAMEL_CASE = "CamelCase";
        public static String UNDER_SCORE_CASE = "UnderScoreCase";
        public static String UPPER_UNDER_SCORE_CASE = "UpperUnderScoreCase";
    }
}
