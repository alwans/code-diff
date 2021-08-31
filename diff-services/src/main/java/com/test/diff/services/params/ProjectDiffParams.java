package com.test.diff.services.params;



import com.test.diff.services.enums.DiffTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @author wl
 */
@Data
public class ProjectDiffParams extends BaseParams{

    @NotNull(message="项目id不能为空")
    private Integer id;

    private String oldVersion;

    private String newVersion;

    /**
     * 比较类型
     * @see DiffTypeEnum
     */
    @NotNull(message = "diff类型不能为空")
    private Integer diffTypeCode;

    /**
     * 是否需要比较前先更新代码
     */
    private boolean isUpdateCode;

    /**
     * 基线commitId
     */
    private String oldCommitId;

    /**
     * 目标commitId
     */
    private String newCommitId;

}
