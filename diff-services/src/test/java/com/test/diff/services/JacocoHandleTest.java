package com.test.diff.services;

import com.test.diff.services.internal.jacoco.JacocoHandle;

import org.jacoco.cli.internal.core.data.ChainNode;
import org.junit.Test;

import java.io.*;

public class JacocoHandleTest {

    @Test
    public void testDump() throws Exception {
        File execfile = new File(
                "C:\\Users\\wl\\Desktop\\dump2.exec");

        JacocoHandle.dump("192.168.100.83", "6300", execfile.getAbsolutePath());
    }

    @Test
    public void testMerge() throws Exception {
        String newEexcFilePath = "C:\\Users\\wl\\Desktop\\merge.exec";
        File[] files = new File("C:\\Users\\wl\\Desktop").listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(".exec");
            }
        });
        JacocoHandle.merge(newEexcFilePath, files);
    }

    @Test
    public void testChainNode() throws IOException, ClassNotFoundException {
        File file = new File("C:\\Users\\wl\\Desktop\\obj.txt");
        InputStream is = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(is);
        ChainNode node =(ChainNode) ois.readObject();
        System.out.println(node);
    }

}
