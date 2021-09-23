package com.test.diff.services.internal;


import com.test.diff.services.consts.GitConst;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.entity.RepoInfo;
import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.exceptions.FileException;
import com.test.diff.services.exceptions.GitException;
import com.test.diff.services.utils.FileUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.file.GC;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.pack.PackConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @author wl
 */
@Slf4j
@Getter
@Setter
public class GitRepository extends BaseRepository {

    public GitRepository(RepoInfo repoInfo){super(repoInfo);}

    @Override
    public Git clone(String url, String projectLocalPath, String branch){
        new FileUtil().createDir(projectLocalPath);
        try {
            return Git.cloneRepository()
                    .setURI(url)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(super.getRepoInfo().getUserName(),
                            super.getRepoInfo().getPasswd()))
                    .setBranch(branch)
                    .setDirectory(new File(projectLocalPath))
                    .call();
        } catch (GitAPIException e) {
            log.error("clone project:{} is failed", url);
            e.printStackTrace();
            throw new GitException(StatusCode.GIT_CLONE_ERROR);
        }
//        log.info("clone success: {}", url);
    }

    @Override
    public void pull(String local_git_path, String branch) {
        if(!local_git_path.endsWith(GitConst.GIT_FILE_SUFFIX)){
            throw new FileException(StatusCode.FILE_GIT_FILE_ERROR);
        }
        try {
            Git git = getGit(local_git_path);
            git.pull()
                .setRemoteBranchName(branch)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(super.getRepoInfo().getUserName(),
                        super.getRepoInfo().getPasswd()))
                .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            log.error("拉取代码失败，git path: {}", local_git_path, e);
            throw new GitException(StatusCode.GIT_PULL_CODE_ERROR);
        }
    }

    private Git getGit(String localGitPath)  {
        File gitFile = new File(localGitPath);
        if(!gitFile.exists()){
            throw new FileException(localGitPath+"对应分支.git文件不存在，请先clone到本地");
        }
        Git git;
        try {
            git = new Git(new FileRepository(localGitPath));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取git对象失败，git path: {}", localGitPath, e);
            throw new GitException(StatusCode.GIT_PULL_CODE_ERROR);
        }
        return  git;
    }

    @Override
    public void pullByCommitId(String local_git_path, String branch, String commitId)  {
        try {
            Git git = getGit(local_git_path);
            //先拉取最新的代码
            pull(local_git_path, branch);
            //回滚到指定的commit id 版本
            git.reset()
                    .setMode(ResetCommand.ResetType.HARD)
                    .setRef(commitId)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            log.error("{}分支回滚代码到commit id: {} 失败", local_git_path, commitId, e);
            throw new GitException(StatusCode.GIT_COMMIT_PULL_CODE_ERROR);
        }
    }

    @Override
    public List<String> lsRemoteBranchList(String url) {
        return null;
    }

    @Override
    public List<String> lsLocalBranchList(String local_git_path){
        //先更新代码
//        pull(local_git_path, branch);

        Git git = getGit(local_git_path);
        try {
            return git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call().stream()
                    .map(Ref::getName)
                    .filter(branchStr -> !branchStr.contains("refs/heads"))
                    .map(branchStr -> branchStr.replace("refs/remotes/origin/",""))
                    .collect(toList());
        } catch (GitAPIException e) {
            e.printStackTrace();
            log.error("获取本地分支列表失败", e);
            throw new GitException(StatusCode.GIT_LOCAL_BRANCH_ERROR);
        }
    }

    @Override
    public List<String> lsLocalTagList(String local_git_path, String branch)  {
        //先更新代码
        pull(local_git_path, branch);

        Git git = getGit(local_git_path);
        try {
            return git.tagList().call().stream().map(Ref::getName).collect(toList());
        } catch (GitAPIException e) {
            e.printStackTrace();
            log.error("获取本地tag列表失败", e);
            throw new GitException(StatusCode.GIT_LOCAL_TAG_ERROR);
        }
    }

    @Override
    public List<String> lsLocalCommitList(String local_git_path) throws IOException {
        Repository repo = new FileRepository(local_git_path);
        Git git = new Git(repo);
        List<String> list = new ArrayList<>();
        try {
//            Iterable<RevCommit> commits = git.log().add(repo.resolve(branch)).call();
            Iterable<RevCommit> commits = git.log().call();
            for(RevCommit commit: commits){
                list.add(commit.getId().getName());
            }
        } catch (GitAPIException e) {
            log.error("{} 获取commit 信息失败", local_git_path, e);
            throw new GitException("获取commit信息失败");
        }finally {
            repo.close();
            git.close();
        }
        return list;
    }


    @Override
    public Collection<Ref> refsList(String url) {
        Collection<Ref> refList;
        try {
            refList = Git.lsRemoteRepository()
                    .setRemote(url)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(super.getRepoInfo().getUserName(),
                            super.getRepoInfo().getPasswd()))
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            log.error("获取项目：{}失败", url, e);
            throw new GitException(StatusCode.GIT_REMOTE_BRANCH_ERROR);
        }
        return refList;
    }

    @Override
    public AbstractTreeIterator prepareTreeParser(Repository repository, String branch) {
        RevWalk revWalk = new RevWalk(repository);
        RevTree tree;
        try {
            if(Objects.isNull(repository.resolve(branch))){
                throw new GitException(StatusCode.GIT_BRANCH_NOT_EXISTS);
            }
            tree = revWalk.parseTree(repository.resolve(branch));
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            revWalk.dispose();
            return treeParser;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("{}分支代码生成解析树失败", branch, e);
            throw new GitException(StatusCode.GIT_PARSER_TREE_ERROR);
        }
    }

    public Collection<Ref> branchList(ProjectInfo projectInfo) {
        return refsList(projectInfo.getProjectUrl());
    }


}
