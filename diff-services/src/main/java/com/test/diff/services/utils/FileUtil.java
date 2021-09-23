package com.test.diff.services.utils;

import com.test.diff.services.consts.FileConst;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.exceptions.FileException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wl
 */
@Component
@Slf4j
public class FileUtil {

    public void createDir(String path, String dirName) throws FileException {
        createDir(path + dirName);
    }
    public void createDir(String path) throws FileException {
        File targetDir = new File(path);
        if(!targetDir.exists()){
            targetDir.mkdir();
            return;
        }
        try {
            FileUtils.cleanDirectory(targetDir);
        } catch (IOException e) {
            log.error("清空{}文件夹失败", targetDir.getAbsolutePath(), e);
            e.printStackTrace();
            throw new FileException("清空文件夹异常");
        }
    }

    /**
     * 合并文件路径
     * @param subPath
     * @return
     */
    public String addPath(String... subPath){
        StringBuilder sb = new StringBuilder();
        for(String path: subPath){
            if(StringUtils.isBlank(path)){
                continue;
            }
            if(path.contains("/") && !path.startsWith("/")){
                path = path.replaceAll("/", "_");
            }
            sb.append(path);
            sb.append(File.separator);
        }
        return sb.toString().substring(0, sb.lastIndexOf(File.separator));
    }

    public String addPaths(String root, String... sunPath){
        StringBuilder path = new StringBuilder(root);
        for(String p: sunPath){
            p = p.replaceAll("/", "_");
            path.append(File.separator)
                    .append(p);
        }
        if(path.toString().endsWith(File.separator)){
            return path.toString().substring(0, path.lastIndexOf(File.separator));
        }
        return path.toString();
    }


    /**
     * 自动创建仓库本地路径
     * @param projectName
     * @param branch
     * @return
     */
    public String getRepoPath(String group, String env, String projectName, String branch){
        if(branch.contains("/")){
            branch = branch.replaceAll("/", "_");
        }
        return addPath(FileConst.DIFF_ROOT_PATH, group, env, projectName, branch);
    }

    public String getRepoPath(ProjectInfo projectInfo, String branch){
        return getRepoPath(projectInfo.getProjectGroup(), projectInfo.getEnv(),
                projectInfo.getProjectName(),
                branch);
    }

    public boolean isExist(String path){
        return new File(path).exists() ? true : false;
    }

    public void reName(String newPath, String oldPath){
        File newFile = new File(newPath);
        if(newFile.exists()){
            newFile.delete();
        }
        new File(oldPath).renameTo(newFile);
    }

    public List<String> getAllSourcePathsByProject(String branchDirPath){
        return getListDirByKey(branchDirPath, addPath("src", "main", "java"));
    }

    public List<String> getAllClassFilePathsByProject(String branchDirPath){
        return getListDirByKey(branchDirPath,
                addPath(FileConst.JAVA_CLASS_FILE_PARENT_DIR_NAME,
                        FileConst.JAVA_CLASS_FILE_DIR_NAME));
    }

    public List<String> getListDirByKey(String dirPath, String key){
        List<String> path = new ArrayList<>();
        File branchDir = new File(dirPath);
        File[] dirs = branchDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for(File dir: dirs){
            if(dir.isDirectory() && dir.getAbsolutePath().contains(key)){
                path.add(dir.getAbsolutePath());
                return path;
            }
            else if(dir.isDirectory()){
                List<String> subPath = getListDirByKey(dir.getAbsolutePath(), key);
                path.addAll(subPath);
            }
        }
        return  path;
    }


}
