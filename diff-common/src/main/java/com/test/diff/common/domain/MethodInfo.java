package com.test.diff.common.domain;


import com.test.diff.common.enums.DiffResultTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String methodName;
    private String md5;
    private DiffResultTypeEnum diffType;
    private String methodUri;
    private String params;

    public String getMethodUri(ClassInfo classInfo){
        return classInfo.getAsmClassName() +
                "." + getMethodName()+getParams();
    }
    public String getMethodUri(String asmClassNmae){
        return asmClassNmae + "." + getMethodName()+getParams();
    }
}
