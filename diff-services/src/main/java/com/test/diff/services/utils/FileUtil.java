package com.test.diff.services.utils;

import com.test.diff.services.exceptions.FileException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

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
            path = path.replaceAll("/", "_");
            sb.append(File.separator);
            sb.append(path);
        }
        return sb.toString();
    }
}
