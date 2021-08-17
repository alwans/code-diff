package com.test.codediff.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum DiffResultTypeEnum {

    DEL(0,"delete"),
    ADD(1, "add"),
    MODIFY(2, "modify");

    private Integer code;
    private String desc;


}
