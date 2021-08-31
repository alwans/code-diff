package com.test.diff.services.params;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProjectParams extends BaseParams{

    @NotNull(message="项目id不能为空")
    private long project_id;
}
