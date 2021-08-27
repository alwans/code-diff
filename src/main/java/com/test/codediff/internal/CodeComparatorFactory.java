package com.test.codediff.internal;


/**
 * 这里获取需要的代码解析器
 * @author wl
 */
public class CodeComparatorFactory {

    public static ICodeComparator createCodeComparator(){
        return new JavaFileCodeComparator();
    }
}
