package com.test.diff.services;


import org.eclipse.jgit.api.errors.GitAPIException;
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
    public void test1() throws IOException, GitAPIException {
        String clone_uri  = "https://github.com/alwans/diff-demo.git";

        String local_path = "G:\\jvm\\diff";

        String local_git_file = local_path+ ".git";

        String name = "alwanstest@163.com";
        String passwd = "";

//        Git.cloneRepository()
//                .setURI(clone_uri)
//                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(name,passwd))
//                .setBranch("master")
//                .setDirectory(new File(local_path)).call();
        // 打开一个存在的仓库
        Repository existingRepo = new FileRepositoryBuilder()
                .setGitDir(new File(local_git_file))
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
