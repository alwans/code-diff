package com.test.codediff.params;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wl
 */
@Data
public class ProjectDiffParams extends BaseParams{

    @NotNull(message="项目id不能为空")
    private long id;

    private String oldVersion;

    private String newVersion;

    /**
     * 比较类型
     * @see com.test.codediff.enums.DiffTypeEnum
     */
    @NotNull(message = "diff类型不能为空")
    private int diffTypeCode;

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
