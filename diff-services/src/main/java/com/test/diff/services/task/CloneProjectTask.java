package com.test.diff.services.task;


import com.sun.prism.impl.BaseResourceFactory;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.entity.RepoInfo;
import com.test.diff.services.exceptions.BizException;
import com.test.diff.services.internal.BaseRepository;
import com.test.diff.services.internal.RepositoryFactory;
import com.test.diff.services.params.CompileParamDto;
import com.test.diff.services.service.ProjectInfoService;
import com.test.diff.services.service.RepoInfoService;
import com.test.diff.services.utils.CMDUtils;
import com.test.diff.services.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author wl
 * @date 2022/9/30
 */
@Slf4j
@Component
public class CloneProjectTask {

    /**
     * clone失败的历史任务
     */
    public static final List<String> failedTasks = new ArrayList<>();

    /**
     * 克隆中的项目
     */
    public static final Map<String, String> taskMap = new ConcurrentHashMap<>();

    /**
     * 仓库实例
     */
    private static final Map<Integer, RepoInfo> repoInstances = new ConcurrentHashMap<>();

    @Resource
    private RepoInfoService repoInfoService;



    /**
     * 异步克隆工程代码
     * @param projectInfo 覆盖工程信息
     * @param branch 分支信息
     */
    @Async("compileExecutor")
    public void cloneProjectTask(ProjectInfo projectInfo, String branch){
        cloneProject(projectInfo, branch, null, true);
    }

    @Async("compileExecutor")
    public void cloneProjectTask(ProjectInfo projectInfo, String branch, String commitId){
        cloneProject(projectInfo, branch, commitId, true);
    }

    /**
     * 检查是否可以克隆
     * @param projectInfo
     */
    public void checkClone(ProjectInfo projectInfo,
                           String branch, String commitId){
        String compileKey = projectInfo.getProjectName() + projectInfo.getEnv()
                + branch + commitId;
        if (failedTasks.contains(projectInfo.getProjectName())){
            new BizException(projectInfo.getProjectName()+"工程克隆异常, 不再继续尝试");
        }
        synchronized (this){
            if (taskMap.get(compileKey) != null){
                throw new BizException(projectInfo.getProjectName()+"项目正在clone中..., 请稍后再试");
            }
            taskMap.put(compileKey, projectInfo.getProjectName());
            if (repoInstances.get(projectInfo.getRepoId()) == null){
                RepoInfo repoInfo = repoInfoService.getById(projectInfo.getRepoId());
                if (repoInfo == null){
                    throw new BizException(projectInfo.getProjectName() + "项目的仓库信息不存在,克隆失败");
                }
                repoInstances.put(projectInfo.getRepoId(), repoInfo);
            }
        }
    }

    /**
     * 克隆工程
     * @param projectInfo
     * @param branch
     * @param commitId
     * @param isNeedCompile 是否需要编译
     */
    public void cloneProject(ProjectInfo projectInfo, String branch, String commitId, Boolean isNeedCompile){
        // 手动调用clone检查
//        checkClone(coverageProject, branch, commitId);
        RepoInfo repoDto = repoInstances.get(projectInfo.getRepoId());
        String new_branch = "";
        if (StringUtils.isEmpty(commitId)){
            new_branch = branch;
        }else {
           new_branch = fixBranch(branch, commitId);
        }
        StopWatch stopWatch = new StopWatch();
        try {
            String repoPath = FileUtil.getRepoPath(projectInfo, new_branch);
            log.info("开始克隆"+projectInfo.getProjectName()+"工程,仓库路径: "+repoPath);
            stopWatch.start();
            BaseRepository repository = RepositoryFactory.create(repoDto);
            Git git = repository.clone(projectInfo.getProjectUrl(), repoPath, branch);
            git.getRepository().close();
            git.close();
            if (StringUtils.isNotEmpty(commitId)){
                repository.pullByCommitId(repoPath, branch, commitId);
            }
            stopWatch.stop();
            log.info("{}工程clone完成, 耗时: {}秒", projectInfo.getProjectName(), stopWatch.getTotalTimeSeconds());
        } catch (Exception e) {
            stopWatch.stop();
            failedTasks.add(projectInfo.getProjectName());
            log.error("克隆{}项目异常, 耗时: {}秒, 异常原因: {}", projectInfo.getProjectName(),
                    stopWatch.getTotalTimeSeconds(),
                    e.getMessage());
        } finally {
            String compileKey = projectInfo.getProjectName() + projectInfo.getEnv()
                    + branch + commitId;
            taskMap.remove(compileKey);
        }
        if (isNeedCompile){
            // 克隆完,顺便编译一下
            CompileParamDto compileParamDto = new CompileParamDto();
            compileParamDto.setId(projectInfo.getId().intValue());
            compileParamDto.setBranchName(branch);
            compileParamDto.setCommitId(commitId);
            compileSource(projectInfo, compileParamDto);
        }
    }

    public void compileSource(ProjectInfo projectInfo, CompileParamDto compileParamDto){
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String branchPath = compileParamDto.getBranchName();
        if (StringUtils.isNotEmpty(compileParamDto.getCommitId())){
            branchPath = fixBranch(compileParamDto.getBranchName(), compileParamDto.getCommitId());
        }
        String repoPath = FileUtil.getRepoPath(projectInfo, branchPath);
        log.info("{}工程开始编译...., 分支: {}, commit id: {}", projectInfo.getProjectName(),
                compileParamDto.getBranchName(),
                compileParamDto.getCommitId());
        // 判断代码是不是存在
        if (!new File(repoPath).exists()){
            log.info("{}项目的{}分支代码不存在,开始clone项目...", projectInfo.getProjectName(), repoPath);
            checkClone(projectInfo, compileParamDto.getBranchName(), compileParamDto.getCommitId());
            log.info("{}项目的{}克隆结束,开始编译项目...", projectInfo.getProjectName(), repoPath);
        }
        try {
            // 开始编译
            String execCommand = "cd "+repoPath+" && mvn clean package -Dmaven.test.skip=true";
            CMDUtils.CmdResult result = CMDUtils.exec(execCommand);
            if (result.getIsSuccess()) {
                log.info("{}工程编译成功", projectInfo.getProjectName());
            } else {
                log.error("{}工程编译失败, 编译信息: {}", projectInfo.getProjectName(), result.getContext());
            }
            stopWatch.stop();
            log.info("{}工程编译完成, 耗时：{}秒", projectInfo.getProjectName(), stopWatch.getTotalTimeSeconds());
        }finally {
            String compileKey = projectInfo.getProjectName() + projectInfo.getEnv()
                    + compileParamDto.getBranchName() + compileParamDto.getCommitId();
//            compileMap.remove(compileKey);
        }

    }

    @Bean("compileExecutor")
    public Executor compileExecutor(){
        return Executors.newFixedThreadPool(2, new ThreadFactory() {
            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("compile async thread --" + thread.getId());
                return thread;
            }
        });
    }


    public  String fixBranch(String branch, String commitId){
        if (branch.contains("/")){
            branch = branch.replaceAll("/", "_" );
        }
        return branch + "_" + commitId;
    }

}
