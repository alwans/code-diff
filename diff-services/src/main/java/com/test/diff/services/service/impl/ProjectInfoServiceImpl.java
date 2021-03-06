package com.test.diff.services.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.diff.services.consts.FileConst;
import com.test.diff.services.consts.JacocoConst;
import com.test.diff.services.convert.ModelConvert;
import com.test.diff.services.convert.ProjectVoConvert;
import com.test.diff.services.entity.CoverageApp;
import com.test.diff.services.entity.CoverageReport;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.enums.CollectStatusEnum;
import com.test.diff.services.exceptions.FileException;
import com.test.diff.services.internal.jacoco.JacocoHandle;
import com.test.diff.services.params.ListProjectParams;
import com.test.diff.services.service.CoverageReportService;
import com.test.diff.services.service.ProjectInfoService;
import com.test.diff.services.mapper.ProjectInfoMapper;
import com.test.diff.services.utils.FileUtil;
import com.test.diff.services.vo.ProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jacoco.cli.internal.core.tools.ExecFileLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
@Slf4j
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo>
    implements ProjectInfoService {

    @Value("${server.port}")
    private String localPort;

    @Resource
    private FileUtil fileUtil;

    @Resource
    private ProjectInfoMapper projectInfoMapper;

    @Resource
    private CoverageReportService coverageReportService;

    @Resource
    private ModelConvert<ProjectInfo, ProjectVo> projectVoModelConvert;

    @Override
    public Long create(ProjectInfo projectInfo) {
        projectInfo.setIsDelete(false);
        projectInfo.setIsDisable(false);
        projectInfo.setAddTime(new Date());
        projectInfo.setLastTime(new Date());
        projectInfoMapper.insert(projectInfo);
        return projectInfo.getId();
    }

    @Override
    public List<ProjectInfo> getInfoByName(String projectName) {
        LambdaQueryWrapper<ProjectInfo> query = new LambdaQueryWrapper();
        query.eq(ProjectInfo::getProjectName, projectName);
        query.eq(ProjectInfo::getIsDelete, false);
        return projectInfoMapper.selectList(query);
    }

    @Override
    public void pullExecData(ProjectInfo projectInfo) {
        CoverageReport report = coverageReportService.selectUsedByProjectId(projectInfo.getId());
        if(Objects.isNull(report)){
            log.info("{}????????????????????????????????????", projectInfo.getProjectName());
            return ;
        }
        ProjectVo projectVo = projectVoModelConvert.convert(projectInfo);
        if(CollectionUtils.isEmpty(projectVo.getApps())){
            log.info("{}????????????????????????????????????", projectVo.getProjectName());
            return;
        }
        projectVo.getApps().stream()
                .filter(coverageApp -> coverageApp.getStatus())
                .forEach(coverageApp -> {
                    pullSingleExec(projectInfo, coverageApp, report);
                });
    }

    @Override
    public void pullExecData(ProjectInfo projectInfo, CoverageApp coverageApp) {
        CoverageReport report = coverageReportService.selectUsedByProjectId(projectInfo.getId());
        if(Objects.isNull(report)){
            log.error("{}????????????????????????????????????", projectInfo.getProjectName());
            return ;
        }
        pullSingleExec(projectInfo, coverageApp, report);
    }

    private void pullSingleExec(ProjectInfo projectInfo, CoverageApp  coverageApp, CoverageReport report){
        log.info("??????????????????{}??????????????????", coverageApp.getAppName());
        String execName = "dump-"+System.currentTimeMillis()+".exec";
        //1.???????????????????????????uuid?????????
        String execUuidPath = dumpData(coverageApp, projectInfo, report.getUuid(), execName);
        //2.??????uuid????????????exec??????
        mergeExec(execUuidPath, projectInfo.getId());
        //3.????????????dump????????????????????????????????????????????? && ????????????????????????exec???????????????????????????
        String oriExecPath = fileUtil.addPath(execUuidPath, execName);
        String branchDir = getBranchPath(projectInfo,
                fileUtil.addPath(execUuidPath, JacocoConst.DEFAULT_EXEC_FILE_NAME));
        try {
            FileUtils.moveFileToDirectory(new File(oriExecPath), new File(branchDir), true);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("{}???????????????{}????????????, ???????????????????????????????????????exec??????", oriExecPath, branchDir, e);
            return;
        }
        mergeExec(branchDir, projectInfo.getId());
        deleteOldExec(branchDir);
        log.info("{}????????????????????????????????????...", coverageApp.getAppName());
    }


    private void deleteOldExec(String dirPath){
        File dir = new File(dirPath);
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(JacocoConst.EXEC_FILE_SUFFIX) &&
                        !pathname.getAbsolutePath().contains(JacocoConst.DEFAULT_EXEC_FILE_NAME);
            }
        });
        for(File file: files){
            file.delete();
        }
    }

    private void mergeExec(String dirPath, long projectId){
        File dir = new File(dirPath);
        if(!dir.exists()){
            log.error("???????????????{}???????????????", dir.getAbsolutePath());
            throw new RuntimeException("???????????????");
        }
        File[] execFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(JacocoConst.EXEC_FILE_SUFFIX);
            }
        });
        if(Objects.isNull(execFiles) || execFiles.length == 0){
            log.info("{}???????????????exec??????");
            return;
        }
        String newFilePath = fileUtil.addPath(dirPath, JacocoConst.EXEC_MERGE_FILE_NAME);
        try {
            JacocoHandle.merge(projectId, localPort, newFilePath, execFiles);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("??????{}????????????exec????????????", dirPath, e);
            throw new RuntimeException("??????????????????");
        }
        //????????????dump.exc????????????????????????merge.exec???????????????dump.exec
        String dumpPath = fileUtil.addPath(dirPath, JacocoConst.DEFAULT_EXEC_FILE_NAME);
        fileUtil.reName(dumpPath, newFilePath);
    }

    private String dumpData(CoverageApp app, ProjectInfo projectInfo, String uuid, String execName){
        String uuidDir = fileUtil.getRepoPath(projectInfo, uuid);
        String execPath = fileUtil.addPath(uuidDir, execName);
        if(!fileUtil.isExist(uuidDir)){
            new File(uuidDir).mkdir();
        }
        try {
            JacocoHandle.dump(app.getHost(), app.getPort(), execPath);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("dump {}?????????exec????????????", projectInfo.getProjectName(), e);
            throw new RuntimeException("dump "+projectInfo.getProjectName()+"?????????exec????????????");
        }
        return uuidDir;
    }

    /**
     * branchDir ????????????????????????????????????
     * ???????????????????????????????????????????????????????????????commit id?????????
     * ????????????????????????????????????????????????class??????
     * ?????????????????????jenkins??????????????????????????????????????????????????????????????????????????????
     * @param projectInfo
     * @param execFilePath
     * @return
     */
    private String getBranchPath(ProjectInfo projectInfo, String execFilePath){
        ExecFileLoader loader = JacocoHandle.getLoader(execFilePath);
        if(Objects.isNull(loader)){

        }
        String branch = JacocoHandle.getBranchName(loader);
        String commit = JacocoHandle.getCommitId(loader);
        String branchPath = fileUtil.getRepoPath(projectInfo, branch+"_"+commit);
        if(!fileUtil.isExist(branchPath)){
            log.error("{}??????????????????", branchPath);
            throw new RuntimeException(branchPath+"??????????????????");
        }
        return branchPath;
    }

    @Override
    public List<ProjectInfo> getCollectProject() {
        LambdaQueryWrapper<ProjectInfo> query = new LambdaQueryWrapper();
        query.eq(ProjectInfo::getCollectStatus, CollectStatusEnum.COLLECTING.getCode());
        return projectInfoMapper.selectList(query);
    }

    @Override
    public Page<ProjectInfo> selectListByParams(ListProjectParams params) {
        LambdaQueryWrapper<ProjectInfo> query = new LambdaQueryWrapper<>();
        if(params.getProjectId() != 0){
            query.eq(ProjectInfo::getId, params.getProjectId());
        }
        return projectInfoMapper.selectPage(new Page<>(params.getPage(), params.getSize()), query);
    }
}




