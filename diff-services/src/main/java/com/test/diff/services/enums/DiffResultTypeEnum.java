package com.test.diff.services.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


/**
 * @author wl
 */

@Getter
@AllArgsConstructor
public enum DiffResultTypeEnum implements Serializable {

    DEL(0,"delete"),
    ADD(1, "add"),
    MODIFY(2, "modify");

    private Integer code;
    private String desc;


}
