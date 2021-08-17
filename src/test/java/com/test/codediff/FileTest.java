package com.test.codediff;

import com.test.codediff.consts.FileConst;
import com.test.codediff.exceptions.FileException;
import com.test.codediff.utils.FileUtil;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

public class FileTest {

    @Test
    public void test1() throws FileException {
        new FileUtil().createDir(FileConst.ROOT_PATH);
    }
}
