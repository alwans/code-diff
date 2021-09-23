package com.test.diff.services.enums;

import lombok.Getter;

/**
 * @author wl
 */
@Getter
public enum ReportTypeEnum {

    FULL(0, "全量"),
    INCREMENT(1, "增量"),
    UNKNOWN(-1, "未知");

    private int code;

    private String desc;

    ReportTypeEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
