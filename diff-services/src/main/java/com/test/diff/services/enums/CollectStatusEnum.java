package com.test.diff.services.enums;

import lombok.Getter;

/**
 * @author wl
 */

@Getter
public enum CollectStatusEnum {

    INIT(0, "未收集"),
    COLLECTING(1, "收集中"),
    SUSPEND_COLLECT(2, "暂停收集"),
    COLLECT_END(3, "收集结束"),
    UNKNOWN(4, "未知收集状态");


    private int code;
    private String desc;

    CollectStatusEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static CollectStatusEnum getObjByCode(int code){
        CollectStatusEnum[] enums = CollectStatusEnum.values();
        for(CollectStatusEnum e: enums){
            if(e.getCode() == code){
                return e;
            }
        }
        return UNKNOWN;
    }
}
