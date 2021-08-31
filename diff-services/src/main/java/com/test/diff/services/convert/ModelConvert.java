package com.test.diff.services.convert;

public interface ModelConvert<S, T>{

    /**
     * 模型转换
     * @param source 源模型
     * @return 目标模型
     */
    T convert(S source);

    /**
     * 模型恢复
     * @param target 目标模型
     * @return 源模型
     */
    S reconvert(T target);
}
