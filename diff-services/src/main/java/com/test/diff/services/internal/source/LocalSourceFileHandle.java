package com.test.diff.services.internal.source;

import com.test.diff.services.consts.FileConst;
import com.test.diff.services.consts.GitConst;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.exceptions.BizException;
import com.test.diff.services.exceptions.FileException;
import com.test.diff.services.exceptions.GitException;
import com.test.diff.services.internal.BaseRepository;
import com.test.diff.services.internal.RepositoryFactory;
import com.test.diff.services.service.ProjectInfoService;
import com.test.diff.services.service.RepoInfoService;
import com.test.diff.services.utils.FileUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 拷贝项目文件到对应的code-diff目录中
 * @author wl
 */
@Slf4j
public class LocalSourceFileHandle implements ISourceFileHandle{

    private FileUtil fileUtil;

    private ProjectInfoService projectInfoService;

    private RepoInfoService repoInfoService;

    private String tempPath;

    private String tempBranchName;

    private ProjectInfo projectInfo;


    public LocalSourceFileHandle(FileUtil fileUtil,
                                 ProjectInfoService projectInfoService,
                                 RepoInfoService repoInfoService){
        this.fileUtil = fileUtil;
        this.projectInfoService = projectInfoService;
        this.repoInfoService =  repoInfoService;
    }

    @Override
    public String copyProjectFile(ReentrantLock lock, String branch, String originDir, int projectId) {
        lock.lock();
        try{
            //        getProjectName(originDir);
            if(StringUtils.isBlank(tempPath)){
                getTempPath(projectId, branch);
            }
            File parentDir = new File(originDir);
            if(!parentDir.exists()){
                log.info("{}文件路径不存在，请检查", originDir);
                throw new FileException(StatusCode.FILE_NOT_EXISTS);
            }
            copyGitFile(parentDir, branch, projectId);
            copySourceFile(parentDir, branch, projectId);
            copyClassFile(parentDir, branch, projectId);
            return renameBranchDir(getRepo(), branch);
        }finally {
            lock.unlock();
        }

    }


    private BaseRepository getRepo(){
        return RepositoryFactory.create(repoInfoService.getById(projectInfo.getRepoId()));
    }

    /**
     * 不再使用了，通过接口直接传递project id
     * @param
     * @return
     */
//    @Deprecated
//    private String getProjectName(String originDir){
//        String[] arr = originDir.split(FileConst.DIR_SPLIT_SEPARATOR);
//        projectName = arr[arr.length-1];
//        List<ProjectInfo> projectInfos = projectInfoService.getInfoByName(projectName);
//        if(projectInfos.size() != 1){
//            throw new BizException("项目名："+projectName+"存在多个或者不存在");
//        }
//        projectInfo = projectInfos.get(0);
//        return projectName;
//    }

    private String renameBranchDir(BaseRepository repository, String branch){
        String gitPath = fileUtil.addPath(tempPath, GitConst.GIT_FILE_SUFFIX);
        List<String> commits = new ArrayList<>();
        try {
            commits = repository.lsLocalCommitList(gitPath);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取{}的commits列表失败", gitPath);
            throw new GitException(StatusCode.GIT_LOCAL_COMMIT_ERROR);
        }
        String lastCommit = commits.get(0);
        String newPath = tempPath.replace(tempBranchName, branch.replace("/","_")+"_"+lastCommit);
        File tempFile = new File(tempPath);
        log.info("路径{}重命名为{}", tempPath, newPath);
        if(tempFile.exists()){
            File newFile = new File(newPath);
            if(newFile.exists()){
//                try {
//                    FileUtils.deleteDirectory(newFile);
//                } catch (IOException e) {
//                    log.error("删除旧文件夹：{}失败", newFile.getAbsolutePath());
//                    throw new FileException("删除文件夹失败");
//                }
                try {
                    FileUtils.deleteDirectory(tempFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("删除临时文件夹：{}失败", tempFile.getAbsolutePath(), e);
                }
            }else{
                tempFile.renameTo(newFile);
            }
        }
        return lastCommit;
    }


        private void copyGitFile(File parentDir, String branch, int projectId){
        File[] git = parentDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return GitConst.GIT_FILE_SUFFIX.equals(pathname.getName());
            }
        });
        if(git.length != 1){
            throw new FileException(".git文件不存在或存在多个");
        }
        log.info("{}文件开始复制...", git[0].getAbsolutePath());
        try {
            FileUtils.copyDirectory(git[0], getGitFile(projectId, branch));
            log.info("{}文件复制完成", git[0].getAbsolutePath());
        } catch (IOException e) {
            log.error("拷贝{}文件失败.", git[0].getAbsolutePath(), e);
            throw new FileException(StatusCode.File_ERROR);
        }
    }

    private File getGitFile(int projectId, String branch){
        return new File(fileUtil.addPath(tempPath, GitConst.GIT_FILE_SUFFIX));
    }

    private void copyClassFile(File parentDir, String branch, int projectId){
        List<String> classesPaths =  fileUtil.getAllClassFilePathsByProject(parentDir.getAbsolutePath());
        log.info("{}项目开始复制class文件...", projectInfo.getProjectName());
        classesPaths.stream()
                .forEach(classesPath -> {
                    File ori = new File(classesPath);
                    File target = new File(classesPath.replace(parentDir.getAbsolutePath(), tempPath));
                    try {
                        FileUtils.copyDirectory(ori, target);
                    } catch (IOException e) {
                        log.error("{}目录拷贝文件失败", ori.getAbsolutePath(), e);
                        throw new FileException(StatusCode.File_ERROR);
                    }
                });
        log.info("{}项目的class文件复制完成", projectInfo.getProjectName());
    }

    private String getTempPath(int projectId, String branch){
        if(Objects.isNull(projectInfo)){
            projectInfo = projectInfoService.getById(projectId);
            if(Objects.isNull(projectInfo)){
                throw new BizException(StatusCode.PROJECT_INFO_NOT_EXISTS);
            }
        }
        tempBranchName = branch.replace("/", "_") + "_temp_" + System.currentTimeMillis();
        tempPath = fileUtil.getRepoPath(projectInfo, tempBranchName);
        return tempPath;
    }

    private void copySourceFile(File parentDir, String branch, int projectId){
        List<String> sourcePaths =  fileUtil.getAllSourcePathsByProject(parentDir.getAbsolutePath());
        log.info("{}项目开始复制java文件...", projectInfo.getProjectName());
        sourcePaths.stream()
                .forEach(sourcePath -> {
                    File ori = new File(sourcePath);
                    File target = new File(sourcePath.replace(parentDir.getAbsolutePath(), tempPath));
                    try {
                        FileUtils.copyDirectory(ori, target);
                    } catch (IOException e) {
                        log.error("{}目录拷贝文件失败", ori.getAbsolutePath(), e);
                        throw new FileException(StatusCode.File_ERROR);
                    }
                });
        log.info("{}项目的java文件复制完成", projectInfo.getProjectName());
    }



}
