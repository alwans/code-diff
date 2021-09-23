package com.test.diff.services.params;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author wl
 */
@Data
public class ProjectParams implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message="项目id不能为空")
    private long projectId;
}
