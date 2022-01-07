package com.test.diff.services.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.diff.services.convert.ModelConvert;
import com.test.diff.services.entity.CoverageApp;
import com.test.diff.services.entity.CoverageReport;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.enums.*;
import com.test.diff.services.params.CollectParams;
import com.test.diff.services.params.ListProjectParams;
import com.test.diff.services.params.ProjectDiffParams;
import com.test.diff.services.params.ReportParams;
import com.test.diff.services.service.CoverageAppService;
import com.test.diff.services.service.CoverageReportService;
import com.test.diff.services.service.ProjectInfoService;
import com.test.diff.services.vo.ProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.test.diff.services.base.controller.result.BaseResult;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author wl
 */
@RestController
@RequestMapping("api/coverage")
@Slf4j
public class WebSiteController {

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private CoverageReportService coverageReportService;

    @Resource
    private CoverageAppService coverageAppService;

    @Resource
    private ModelConvert<ProjectInfo, ProjectVo> projectVoModelConvert;

    @GetMapping("/list")
    public BaseResult getList(@RequestParam("currentPage") int page,
                              @RequestParam("pageSize") int size,
                              @RequestParam(value = "projectId", required = false) int projectId){
        ListProjectParams params = ListProjectParams.builder().page(page).size(size).projectId(projectId).build();
        Page<ProjectInfo> pages = projectInfoService.selectListByParams(params);
        Page<ProjectVo> projectVoPage = new Page<>();
        projectVoPage.setRecords(pages.getRecords().stream().map(projectInfo -> projectVoModelConvert.convert(projectInfo)).collect(Collectors.toList()));
        projectVoPage.setCurrent(pages.getCurrent());
        projectVoPage.setSize(pages.getSize());
        projectVoPage.setTotal(pages.getTotal());
        return BaseResult.success(projectVoPage);
    }

    @PostMapping("/save/project")
    public BaseResult saveProject(@RequestBody ProjectVo projectVo){
        //暂时没有考虑env+projectName的唯一性问题；包括app的也是如此
        ProjectInfo projectInfo = projectVoModelConvert.reconvert(projectVo);
        int projectId = projectInfo.getId().intValue();
        if(projectId == 0){
            projectId = (projectInfoService.create(projectInfo)).intValue();
        }else{
            projectInfo.setLastTime(new Date());
            projectInfoService.updateById(projectInfo);
            projectId = projectInfo.getId().intValue();
        }
        List<CoverageApp> coverageApps = projectVo.getApps();
        for(CoverageApp app: coverageApps){
            if(Objects.isNull(app.getId()) || app.getId() == 0){
                coverageAppService.create(projectId, app);
                continue;
            }
            app.setLastTime(new Date());
            coverageAppService.updateById(app);
        }
        return BaseResult.success(null, "保存成功");
    }

    @PostMapping(value = "collect/status", produces = "application/json;charset=UTF-8")
    public BaseResult updateCollectStatus(@Validated @RequestBody CollectParams params){
        if(Objects.isNull(params.getApps())
                || params.getApps().size() == 0){
            return BaseResult.error(StatusCode.PARAMS_ERROR, "需要收集的应用列表不能为空");
        }
        int projectId = params.getProjectId();
        ProjectInfo projectInfo = projectInfoService.getById(projectId);
        if(Objects.isNull(projectInfo)){
            return BaseResult.error(StatusCode.PROJECT_INFO_NOT_EXISTS);
        }
        switch (CollectStatusEnum.getObjByCode(params.getStatus())){
            case COLLECTING:
                return startOrContinueCollect(params.getApps(), projectId, projectInfo);
            case SUSPEND_COLLECT:
                return suspendCollect(projectInfo);
            case COLLECT_END:
                return stopCollect(params.getApps(), projectId, projectInfo);
            default:
                log.info("未知的收集操作状态： {}", projectId);
                return BaseResult.error(StatusCode.UNKNOWN_COLLECT_STATUS);
        }
    }

    @PostMapping(value = "/generate/report", produces = "application/json;charset=UTF-8")
    public BaseResult generateReport(@Validated  @RequestBody ReportParams params){
        if(params.getReportType() == ReportTypeEnum.INCREMENT.getCode()){
            if(params.getDiffType() == DiffTypeEnum.UNKNOWN.getCode()){
                return BaseResult.error(StatusCode.PARAMS_ERROR, "diff类型选择错误");
            }
            if(StringUtils.isBlank(params.getOldVersion())){
                return BaseResult.error(StatusCode.PARAMS_ERROR, "基线分支不允许为空");
            }
        }
        return coverageReportService.report(params);
    }

    @GetMapping( "/getReportUri")
    public BaseResult getReportUrl(@RequestParam("id") int projectId){
        if(projectId == 0){
            return BaseResult.error(StatusCode.PARAMS_ERROR, "项目id不允许为空");
        }
        return coverageReportService.getReportURI(projectId);
    }


    /**
     * 开始或继续收集
     * @param apps 应用id列表
     * @param projectId
     * @param projectInfo
     * @return
     */
    private BaseResult startOrContinueCollect(List<CoverageApp> apps, int projectId, ProjectInfo projectInfo){
        /**
         * 防止同一个工程并发请求，创建了多个report记录
         */
        synchronized (this){
            CoverageReport report = coverageReportService.selectUsedByProjectId(projectId);
            if(Objects.isNull(report)){
                coverageReportService.create(projectId);
            }
        }
        for(CoverageApp app : apps){
            if(app.getId() == 0){
                return BaseResult.error(StatusCode.PARAMS_ERROR, "id："+app.getId()+ "的应用不存在，请检查参数");
            }
            //把选中的应用设置为收集状态
            app.setStatus(true);
            coverageAppService.updateById(app);
        }
        projectInfo.setCollectStatus(CollectStatusEnum.COLLECTING.getCode());
        projectInfoService.updateById(projectInfo);
        return BaseResult.success(null, "操作成功");
    }

    /**
     * 暂停收集
     * @param projectInfo
     * @return
     */
    private BaseResult suspendCollect(ProjectInfo projectInfo){
        projectInfo.setCollectStatus(CollectStatusEnum.SUSPEND_COLLECT.getCode());
        projectInfoService.updateById(projectInfo);
        return BaseResult.success(null, "操作成功");
    }

    /**
     * 结束收集
     * @param apps 应用id列表
     * @param projectId
     * @param projectInfo
     * @return
     */
    private BaseResult stopCollect(List<CoverageApp> apps, int projectId, ProjectInfo projectInfo){
        CoverageReport report = coverageReportService.selectUsedByProjectId(projectId);
        if(!Objects.isNull(report)){
            //报告记录设置为不使用
            report.setIsUsed(false);
            coverageReportService.updateById(report);
        }
//        for(Integer appId : apps){
//            CoverageApp app = coverageAppService.getById(appId);
//            if(Objects.isNull(app)){
//                return BaseResult.error(StatusCode.PARAMS_ERROR, "id："+appId+ "的应用不存在，请检查参数");
//            }
//            //把选中的应用设置为未收集状态
//            app.setStatus(false);
//            coverageAppService.updateById(app);
//        }
        //把这个工程的所有应用状态都置为未收集
        List<CoverageApp> list = coverageAppService.getListByProjectId(projectId);
        list.stream().forEach(coverageApp -> coverageApp.setStatus(false));
        coverageAppService.updateBatchById(list);
        //更新项目收集状态为：初始状态
        projectInfo.setCollectStatus(CollectStatusEnum.INIT.getCode());
        //更新项目报告状态为：初始状态
        projectInfo.setReportStatus(ReportStatusEnum.INIT.getCode());
        projectInfoService.updateById(projectInfo);
        return BaseResult.success(null, "结束收集操作成功");
    }
}
