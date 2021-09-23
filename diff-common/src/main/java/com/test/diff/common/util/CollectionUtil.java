package com.test.diff.common.util;

import com.google.gson.JsonObject;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * @author wl
 */
public class CollectionUtil {

    public static String map2JsonString(Map<String, String> params){
        if(MapUtils.isNotEmpty(params)){
            JsonObject json = new JsonObject();
            for(Map.Entry<String, String> entry: params.entrySet()){
                json.addProperty(entry.getKey(), entry.getValue());
            }
            return json.toString();
        }
        return "";
    }
}
