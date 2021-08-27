package com.test.codediff;

import com.test.codediff.consts.FileConst;
import com.test.codediff.exceptions.FileException;
import com.test.codediff.utils.FileUtil;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

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
