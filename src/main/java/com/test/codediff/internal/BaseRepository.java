package com.test.codediff.internal;

import com.test.codediff.entity.RepoInfo;
import com.test.codediff.exceptions.FileException;
import com.test.codediff.exceptions.GitException;
import lombok.Data;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author wl
 */
@Data
public abstract class BaseRepository {

    private RepoInfo repoInfo;

    BaseRepository(RepoInfo repoInfo){
        this.repoInfo = repoInfo;
    }

    /**
     * 克隆项目
     * @param url 远程仓库项目http地址
     * @param project_local_path  项目本地存储路径
     * @param branch 项目分支
     * @return Git
     * @throws GitException  git操作异常
     * @throws FileException 文件操作异常
     */
    public abstract Git clone(String url, String project_local_path, String branch);

    /**
     * 拉取最新代码
     * @param local_git_path 本地.git文件的路径
     * @param branch 需要拉取的分支名
     * @throws FileException 如果需要指定的分支未拉取过，抛异常
     * @throws GitException 拉取失败异常
     */
    public abstract void pull(String local_git_path, String branch);

    /**
     * 根据commit id 回滚代码
     * @param local_git_path 对应分支的.git文件路径
     * @param branch 分支名
     * @param commitId 需要回归的commit id
     * @throws FileException  文件操作异常
     * @throws GitException  git操作异常
     */
    public abstract void pullByCommitId(String local_git_path, String branch, String commitId);

    /**
     * 获取项目远程仓库中的分支列表
     * @param url 远程仓库中项目http地址
     * @return 远程分支利列表
     */
    public abstract List<String> lsRemoteBranchList(String url);

    /**
     * 获取本地分支列表
     * @param local_git_path 对应项目分支的.git文件路径
     * @param branch 分支名
     * @return 本地分支列表
     * @throws FileException 文件操作异常
     * @throws GitException git操作异常
     */
    public abstract List<String> lsLocalBranchList(String local_git_path, String branch);

    /**
     * 获取项目tag列表
     * @param local_git_path 对应项目分支的.git文件路径
     * @param branch 分支名
     * @return 本地tag列表
     * @throws FileException 文件操作异常
     * @throws GitException git操作异常
     */
    public abstract List<String> lsLocalTagList(String local_git_path, String branch);

    /**
     * 获取项目的分支列表
     * @param url 远程仓库项目http地址
     * @return 分支列表
     * @throws GitException git相关通用异常
     */
    public abstract Collection<Ref> refsList(String url);

    public abstract AbstractTreeIterator prepareTreeParser(Repository repository, String branch);


}
