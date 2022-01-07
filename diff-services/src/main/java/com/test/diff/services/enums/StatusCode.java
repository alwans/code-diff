package com.test.diff.services.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {

    SUCCESS(10000, "成功"),
    PARAMS_ERROR(10010, "参数错误"),
    OTHER_ERROR(-1, "其他错误"),
    UNKNOWN_COLLECT_STATUS(10030, "未知的收集状态"),
    UNKNOWN_REPORT_STATUS(10040, "未知的报告状态"),

    //git相关错误
    GIT_ERROR(20010, "git操作异常"),
    DIFF_TYPE_ERROR(20020, "比较类型不存在"),
    GIT_CLONE_ERROR(20030, "克隆项目失败"),
    GIT_PULL_CODE_ERROR(20040, "拉取代码失败"),
    GIT_COMMIT_PULL_CODE_ERROR(20050, "拉取指定commitId代码失败"),
    GIT_LOCAL_BRANCH_ERROR(20060, "获取本地分支列表失败"),
    GIT_LOCAL_TAG_ERROR(20070, "获取tag列表失败"),
    GIT_REMOTE_BRANCH_ERROR(20080, "获取remote分支列表失败"),
    GIT_BRANCH_NOT_EXISTS(20090, "分支不存在"),
    GIT_PARSER_TREE_ERROR(20100, "生成代码解析树失败"),
    GIT_REPO_TYPE_NOT_EXISTS(20200, "仓库类型不存在"),
    GIT_LOCAL_COMMIT_ERROR(20210, "获取本地commit列表失败"),


    //解析相关错误
    JAVA_PARSER_ERROR(20030, "java文件解析异常"),
    CLASS_PARSER_ERROR(20040, "class文件解析异常"),

    //数据库信息相关
    PROJECT_INFO_NOT_EXISTS(30010, "项目信息不存在"),
    REPO_INFO_NOT_EXITS(30020, "项目对应的仓库信息不存在"),
    APP_INFO_NOT_EXISTS(30030, "应用信息不存在"),
    REPORT_NOT_EXISTS(30040, "报告不存在"),

    //操作文件相关
    File_ERROR(40010, "文件操作异常"),
    FILE_GIT_FILE_ERROR(40020, "git文件路径错误"),
    FILE_NOT_EXISTS(40020, "文件不存在"),

    //操作jacoco相关
    JACOCO_DUMP_ERROR(50010, "jacoco dump 数据失败"),
    JACOCO_MERGE_ERROR(50020, "jacoco merge 数据失败"),
    JACOCO_REPORT_ERROR(50030, "jacoco report 生成失败");


    private int code;
    private String desc;
}
