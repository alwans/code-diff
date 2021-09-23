package com.test.diff.services.params;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author wl
 */
@Getter
@Setter
@Builder
public class ReportParams implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "项目id不能为空")
    private Integer projectId;

    /**
     * 报告类型
     * 0: 全量 ， 1：增量
     * {@link com.test.diff.services.enums.ReportTypeEnum}
     */
    @NotNull(message = "报告类型不能为空")
    private Integer reportType;

    /**
     * 增量报告中的diff类型
     * {@link com.test.diff.services.enums.DiffTypeEnum}
     */
    private Integer diffType;

    /**
     * 基线分支
     */
    private String oldVersion;



}
