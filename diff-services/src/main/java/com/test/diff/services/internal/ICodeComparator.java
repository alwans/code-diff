package com.test.diff.services.internal;


import com.test.diff.common.domain.ClassInfo;
import org.eclipse.jgit.diff.DiffEntry;

/**
 * class文件，通过asm解析更方便
 * java文件，通过javaParser来解析
 * 2种解析方式，方便后期扩展
 * @author wl
 */
public interface ICodeComparator {

    /**
     * 通过diffEntry信息解析对应的类文件，返回类的修改的信息
     * @param diffEntry diff信息
     * @param oldFilePath 基线版本java文件路径
     * @param newFilePath 目标版本java文件路径
     * @return
     */
    public ClassInfo getDiffClassInfo(DiffEntry diffEntry, String oldFilePath, String newFilePath);
}
