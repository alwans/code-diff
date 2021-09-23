package com.test.diff.services.controller;

import com.test.diff.services.base.controller.result.BaseResult;
import com.test.diff.services.entity.CoverageReport;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.entity.RepoInfo;
import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.internal.BaseRepository;
import com.test.diff.services.internal.DiffWorkFlow;
import com.test.diff.services.internal.RepositoryFactory;
import com.test.diff.services.params.ProjectBranchParams;
import com.test.diff.services.params.ProjectParams;
import com.test.diff.services.service.CoverageReportService;
import com.test.diff.services.service.ProjectInfoService;
import com.test.diff.services.service.RepoInfoService;
import com.test.diff.services.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/git")
@Slf4j
public class GitController {

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private RepoInfoService repoInfoService;


    @Resource
    private FileUtil fileUtil;

    private static final String DEFAULT_BRANCH = "master";

    @GetMapping("/branch/list")
    public BaseResult getBranchList(@Validated ProjectParams projectParam){
        ProjectInfo projectInfo = projectInfoService.getById(projectParam.getProjectId());
        if(Objects.isNull(projectInfo)){
            return BaseResult.error(StatusCode.PARAMS_ERROR, "id="+projectParam.getProjectId()+" 的项目不存在");
        }
        RepoInfo repoInfo = repoInfoService.getById(projectInfo.getRepoId());
        BaseRepository repository = RepositoryFactory.create(repoInfo);
        String path = fileUtil.getRepoPath(projectInfo, DEFAULT_BRANCH);
        if(!fileUtil.isExist(path)){
            repository.clone(projectInfo.getProjectUrl(), path, DEFAULT_BRANCH);
        }
        String gitPath = fileUtil.addPath(path, ".git");
        List<String> branchList = repository.lsLocalBranchList(gitPath);
        return BaseResult.success(branchList);
    }

    @GetMapping(value = "/commit/list")
    public BaseResult getCommitList(@Validated ProjectParams params){
        ProjectInfo projectInfo = projectInfoService.getById(params.getProjectId());
        if(Objects.isNull(projectInfo)){
            return BaseResult.error(StatusCode.PARAMS_ERROR, "id=" + params.getProjectId()+" 的项目不存在");
        }
        RepoInfo repoInfo = repoInfoService.getById(projectInfo.getRepoId());
        BaseRepository repository = RepositoryFactory.create(repoInfo);
        String path = fileUtil.getRepoPath(projectInfo, DEFAULT_BRANCH);
        String gitPath = fileUtil.addPath(path, ".git");
        if(!fileUtil.isExist(path)){
            repository.clone(projectInfo.getProjectUrl(), path, DEFAULT_BRANCH);
        }else{
            repository.pull(gitPath, DEFAULT_BRANCH);
        }
        try {
            List<String> commits = repository.lsLocalCommitList(gitPath);
            return BaseResult.success(commits);
        } catch (IOException e) {
            log.error("获取{}项目的commit记录失败", projectInfo.getProjectName(), e);
            return BaseResult.error(StatusCode.GIT_LOCAL_COMMIT_ERROR);
        }
    }
}
