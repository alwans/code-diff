package com.test.diff.services;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StringTest {

    @Test
    public void testReplace(){
        String str = "/root/code-diff/wuchan/test/scm-server/feature_1.0_f7eb21efa925a7588ae424ea3033d3805936fb23/sc-support/sc-job/src/main/java";
        str = str.replaceAll("/", ".");
        System.out.println(str);
    }

    @Test
    public void testIterator(){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        for(int i=0; i<list.size(); i++){
            System.out.println("-----");
            System.out.println(list.get(i));
            System.out.println(list.get(++i));
        }
    }

    @Test
    public void testDemo(){
        ThreadLocal<List<String>> local = new ThreadLocal<>();
        System.out.println(local.get()==null);
        local.set(new ArrayList<>());
        System.out.println(local.get());
    }

    @Test
    public void testLen(){
        System.out.println("".length());
    }
}
