package com.test.diff.services.controller;

import com.test.diff.services.consts.FileConst;
import com.test.diff.services.consts.GitConst;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.internal.source.SourceFileHandleFactory;
import com.test.diff.services.params.FileParams;
import com.test.diff.services.service.ProjectInfoService;
import com.test.diff.services.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.test.diff.services.base.controller.result.BaseResult;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("api/file")
@Slf4j
public class FileController {

    private static final HashMap<Integer, ReentrantLock> copyLocks = new HashMap<>();

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private FileUtil fileUtil;

    @PostMapping(value = "/class/path", produces = "application/json;charset=UTF-8")
    public BaseResult getFileClassPath(@RequestBody @Validated FileParams fileParams){
        ProjectInfo projectInfo = projectInfoService.getById(fileParams.getId());
        if(Objects.isNull(projectInfo)){
            return BaseResult.error(StatusCode.PROJECT_INFO_NOT_EXISTS);
        }
        String repoPath;
        if(StringUtils.isNotBlank(fileParams.getCommitId())){
            repoPath = fileUtil.getRepoCommitPath(projectInfo, fileParams.getBranch(), fileParams.getCommitId());
        }else{
            repoPath =  fileUtil.getRepoPath(projectInfo, fileParams.getBranch());
        }
        String classSubPath = fileParams.getClassName() + GitConst.CLASS_FILE_SUFFIX;
        String path = fileUtil.getClassFilePath(repoPath, classSubPath);
        if(!Objects.isNull(path) && fileUtil.isExist(path)){
            return BaseResult.success(path);
        }
        log.error("未找到{} class文件路径，工程id: {}, branch: {}, commitId: {}", fileParams.getId(), fileParams.getClassName(),
                fileParams.getBranch(), fileParams.getCommitId());
        return BaseResult.error(StatusCode.OTHER_ERROR, "class文件不存在");
    }

    @GetMapping( "copy/project")
    public BaseResult copyProjectFile(@RequestParam("path") String path,
                                      @RequestParam("branch") String branch,
                                      @RequestParam("id") int projectId){
        ReentrantLock lock = null;
        synchronized (this){
            lock = copyLocks.get(projectId);
            if(Objects.isNull(lock)){
                lock = new ReentrantLock();
                copyLocks.put(projectId, lock);
            }
        }
        String commitId = SourceFileHandleFactory.build().copyProjectFile(lock, branch, path, projectId);
        return BaseResult.success(commitId);
    }
}
