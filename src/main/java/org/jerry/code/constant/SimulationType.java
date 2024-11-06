package org.jerry.code.constant;

import lombok.Getter;

@Getter
public enum SimulationType {
    fixed(10001, "固定"),
    random(10002, "随机"),
    incremental(10003, "递增"),
    rule(10004, "规则"),
    noSimulation(10005, "不模拟");

    private final Integer code;
    private final String desc;

    SimulationType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SimulationType getByCode(Integer code) {
        for (SimulationType item : SimulationType.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
