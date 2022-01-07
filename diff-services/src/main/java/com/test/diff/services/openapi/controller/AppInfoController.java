package com.test.diff.services.openapi.controller;

import com.test.diff.services.entity.CoverageApp;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.service.CoverageAppService;
import com.test.diff.services.service.ProjectInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.diff.services.base.controller.result.BaseResult;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/app")
public class AppInfoController {


    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private CoverageAppService coverageAppService;

    @GetMapping(value = "/info")
    public BaseResult getInfo(@RequestParam("groupName") String groupName,
                              @RequestParam("projectName") String projectName){
        if(StringUtils.isEmpty(groupName)
                || StringUtils.isEmpty(projectName) ){
            return BaseResult.error(StatusCode.PARAMS_ERROR, "参数不允许为空");
        }
        List<ProjectInfo> list = projectInfoService.getInfoByName(groupName);
        if(CollectionUtils.isEmpty(list)){
            return BaseResult.error(StatusCode.PROJECT_INFO_NOT_EXISTS);
        }
        ProjectInfo projectInfo = list.get(0);
        List<CoverageApp> apps = coverageAppService.getListByProjectId(projectInfo.getId());
        if(CollectionUtils.isEmpty(apps)){
            return BaseResult.error(StatusCode.APP_INFO_NOT_EXISTS);
        }
        for(CoverageApp app : apps){
            if(app.getAppName().equalsIgnoreCase(projectName)){
                return  BaseResult.success(app);
            }
        }
        return BaseResult.error(StatusCode.APP_INFO_NOT_EXISTS);
    }

}
