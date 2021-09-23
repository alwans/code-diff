package com.test.diff.services.params;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class FileParams {

    @NotNull(message = "项目id不能为空")
    private Integer id;

    @NotEmpty(message = "branch参数不能为空")
    private String branch;

    @NotEmpty(message = "类名参数不能为空")
    private String className;

    private String commitId;

}
