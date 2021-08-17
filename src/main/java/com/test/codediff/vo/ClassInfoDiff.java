package com.test.codediff.vo;

import com.test.codediff.enums.DiffResultTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class ClassInfoDiff implements Serializable {
    private static final long serialVersionUID = 1L;

    private String className;
    private DiffResultTypeEnum diffType;
    private List<MethodInfoDiff> methods;

}

@Data
class MethodInfoDiff implements Serializable {
    private static final long serialVersionUID = 1L;

    private String methodName;
    private DiffResultTypeEnum diffType;
    private String methodUri;
}
