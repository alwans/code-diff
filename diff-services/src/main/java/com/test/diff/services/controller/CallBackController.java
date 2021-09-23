package com.test.diff.services.controller;

import com.test.diff.services.entity.CoverageApp;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.service.CoverageAppService;
import com.test.diff.services.service.ProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.test.diff.services.base.controller.result.BaseResult;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("api/cb")
@Slf4j
public class CallBackController {

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private CoverageAppService coverageAppService;


    @GetMapping("pull/data")
    public BaseResult cbPullData(@RequestParam(value = "projectId", required = true) int projectId,
                                 @RequestParam(value = "jacocoPort", required = true) int jacocoPort){
        ProjectInfo projectInfo = projectInfoService.getById(projectId);
        if(Objects.isNull(projectInfo)){
            log.error("项目(id={})不存在", projectId);
            return BaseResult.error(StatusCode.PARAMS_ERROR, "项目(id="+projectId+")不存在");
        }
        CoverageApp coverageApp = coverageAppService.getAppByProjectIdAndPort(projectId, jacocoPort);
        if(Objects.isNull(coverageApp)){
            log.error("id为{}的项目：{}，没有正在收集中的应用，无法完成拉取数据", projectInfo.getId(), projectInfo.getProjectName());
            return BaseResult.error(StatusCode.PARAMS_ERROR, "无运行中的应用");
        }
        projectInfoService.pullExecData(projectInfo, coverageApp);
        return BaseResult.success(null, "拉取成功");
    }
}
