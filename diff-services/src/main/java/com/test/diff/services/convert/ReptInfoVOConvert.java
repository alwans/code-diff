package com.test.diff.services.convert;

import com.test.diff.services.entity.RepoInfo;
import com.test.diff.services.enums.CodeManageTypeEnum;
import com.test.diff.services.vo.RepotInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author wl
 */
@Component("DeptInfoConvert")
@Slf4j
public class ReptInfoVOConvert implements ModelConvert<RepoInfo, RepotInfoVO>{


    @Override
    public RepotInfoVO convert(RepoInfo source) {
        RepotInfoVO target = new RepotInfoVO();
        if(source == null){
            return  target;
        }
        BeanUtils.copyProperties(source, target);
        target.setDepotType(CodeManageTypeEnum.getCmteByCode(source.getDepotType()));
        return target;
    }

    @Override
    public RepoInfo reconvert(RepotInfoVO target) {
        RepoInfo source = new RepoInfo();
        if(target == null){
            return source;
        }
        BeanUtils.copyProperties(target, source);
        source.setDepotType(target.getDepotType().getCode());
        return source;
    }
}
