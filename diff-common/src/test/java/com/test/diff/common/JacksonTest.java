package com.test.diff.common;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.diff.common.util.JacksonUtil;
import lombok.Data;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class JacksonTest {

    int deep = 50;
    int count = 1;
    Node tailNode = null;
    @Test
    public void testOutputStreamSerialize() throws FileNotFoundException, JsonProcessingException {
        Node head = new Node();
        head.setUri("com/icai/common/redis/RedisUtils.setRedisProperties(Lcom/icai/common/redis/RedisProperties;)V--" +count);
        count++;
        getNode(head);
//        System.out.println(tailNode);
//        String str = JacksonUtil.serialize(tailNode);
        String str = JSON.toJSONString(tailNode);
        System.out.println(str.length());
    }

    private void getNode(Node preNode) {
        Node node = new Node();
        node.setPreNode(preNode);
        node.setCalledNode(preNode);
        node.setUri("com/beitai/common/redis/RedisUtils.setRedisProperties(Lcom/beitai/common/redis/RedisProperties;)V--" +count);
        count++;
        if(count<=deep){
            deep--;
            getNode(node);
        }
        else {
            tailNode = node;
        }
    }
}

@Data
class Node {

    private Node preNode;

    private Node calledNode;

    private String uri;
}
