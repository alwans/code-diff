package com.test.diff.services.consts;

import java.io.File;

public class FileConst {

    public static final String DEFAULT_EMPTY_PATH = "";

    public static final String DEFAULT_DIFF_DIR_NAME = "code-diff";

    public static final String USER_HOME_PATH = System.getProperty("user.home");

    public static final String DIFF_ROOT_PATH = USER_HOME_PATH + File.separator + DEFAULT_DIFF_DIR_NAME;

    public static final String JAVA_SOURCE_DIR_NAME = "src";

    public static final String JAVA_CLASS_FILE_PARENT_DIR_NAME = "target";

    public static final String JAVA_CLASS_FILE_DIR_NAME = "classes";

    /**
     * 用来切割路径，在win和linux通用的正则表达式
     */
    public static final String DIR_SPLIT_SEPARATOR = "/|\\\\";


}
