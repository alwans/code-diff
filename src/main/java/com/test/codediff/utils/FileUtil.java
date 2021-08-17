package com.test.codediff.utils;

import com.test.codediff.exceptions.FileException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.IOException;

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
}
