package com.test.diff.services;


import com.test.diff.services.consts.FileConst;
import com.test.diff.services.exceptions.FileException;
import com.test.diff.services.utils.FileUtil;
import org.junit.Test;

public class FileTest {

    @Test
    public void test1() throws FileException {
        new FileUtil().createDir(FileConst.DIFF_ROOT_PATH);
    }

    @Test
    public void filePathTest(){
        System.out.println(new FileUtil().addPath(FileConst.DIFF_ROOT_PATH, "server", "feature/v1.01"));
    }
}
