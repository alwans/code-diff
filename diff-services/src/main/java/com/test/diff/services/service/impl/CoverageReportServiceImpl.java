package com.test.diff.services.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.diff.common.util.CollectionUtil;
import com.test.diff.common.util.JacksonUtil;
import com.test.diff.services.base.controller.result.BaseResult;
import com.test.diff.services.consts.FileConst;
import com.test.diff.services.consts.GitConst;
import com.test.diff.services.consts.JacocoConst;
import com.test.diff.services.convert.ModelConvert;
import com.test.diff.services.entity.CoverageApp;
import com.test.diff.services.entity.CoverageReport;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.enums.DiffTypeEnum;
import com.test.diff.services.enums.ReportStatusEnum;
import com.test.diff.services.enums.ReportTypeEnum;
import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.internal.DiffWorkFlow;
import com.test.diff.services.internal.jacoco.JacocoHandle;
import com.test.diff.services.params.ProjectDiffParams;
import com.test.diff.services.params.ReportParams;
import com.test.diff.services.service.CoverageAppService;
import com.test.diff.services.service.CoverageReportService;
import com.test.diff.services.mapper.CoverageReportMapper;
import com.test.diff.services.service.ProjectInfoService;
import com.test.diff.services.utils.CommonUtil;
import com.test.diff.services.utils.FileUtil;
import com.test.diff.services.utils.WildcardMatcher;
import com.test.diff.services.vo.ProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Result;
import org.jacoco.cli.internal.JacocoApi;
import org.jacoco.cli.internal.core.tools.ExecFileLoader;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CoverageReportServiceImpl extends ServiceImpl<CoverageReportMapper, CoverageReport>
    implements CoverageReportService{

    @Value("${report.domain}")
    private String reportDomain;

    @Resource
    private CoverageReportMapper coverageReportMapper;

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private CoverageAppService coverageAppService;

    @Resource
    private FileUtil fileUtil;

    @Resource
    private DiffWorkFlow diffWorkFlow;

    @Override
    public int create(int projectId) {
        CoverageReport report = new CoverageReport();
        report.setProjectId(projectId);
        report.setUuid(CommonUtil.getUUID());
        report.setAddTime(new Date());
        report.setLastTime(new Date());
        return coverageReportMapper.insert(report);
    }

    @Override
    public CoverageReport selectUsedByProjectId(long projectId) {
        LambdaQueryWrapper<CoverageReport> query = new LambdaQueryWrapper<>();
        query.eq(CoverageReport::getProjectId, projectId);
        query.eq(CoverageReport::getIsUsed, true);
        query.eq(CoverageReport::getIsDelete, false);
        CoverageReport report;
        report = coverageReportMapper.selectOne(query);
        return report;
    }

    @Override
    public List<CoverageReport> selectListByProjectId(long projectId, boolean isDesc) {
        LambdaQueryWrapper<CoverageReport> query = new LambdaQueryWrapper<>();
        query.eq(CoverageReport::getProjectId, projectId);
        query.eq(CoverageReport::getIsDelete, false);
        if(isDesc){
            query.orderByDesc(CoverageReport::getId);
        }
        return coverageReportMapper.selectList(query);
    }

    /**
     * ????????????????????????
     * @param params
     * @return
     */
    @Override
    public BaseResult report(ReportParams params) {
        ProjectInfo projectInfo = projectInfoService.getById(params.getProjectId());
        if(Objects.isNull(projectInfo)){
            return BaseResult.error(StatusCode.PARAMS_ERROR, "id= "+params.getProjectId()+" ??????????????????");
        }
        //?????????????????????????????????
        log.info("{}???????????????????????????...", projectInfo.getProjectName());
        projectInfo.setReportStatus(ReportStatusEnum.REPORT_GENERATING.getCode());
        projectInfoService.updateById(projectInfo);

        CoverageReport report = selectUsedByProjectId(params.getProjectId());
        //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????report??????
        if(Objects.isNull(report)){
            create(params.getProjectId());
        }
        //??????????????????????????????
        projectInfoService.pullExecData(projectInfo);

        //????????????
        String uuidDir = fileUtil.getRepoPath(projectInfo, report.getUuid());
        String execFilePath = fileUtil.addPath(uuidDir, JacocoConst.DEFAULT_EXEC_FILE_NAME);
        ExecFileLoader loader = JacocoHandle.getLoader(execFilePath);
        String branch = JacocoHandle.getBranchName(loader);
        String commitId = JacocoHandle.getCommitId(loader);
        log.info("???????????????{}???commit id:{}", branch, commitId);

        String branchDir = fileUtil.addPath(fileUtil.getRepoPath(projectInfo, branch+"_"+commitId));
        List<String> sourcePaths = fileUtil.getAllSourcePathsByProject(branchDir);
        List<String> classFilePaths = fileUtil.getAllClassFilePathsByProject(branchDir);

        try {
            String diffResult = null;
            if(params.getReportType() == ReportTypeEnum.INCREMENT.getCode()){
                ProjectDiffParams diffParams = new ProjectDiffParams();
                diffParams.setId(params.getProjectId());
                diffParams.setDiffTypeCode(params.getDiffType());
                //??????diff
                if(params.getDiffType() == DiffTypeEnum.BRANCH_DIFF.getCode()){
                    log.info("??????????????????diff???????????????...");
                    diffParams.setOldVersion(params.getOldVersion());
                    diffParams.setNewVersion(branch);
                    report.setNewBranch(branch);
                    report.setOldBranch(params.getOldVersion());
                    report.setReportType(ReportTypeEnum.INCREMENT.getCode());
                    report.setDiffType(DiffTypeEnum.BRANCH_DIFF.getCode());
                }
                //commit diff??????????????????diff????????????????????????
                else{
                    log.info("????????????commitId diff???????????????...");
                    diffParams.setOldVersion(branch);
                    diffParams.setNewVersion(branch);
                    diffParams.setOldCommitId(params.getOldVersion());
                    diffParams.setNewCommitId(commitId);
                    //----------??????report
                    report.setNewBranch(commitId);
                    report.setOldBranch(params.getOldVersion());
                    report.setReportType(ReportTypeEnum.INCREMENT.getCode());
                    report.setDiffType(DiffTypeEnum.COMMIT_DIFF.getCode());
                }
                diffResult = JacksonUtil.serialize(diffWorkFlow.diff(diffParams));
            }
            else{
                report.setNewBranch(branch);
                report.setReportType(ReportTypeEnum.FULL.getCode());
            }
            //??????????????????
            List<String> filters = getAppFilterRules(projectInfo.getId());
            String filterRules = JacksonUtil.serialize(filters);

            JacocoHandle.report(execFilePath, classFilePaths, sourcePaths, uuidDir, diffResult, filterRules);
            log.info("?????????????????????");
            projectInfo.setReportStatus(ReportStatusEnum.REPORT_SUCCESS.getCode());
            projectInfoService.updateById(projectInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}????????????????????????", projectInfo.getProjectName(), e);
            projectInfo.setReportStatus(ReportStatusEnum.REPORT_FAILED.getCode());
            projectInfoService.updateById(projectInfo);
            return BaseResult.error(StatusCode.OTHER_ERROR, "??????????????????");
        }
        report.setLastTime(new Date());
        report.setReportUri(uuidDir);
        coverageReportMapper.updateById(report);
        log.info("{}????????????????????????", projectInfo.getProjectName());
        return BaseResult.success("????????????");
    }

    @Override
    public BaseResult getReportURI(long projectId) {
        ProjectInfo projectInfo = projectInfoService.getById(projectId);
        if(Objects.isNull(projectInfo)){
            return BaseResult.error(StatusCode.PARAMS_ERROR, "id= "+projectId+" ??????????????????");
        }
        CoverageReport report = selectUsedByProjectId(projectId);
        if(Objects.isNull(report)){
            List<CoverageReport> historyReports = selectListByProjectId(projectId, true);
            if(CollectionUtils.isEmpty(historyReports)){
                return BaseResult.error(StatusCode.REPORT_NOT_EXISTS);
            }
            CoverageReport lastReport = historyReports.get(historyReports.size()-1);
            if(StringUtils.isEmpty(lastReport.getReportUri())){
                return BaseResult.error(StatusCode.REPORT_NOT_EXISTS, "??????????????????????????????????????????");
            }
            return BaseResult.success(reportDomain + lastReport.getReportUri() + "/index.html");
        }
        if(StringUtils.isEmpty(report.getReportUri())){
            return BaseResult.error(StatusCode.REPORT_NOT_EXISTS, "?????????????????????????????????");
        }
        //????????????????????????windows??????
        return BaseResult.success(reportDomain + report.getReportUri() + "/index.html");
    }

    /**
     * ?????????????????????????????????????????????????????????
     * @param projectId
     * @return ??????list?????????[app1-includes, app1-excludes, app2-includes, app2-excludes, ...]
     */
    private List<String> getAppFilterRules(long projectId){
        List<CoverageApp> apps = coverageAppService.getListByProjectId(projectId).stream()
                .filter(app -> app.getStatus())
                .collect(Collectors.toList());
        List<String> list = new ArrayList();
        for(CoverageApp app: apps){
            list.add(app.getIncludes());
            list.add(app.getExcludes()==null? "" : app.getExcludes());
        }
        return list;
    }


    @Deprecated //(files???????????????????????????????????????????????????????????????????????????) = ??????
    private List<String> filter(List<String> files, ProjectInfo projectInfo){
        //?????????????????????????????????????????????
        List<CoverageApp> apps = coverageAppService.getListByProjectId(projectInfo.getId()).stream()
                .filter(app -> app.getStatus()).collect(Collectors.toList());
        List<String> list = new ArrayList();
        //??????????????????
        for(String file: files){
            //??????????????????????????????includes && excludes ??????????????????????????????
            boolean flag = apps.stream().anyMatch(app -> {
                WildcardMatcher includes = new WildcardMatcher(app.getIncludes());
                WildcardMatcher excludes = new WildcardMatcher(app.getExcludes());
                return includes.matches(file) &&
                        !excludes.matches(file);
            });
            if(flag){
                list.add(file);
            }
        }
        return list;
    }
}




