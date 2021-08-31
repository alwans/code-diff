package com.test.diff.services.params;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ProjectBranchParams extends ProjectParams {

    @NotEmpty(message = "分支名不能为空")
    private String branch_name;
}
