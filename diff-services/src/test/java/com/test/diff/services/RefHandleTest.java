package com.test.diff.services;


import com.test.diff.services.consts.FileConst;
import com.test.diff.services.utils.FileUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class RefHandleTest {

    @Test
    public void test() throws IOException, GitAPIException {
        String branch = "develop";
        String projectName = "diff-code";
        String gitPath = new FileUtil().addPath(FileConst.DIFF_ROOT_PATH, projectName, branch, ".git");
        Repository repo = new FileRepository(gitPath);
        Git git = new Git(repo);
        RevWalk walk = new RevWalk(repo);
        for(Ref ref: git.branchList().call()){
            System.out.println("branchName: "+ ref.getName());
        }
        Iterable<RevCommit> commits = git.log().add(repo.resolve(branch)).call();
        for(RevCommit commit: commits){
            Date commitDate = commit.getAuthorIdent().getWhen();
            String commitUser = commit.getAuthorIdent().getName();
            String commitMsg =  commit.getFullMessage();
            String commitId  = commit.getId().getName();
            System.out.println("commitId:"+commitId+", commit user: "+commitUser+
                    " , commit date: "+commitDate+ " ,commit msg: "+commitMsg);
        }
    }

    @Test
    public void CommitTest() throws IOException, GitAPIException {
        String branch = "develop";
        String projectName = "diff-code";
        String gitPath = new FileUtil().addPath(FileConst.DIFF_ROOT_PATH, projectName, branch, ".git");
        Repository repo = new FileRepository(gitPath);
        Git git = new Git(repo);
        RevWalk walk = new RevWalk(repo);
        git.reset().setMode(ResetCommand.ResetType.HARD).setRef("75fea70f5276edd0d79e8bf4acdaadea3217f09c").call();
//        Iterable<RevCommit> refCommits = git.log().add(repo.resolve(branch)).call();
//        for(RevCommit commit: refCommits){
//            if(commit.getId().getName().equals("32878bf7f1dfbb5bc3073b65afc7248026456979")){
//                git.reset().setMode(ResetCommand.ResetType.HARD).setRef(commit.getName()).call();
//                System.out.println("更新完成");
//                break;
//            }
//        }
    }


}
