package com.test.codediff.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {

    SUCCESS(10000, "成功"),
    PARAMS_ERROR(10010, "参数错误"),
    OTHER_ERROR(-1, "其他错误"),

    //git相关错误
    GIT_ERROR(10020, "git操作异常"),

    //操作文件相关
    File_ERROR(10030, "文件操作异常");


    private int code;
    private String desc;
}
