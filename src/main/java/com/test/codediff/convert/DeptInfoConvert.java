package com.test.codediff.convert;

import com.test.codediff.entity.RepotInfo;
import com.test.codediff.enums.CodeManageTypeEnum;
import com.test.codediff.vo.ReptInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component("DeptInfoConvert")
@Slf4j
public class DeptInfoConvert implements ModelConvert<RepotInfo, ReptInfoVO>{


    @Override
    public ReptInfoVO convert(RepotInfo source) {
        ReptInfoVO target = new ReptInfoVO();
        BeanUtils.copyProperties(source, target);
        target.setDepotType(CodeManageTypeEnum.getCmteByCode(source.getDepotType()));
        return target;
    }

    @Override
    public RepotInfo reconvert(ReptInfoVO target) {
        RepotInfo source = new RepotInfo();
        BeanUtils.copyProperties(target, source);
        source.setDepotType(target.getDepotType().getCode());
        return source;
    }
}
