package com.test.codediff.vo;

import com.test.codediff.enums.DiffResultTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wl
 */
@Data
@Builder
public class MethodInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String methodName;
    private String md5;
    private DiffResultTypeEnum diffType;
//    private String methodUri;
    private String params;

    public String getUri(ClassInfo classInfo){
        return classInfo.getAsmClassName() +
                "~" + getMethodName();
    }
}
