package com.test.diff.services;

import org.jacoco.cli.internal.core.data.ChainNode;
import org.junit.Test;

import java.util.*;

public class CollectionTest {

    @Test
    public void testSetClear(){
        Set<ChainNode> sets = new HashSet<>();
        ChainNode node = new ChainNode();
        node.setUri("test");
        sets.add(node);
        System.out.println(sets.size());
        sets.clear();
        System.out.println(sets.size());
    }

    @Test
    public void testSetArrayClear(){
        Set[] sets = new Set[10];
        for(int i = 0;i<sets.length;i++){
            sets[i] = new HashSet<String>();
            sets[i].add("test");
        }
        Arrays.stream(sets).forEach(item ->{
            item.clear();
        });
        System.out.println(sets);
    }

    @Test
    public void testStream(){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.stream().filter(item -> item.equals("a"))
                .forEach(item -> System.out.println(item));
    }

    @Test
    public void testArraySplit(){
//        String str = "String;String;Integer;";
        String str = "Ljava/lang/String;Lcom/beitai/iot/data/enums/SourceType;";
        String[] arr = str.split(";");
        System.out.println(arr);
    }
}
