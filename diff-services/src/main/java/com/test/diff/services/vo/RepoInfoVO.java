package com.test.diff.services.vo;


import com.test.diff.services.enums.CodeManageTypeEnum;
import lombok.Data;

import java.util.Date;

@Data
public class RepoInfoVO extends BaseVO {

    private Long id;

    private String depotName;

    private CodeManageTypeEnum depotType;

    private String depotUrl;

    private String userName;

    private String passwd;

    private Boolean isDisable;

    private Boolean isDelete;

    private Date addTime;

    private Date lastTime;

    private static final long serialVersionUID = 1L;

}
