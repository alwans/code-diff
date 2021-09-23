package com.test.diff.common;


import com.google.gson.JsonObject;
import com.test.diff.common.util.CollectionUtil;
import com.test.diff.common.util.HttpUtil;
import okhttp3.HttpUrl;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpTest {


    @Test
    public void testPost(){
        String url = "http://127.0.0.1:1990/api/diff/getDiffResult";
//        JsonObject json = new JsonObject();
//        json.addProperty("id", 3);
//        json.addProperty("newVersion", "master");
//        json.addProperty("oldVersion", "master");
//        json.addProperty("diffTypeCode", 1);
//        json.addProperty("oldCommitId", "17b29503b740d0e09339a02dd214b2d925c890a8");
//        json.addProperty("newCommitId", "35c27bd393e1f92efb757dfff8d2029bd3163849");
        Map<String,String> data = new HashMap<>();
        data.put("id", "3");
        data.put("newVersion", "master");
        data.put("oldVersion", "master");
        data.put("diffTypeCode", "1");
        data.put("oldCommitId", "17b29503b740d0e09339a02dd214b2d925c890a8");
        data.put("newCommitId", "35c27bd393e1f92efb757dfff8d2029bd3163849");

        HttpUtil.Resp resp = HttpUtil.doPost(url, CollectionUtil.map2JsonString(data));
        if(!resp.isSuccess()){
            System.out.println("请求失败");
        }
        HttpUtil.Result result = resp.getResult();
        System.out.println(result.getData().toString());

    }
}
