package com.test.diff.common.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * {@link HttpUtil} 基于{@link okhttp3.OkHttpClient}封装的http请求工具
 * 工具类来源于sandbox-repeater-core项目中的HttpUtil工具类
 * @author wl
 */
public class HttpUtil {

    private static final String QUESTION_SEPARATE = "?";

    private static final String PARAM_SEPARATE = "&";

    private static final String KV_SEPARATE = "=";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    public static Resp doGet(String url){
        return executeRequest(new Request.Builder().get().url(url).build());
    }

    public static Resp doGetWithHeader(String url , Map<String, String> headers){
        final Request.Builder builder = new Request.Builder().get().url(url);
        if(MapUtils.isNotEmpty(headers)){
            for(Map.Entry<String,String>  entry : headers.entrySet()){
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        return executeRequest(builder.build());
    }

    public static Resp doGet(String url, Map<String, String> params){
        StringBuilder builder = new StringBuilder(url);
        if(!StringUtils.contains(url, QUESTION_SEPARATE)){
            builder.append(QUESTION_SEPARATE).append("_r=1");
        }
        if(MapUtils.isNotEmpty(params)){
            for(Map.Entry<String, String>  entry: params.entrySet()){
                builder.append(PARAM_SEPARATE)
                        .append(entry.getKey())
                        .append(KV_SEPARATE)
                        .append(entry.getValue());
            }
        }
        return doGet(builder.toString());
    }

    public static Resp doPost(String url, Map<String, String> params){
        FormBody.Builder builder =new FormBody.Builder();
        if(MapUtils.isNotEmpty(params)){
            for(Map.Entry<String, String> entry : params.entrySet()){
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder().post(builder.build()).url(url).build();
        return executeRequest(request);
    }

    public static Resp doPost(String url, String jsonStr){
        RequestBody body = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder().post(body).url(url).build();
        return executeRequest(request);
    }

    public static Resp executeRequest(Request request){
        return executeRequest(request, 3);
    }

    public static Resp executeRequest(Request request, int retryTime){
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                return Resp.builder().code(response.code())
                        .result(body2Result(response.body())).build();
            }
            if(retryTime-- > 0){
                TimeUnit.MILLISECONDS.sleep(1000);
                executeRequest(request, retryTime);
            }
            return Resp.builder().code(response.code())
                    .result(body2Result(response.body()))
                    .message("Invoke failed, status code is not 200")
                    .build();
        } catch (Exception e) {
            if(retryTime-- > 0){
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException ex) {
                    // ignore
                }
                return  executeRequest(request, retryTime);
            }
            return  Resp.builder().code(500)
                        .message("Invoke occurred exception, request=" + request.toString() + ";message=" + e.getMessage())
                        .build();
        }
    }

    private static String body2String(ResponseBody body) throws IOException {
        return body == null ? "" : body.string();
    }

    private static Result body2Result(ResponseBody body) throws IOException {
        return body == null ? Result.builder().code(-1).build() :
                new GsonBuilder().create().fromJson(body.string(), Result.class);
    }

    @Builder
    @Getter
    @Setter
    public static class Resp{
        private int code;
        private Result result;
        private String message;

        @ConstructorProperties({"code", "result", "message"})
        Resp(int code, Result result, String message){
            this.code = code;
            this.result = result;
            this.message = message;
        }


        public boolean isSuccess(){
            return code >=200 && code <=300;
        }

    }

    @Builder
    @Getter
    @Setter
    public static class Result<T> {
        private int code;
        private T data;
        private String msg;
    }
}
