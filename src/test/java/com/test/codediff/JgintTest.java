package com.test.codediff;


import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;


import java.io.File;
import java.io.IOException;

public class JgintTest {

    @Test
    public void test1() throws IOException {
        // 打开一个存在的仓库
        Repository existingRepo = new FileRepositoryBuilder()
                .setGitDir(new File("git@202.101.187.185:13622/test_new/Tester.git"))
                .build();

        // 获取引用
        Ref master = existingRepo.getRefDatabase().findRef("master");

        // 获取该引用所指向的对象
        ObjectId masterTip = master.getObjectId();
        // Rev-parse
        ObjectId obj = existingRepo.resolve("HEAD^{tree}");

        // 装载对象原始内容
        ObjectLoader loader = existingRepo.open(masterTip);
        loader.copyTo(System.out);

    }
}
