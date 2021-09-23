package com.test.diff.services;

import com.test.diff.common.util.JacksonUtil;
import org.jacoco.cli.internal.core.data.ChainNode;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JacksonTest {

    @Test
    public void testStreamSerialize() throws IOException {
        ChainNode node = new ChainNode();
        node.setUri("12dfj");
        String str = JacksonUtil.serialize(node);
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        String ss = new String(bytes, StandardCharsets.UTF_8);
        ChainNode n =  JacksonUtil.deserialize(ss, ChainNode.class);
//        ChainNode n = JacksonUtil.deserialize(bytes, ChainNode.class);
//        ChainNode n = JacksonUtil.deserialize(str, ChainNode.class);
        System.out.println(n.toString());
    }
}
