package com.test.diff.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class JacksonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setDateFormat(sdf);
    }

    public static String serialize(Object object) throws JsonProcessingException {
        return serialize(object, false);
    }

    public static String serialize(Object object, boolean pretty) throws JsonProcessingException {
        return pretty ? mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object) : mapper.writeValueAsString(object);
    }

    public static byte[] serialize2Bytes(Object object) throws JsonProcessingException {
        return mapper.writeValueAsBytes(object);
    }

    public static void serializeByStream(OutputStream writer, Object object) throws IOException {
        mapper.writeValue(writer, object);
    }


    public static <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        return mapper.readValue(bytes, type);
    }

    public static <T> T deserialize(String sequence, Class<T> type) throws IOException {
        return mapper.readValue(sequence, type);
    }

    public static <T> T deserialize(InputStream reader, Class<T> type) throws IOException {
        return mapper.readValue(reader, type);
    }

    public static <T> List<T> deserializeArray(byte[] bytes, Class<T> type) throws IOException {
        return mapper.readValue(bytes, getCollectionType(ArrayList.class, type));
    }

    public static <T> List<T> deserializeArray(String sequence, Class<T> type) throws IOException {
        return mapper.readValue(sequence, getCollectionType(ArrayList.class, type));
    }

    public static <T> List<T> deserializeArray(InputStream reader, Class<T> type) throws IOException {
        return mapper.readValue(reader, getCollectionType(ArrayList.class, type));
    }

    private static JavaType getCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClasses){
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}
