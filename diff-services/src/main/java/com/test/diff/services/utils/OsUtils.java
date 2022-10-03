package com.test.diff.services.utils;

/**
 * @author wl
 * @date 2022/9/29
 */
public class OsUtils {

    public static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    public static final String WINDOWS_PREFIX = "window";
    public static final String LINUX_PREFIX = "linux";

    public static Boolean isWindows() {
        return OS_NAME.contains(WINDOWS_PREFIX);
    }

    public static Boolean isLinux() {
        return OS_NAME.contains(LINUX_PREFIX);
    }
}
