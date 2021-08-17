package com.test.codediff.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeManageTypeEnum {

    SVN(0, "svn"),
    GIT(1, "git"),
    UNKNOWN(-1, "unknown");

    private Integer code;
    private String desc;

    public static CodeManageTypeEnum getCmteByCode(Integer code){
        for(CodeManageTypeEnum cmte: CodeManageTypeEnum.values()){
            if(code==cmte.getCode())
                return cmte;
        }
        return CodeManageTypeEnum.UNKNOWN;
    }
}
