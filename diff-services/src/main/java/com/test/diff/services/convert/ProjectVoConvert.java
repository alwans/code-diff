package com.test.diff.services.convert;

import com.test.diff.services.entity.CoverageApp;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.enums.CollectStatusEnum;
import com.test.diff.services.enums.ReportStatusEnum;
import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.exceptions.BizException;
import com.test.diff.services.service.CoverageAppService;
import com.test.diff.services.vo.ProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("ProjectVoConvert")
@Slf4j
public class ProjectVoConvert implements ModelConvert<ProjectInfo, ProjectVo>{

    @Resource
    private CoverageAppService coverageAppService;

    @Override
    public ProjectVo convert(ProjectInfo source) {
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(source, projectVo);
//        CollectStatusEnum e = CollectStatusEnum.getObjByCode(source.getCollectStatus());
//        if(e == CollectStatusEnum.UNKNOWN){
//            log.error("ProjectVo转换失败, 收集状态code:{} 不存在", source.getCollectStatus());
//            throw new BizException(StatusCode.UNKNOWN_COLLECT_STATUS);
//        }
//        ReportStatusEnum rse = ReportStatusEnum.getEnumByCode(source.getReportStatus());
//        if(rse == ReportStatusEnum.UNKNOWN){
//            log.error("ProjectVo转换失败, 报告状态code:{} 不存在", source.getReportStatus());
//            throw new BizException(StatusCode.UNKNOWN_REPORT_STATUS);
//        }
//        projectVo.setCollectStatusEnum(e);
//        projectVo.setReportStatusEnum(rse);
        List<CoverageApp> apps =  coverageAppService.getListByProjectId(source.getId());
        projectVo.setApps(apps);
        return projectVo;
    }

    @Override
    public ProjectInfo reconvert(ProjectVo target) {
        ProjectInfo projectInfo = new ProjectInfo();
        BeanUtils.copyProperties(target, projectInfo);
//        projectInfo.setCollectStatus(target.getCollectStatusEnum().getCode());
//        projectInfo.setReportStatus(target.getReportStatusEnum().getCode());
        return projectInfo;
    }
}
