package com.test.diff.services.enums;


public enum CodeManageTypeEnum {

    SVN(0, "svn"),
    GIT(1, "git"),
    UNKNOWN(-1, "unknown");

    private Integer code;
    private String desc;

    CodeManageTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CodeManageTypeEnum getCmteByCode(Integer code){
        for(CodeManageTypeEnum cmte: CodeManageTypeEnum.values()){
            if(code.equals(cmte.getCode())) {
                return cmte;
            }
        }
        return CodeManageTypeEnum.UNKNOWN;
    }
}
