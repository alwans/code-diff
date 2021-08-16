package com.test.codediff.vo;

import com.test.codediff.enums.DiffTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ClassInfoDiff {

    private String className;
    private DiffTypeEnum diffType;
    private List<MethodInfoDiff> methods;

}

@Data
class MethodInfoDiff {

    private String methodName;
    private DiffTypeEnum diffType;
    private String methodUri;
}
