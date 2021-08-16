package com.test.codediff.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeManageTypeEnum {

    SVN(0, "svn"),
    GIT(1, "git");

    private Integer code;
    private String desc;


}
