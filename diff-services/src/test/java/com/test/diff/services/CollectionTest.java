package com.test.diff.services;

import org.jacoco.cli.internal.core.data.ChainNode;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

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
    public void testFilter(){
        List<Boolean> list = new ArrayList<>();
        list.add(false);
        list.add(true);
        List<Boolean> new_list = list.stream().filter(item -> item).collect(Collectors.toList());
        System.out.println(list);
        System.out.println(new_list);
    }

    @Test
    public void testAnyMatch(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
//        boolean flag = list.stream().anyMatch(item -> item==1);
        boolean flag = list.stream().anyMatch(item -> { return item==1;} ); //2中写法
        System.out.println(list);
        System.out.println(flag);
    }

    @Test
    public void testArraySplit(){
//        String str = "String;String;Integer;";
        String str = "Ljava/lang/String;Lcom/beitai/iot/data/enums/SourceType;";
        String[] arr = str.split(";");
        System.out.println(arr);
    }

    @Test
    public void testRepeatFilter(){
        HashSet<String> set1 = new HashSet<>();
        HashSet<String> set2 = new HashSet<>();
        set1.add("a");
        set1.add("b");
        set1.add("c");
        //------------
        set2.add("a");
        set2.add("d");
        set1.addAll(set2);
        System.out.println(set1);
    }
}
