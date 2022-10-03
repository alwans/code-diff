package com.test.diff.services.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author wl
 * @date 2022/10/1
 * @Description 克隆项目请求参数
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloneParamDto extends BaseParams {

    @NotNull(message = "工程id不可为空")
    private Integer id;

    @NotEmpty(message = "分支名不可为空")
    private String branchName;

    private String commitId;

//    @ApiModelProperty(value = "编译命令")
//    private String compileInstruction;
//
//    /**
//     * {@link BuildToolTypeEnum#getValue()}
//     */
//    @ApiModelProperty(value = "编译工具类型")
//    private Integer buildToolType;
//
//    @ApiModelProperty("是否需要编译")
//    private Boolean isNeedCompile;
}
