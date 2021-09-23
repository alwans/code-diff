package com.test.diff.common.enums;

import lombok.Getter;

@Getter
public enum HttpCodeEnum {

    SUCCESS(10000, "成功");

    private int code;
    private String msg;

    HttpCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
