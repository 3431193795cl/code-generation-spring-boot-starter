package org.jerry.code.constant;

import lombok.Getter;

@Getter
public enum SimulationValue {

    name("1000201", "姓名"),
    adderess("1000202", "地址"),
    idCard("1000203", "身份证号"),
    email("1000204", "邮箱"),
    phone("1000205", "手机号"),
    garbage("1000206", "废话");

    private final String code;
    private final String desc;

    SimulationValue(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SimulationValue getByCode(String code) {
        for (SimulationValue item : SimulationValue.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
