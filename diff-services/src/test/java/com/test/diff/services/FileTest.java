package com.test.diff.services;


import com.test.diff.services.consts.FileConst;
import com.test.diff.services.consts.GitConst;
import com.test.diff.services.exceptions.FileException;
import com.test.diff.services.utils.FileUtil;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import java.io.*;
import java.nio.Buffer;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class FileTest {

    private FileUtil fileUtil;

    @Before
    public void before(){
        fileUtil = new FileUtil();
    }

    @Test
    public void testPrintHomePath() throws FileException {
        System.out.println(FileConst.USER_HOME_PATH);
    }

    @Test
    public void testAddPath(){

        String path ;
        String projectName = "jvmTest";
        String branch = "master";
        String commitId = "17b29503b740d0e09339a02dd214b2d925c890a8";
        String className = "com/jvm/test/controller/TestController";
        branch = branch+"_"+commitId;
        System.out.println(FileConst.DIFF_ROOT_PATH);
        path = fileUtil.addPath(FileConst.DIFF_ROOT_PATH, projectName, branch, "target", "classes");
        if(className.contains("/")){
            String[] arr = className.split("/");
            path = fileUtil.addPaths(path, arr);
        }
        path = path + GitConst.CLASS_FILE_SUFFIX;
        if(new File(path).exists()){
            System.out.println("yes");
        }
        System.out.println(path);
    }

    @Test
    public void testCopy() throws IOException {
        String oriPath = fileUtil.addPath(FileConst.DIFF_ROOT_PATH, "1");
        String newPath = fileUtil.addPath(FileConst.DIFF_ROOT_PATH, "2");
        String path = fileUtil.addPath(FileConst.DIFF_ROOT_PATH, "3");
        File oldFile = new File(oriPath);
        File newFile = new File(newPath);
        FileUtils.copyDirectory(oldFile, newFile);
        newFile.renameTo(new File(path));
    }

    @Test
    public void testFilePathTest(){
        System.out.println(new FileUtil().addPath(FileConst.DIFF_ROOT_PATH, "server", "feature/v1.01"));
    }

    @Test
    public void testRename(){
        String newPath = "C:\\Users\\wl\\code-diff\\dump.txt";
        String oldPath = "C:\\Users\\wl\\code-diff\\merge.txt";
        fileUtil.reName(newPath, oldPath);
    }

    @Test
    public void testDelDir() throws IOException {
        FileUtils.deleteDirectory(new File("C:\\Users\\wl\\code-diff\\jvmTest\\master_35c27bd393e1f92efb757dfff8d2029bd3163849"));
    }

    @Test
    public void testAsyncWrite()   {
        ReentrantLock lock = new ReentrantLock();
        File file = new File("C:\\Users\\wl\\code-diff\\12.txt");
        Thread t1 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(100);
                int pos = 10;
                lock.lock();
                try{
                    while(pos-->0){
                        try {
                            FileUtils.write(file, "t1 write..."+pos+"----", true);
                            FileUtils.write(file, "\r\n", true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }finally {
                    lock.unlock();
                }

            }
        };
        Thread t2 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(300);
                int pos = 10;
                lock.lock();
                try{
                    while(pos-->0){
                        try {
                            FileUtils.write(file, "t2 write..."+pos+"----", true);
                            FileUtils.write(file, "\r\n", true);
                        } catch (IOException  e) {
                            e.printStackTrace();
                        }
                    }
                }finally {
                    lock.unlock();
                }
            }
        };

        t1.start();
        t2.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }

    @Test
    public void testSourcePaths(){
        String branPath = "G:\\jvm\\gitlab\\jvmTest";
        List<String> list = fileUtil.getAllSourcePathsByProject(branPath);
        System.out.println(list.size());
        list.stream().forEach(path -> System.out.println(path));
    }

    @Test
    public void testClassPaths(){
        String branPath = "C:\\Users\\wl\\code-diff\\11\\yami-server";
        List<String> list = fileUtil.getAllClassFilePathsByProject(branPath);
        System.out.println(list.size());
        list.stream().forEach(path -> System.out.println(path));
    }

    @Test
    public void testCopyProject() throws IOException {
        String oriPath = "C:\\Users\\wl\\code-diff\\yami-server";
        String newPath = "C:\\Users\\wl\\code-diff\\122";
        //拷贝.git目录
        String oriGitPath = fileUtil.addPath(oriPath, ".git");
        String newGitPath = fileUtil.addPath(newPath, ".git");
        FileUtils.copyDirectory(new File(oriGitPath), new File(newGitPath));
        //拷贝java文件
        List<String> oriJavaPathList = fileUtil.getAllSourcePathsByProject(oriPath);
        oriJavaPathList.stream()
                .forEach(oriJavaPath -> {
                    File oriJavaDir = new File(oriJavaPath);
                    File newJavaDir = new File(oriJavaPath.replace(oriPath, newPath));
                    try {
                        FileUtils.copyDirectory(oriJavaDir, newJavaDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        //拷贝classes文件
        List<String> oriClassPathList = fileUtil.getAllClassFilePathsByProject(oriPath);
        oriClassPathList.stream()
                .forEach(oriClassPath ->{
                    File oriClassDir = new File(oriClassPath);
                    File newClassDir = new File(oriClassPath.replace(oriPath, newPath));
                    try {
                        FileUtils.copyDirectory(oriClassDir, newClassDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    public void testFindSelectedClassFilePath(){
        String key = "CoverageAppServiceImpl.class";
        if(!key.contains(File.separator)){
            key = key.replace("/", File.separator);
        }
        System.out.println(key);
        String path = fileUtil.getClassFilePath("G:\\jvm\\github\\code-diff",
                key);
        System.out.println(path);
    }

}
