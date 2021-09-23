package com.test.diff.services.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.diff.common.util.JacksonUtil;
import com.test.diff.services.base.controller.result.BaseResult;
import com.test.diff.services.consts.FileConst;
import com.test.diff.services.consts.GitConst;
import com.test.diff.services.consts.JacocoConst;
import com.test.diff.services.convert.ModelConvert;
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
import com.test.diff.services.service.CoverageReportService;
import com.test.diff.services.mapper.CoverageReportMapper;
import com.test.diff.services.service.ProjectInfoService;
import com.test.diff.services.utils.CommonUtil;
import com.test.diff.services.utils.FileUtil;
import com.test.diff.services.vo.ProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Result;
import org.jacoco.cli.internal.JacocoApi;
import org.jacoco.cli.internal.core.tools.ExecFileLoader;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class CoverageReportServiceImpl extends ServiceImpl<CoverageReportMapper, CoverageReport>
    implements CoverageReportService{


    @Resource
    private CoverageReportMapper coverageReportMapper;

    @Resource
    private ProjectInfoService projectInfoService;

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

    /**
     * 这个方法要重新写
     * @param params
     * @return
     */
    @Override
    public BaseResult report(ReportParams params) {
        ProjectInfo projectInfo = projectInfoService.getById(params.getProjectId());
        if(Objects.isNull(projectInfo)){
            return BaseResult.error(StatusCode.PARAMS_ERROR, "id= "+params.getProjectId()+" 的项目不存在");
        }
        //设置报告状态为：生成中
        log.info("{}项目的报告在生成中...", projectInfo.getProjectName());
        projectInfo.setReportStatus(ReportStatusEnum.REPORT_GENERATING.getCode());
        projectInfoService.updateById(projectInfo);

        CoverageReport report = selectUsedByProjectId(params.getProjectId());
        //没有使用中的记录，说明没有点开始收集，而是手机点击生成报告。所以先创建一条使用的report记录
        if(Objects.isNull(report)){
            create(params.getProjectId());
        }
        //最后收集合并一次数据
        projectInfoService.pullExecData(projectInfo);

        //生成报告
        String uuidDir = fileUtil.getRepoPath(projectInfo, report.getUuid());
        String execFilePath = fileUtil.addPath(uuidDir, JacocoConst.DEFAULT_EXEC_FILE_NAME);
        ExecFileLoader loader = JacocoHandle.getLoader(execFilePath);
        String branch = JacocoHandle.getBranchName(loader);
        String commitId = JacocoHandle.getCommitId(loader);
        log.info("获取分支：{}，commit id:{}", branch, commitId);

        String branchDir = fileUtil.addPath(fileUtil.getRepoPath(projectInfo, branch+"_"+commitId));
        List<String> sourcePaths = fileUtil.getAllSourcePathsByProject(branchDir);
        List<String> classFilePaths = fileUtil.getAllClassFilePathsByProject(branchDir);

        try {
            String diffResult = null;
            if(params.getReportType() == ReportTypeEnum.INCREMENT.getCode()){
                ProjectDiffParams diffParams = new ProjectDiffParams();
                diffParams.setId(params.getProjectId());
                diffParams.setDiffTypeCode(params.getDiffType());
                //分支diff
                if(params.getDiffType() == DiffTypeEnum.BRANCH_DIFF.getCode()){
                    log.info("开始生成分支diff的增量报告...");
                    diffParams.setOldVersion(params.getOldVersion());
                    diffParams.setNewVersion(branch);
                    report.setNewBranch(branch);
                    report.setOldBranch(params.getOldVersion());
                    report.setReportType(ReportTypeEnum.INCREMENT.getCode());
                    report.setDiffType(DiffTypeEnum.BRANCH_DIFF.getCode());
                }
                //commit diff；暂时不考虑diff类型不存在的情况
                else{
                    log.info("开始生成commitId diff的增量报告...");
                    diffParams.setOldVersion(branch);
                    diffParams.setNewVersion(branch);
                    diffParams.setOldCommitId(params.getOldVersion());
                    diffParams.setNewCommitId(commitId);
                    //----------更新report
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
            JacocoHandle.report(execFilePath, classFilePaths, sourcePaths, uuidDir, diffResult);
            log.info("报告生成完成！");
            projectInfo.setReportStatus(ReportStatusEnum.REPORT_SUCCESS.getCode());
            projectInfoService.updateById(projectInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}项目报告生成失败", projectInfo.getProjectName(), e);
            projectInfo.setReportStatus(ReportStatusEnum.REPORT_FAILED.getCode());
            projectInfoService.updateById(projectInfo);
            return BaseResult.error(StatusCode.OTHER_ERROR, "报告生成失败");
        }
        report.setLastTime(new Date());
        coverageReportMapper.updateById(report);
        log.info("{}项目报告生成成功", projectInfo.getProjectName());
        return BaseResult.success("操作成功");
    }
}




