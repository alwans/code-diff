package com.test.codediff.consts;

import java.io.File;

public class FileConst {

    public static final String DEFAULT_EMPTY_PATH = "";

    public static final String DEFAULT_DIFF_DIR_NAME = "code-diff";

    public static final String USER_HOME_PATH = System.getProperties().getProperty("user.home");

    public static final String DIFF_ROOT_PATH = USER_HOME_PATH + File.separator + DEFAULT_DIFF_DIR_NAME;



}
