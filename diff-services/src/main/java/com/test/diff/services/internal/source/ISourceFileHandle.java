package com.test.diff.services.internal.source;


import java.util.concurrent.locks.ReentrantLock;

/**
 * 操作项目文件：java文件和class文件
 * @author wl
 */
public interface ISourceFileHandle {

    /**
     * 拷贝项目的源码和class文件到对应版本目录文件夹下
     * @param branch 分支名
     * @param originDir 项目父级目录：原始文件路径
     * @return String 返回对应的commit id 信息
     */
    public String copyProjectFile(ReentrantLock lock, String branch, String originDir, int projectId);
}
