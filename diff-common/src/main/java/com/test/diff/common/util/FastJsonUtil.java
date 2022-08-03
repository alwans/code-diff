package com.test.diff.common.util;

import com.alibaba.fastjson2.JSON;

/**
 * @author wl
 * @date 2022/8/2
 */
public class FastJsonUtil {


    public static String serialize (Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T deserialize (String str, Class<T> type) {
        return JSON.parseObject(str, type);
    }
}
