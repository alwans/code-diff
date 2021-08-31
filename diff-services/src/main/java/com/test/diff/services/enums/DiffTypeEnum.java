package com.test.diff.services.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wl
 */

@Getter
@AllArgsConstructor
public enum DiffTypeEnum {

    BRANCH_DIFF(0, "分支diff"),
    COMMIT_DIFF(1, "commitId diff"),
    UNKNOWN(-1, "不存在的比对类型");

    private int code;
    private String desc;

    public static DiffTypeEnum getDiffTypeByCode(int code){
        for(DiffTypeEnum diffTypeEnum: DiffTypeEnum.values()){
            if(code==diffTypeEnum.getCode()) {
                return diffTypeEnum;
            }
        }
        return DiffTypeEnum.UNKNOWN;
    }
}
