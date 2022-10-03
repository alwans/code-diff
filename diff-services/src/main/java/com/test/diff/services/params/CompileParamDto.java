package com.test.diff.services.params;
;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author wl
 * @date 2022/9/30
 * @Description 编译参数
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompileParamDto extends BaseParams {

    @NotNull(message = "工程id不可为空")
    private Integer id;

    @NotEmpty(message = "分支名不可为空")
    private String branchName;

    private String commitId;

    private String compileInstruction;


    private Integer buildToolType;

    private Boolean isRecompile;

}
