package com.test.diff.services.enums;

import lombok.Getter;

/**
 * @author wl
 */
@Getter
public enum ReportStatusEnum {

    INIT(0, "未生成"),
    REPORT_GENERATING(1, "报告生成中"),
    REPORT_SUCCESS(2, "报告生成成功"),
    REPORT_FAILED(3, "报告生成失败"),
    UNKNOWN(-1, "未知报告状态");

    private int code;
    private String desc;

    ReportStatusEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static ReportStatusEnum getEnumByCode(int code){
        ReportStatusEnum[] enums = ReportStatusEnum.values();
        for(ReportStatusEnum e: enums){
            if(e.getCode() == code){
                return e;
            }
        }
        return UNKNOWN;
    }
}
