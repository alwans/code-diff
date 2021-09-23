package com.test.diff.services.params;

import com.test.diff.services.entity.CoverageApp;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CollectParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "项目id不能为空")
    private Integer projectId;

    /**
     * {@link com.test.diff.services.enums.CollectStatusEnum}
     */
    @NotNull(message = "收集状态不能为空")
    private Integer status;

    @NotNull(message = "应用列表不能为空")
    private List<CoverageApp> apps;

}
