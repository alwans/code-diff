package com.test.diff.services;

import com.test.diff.common.domain.ClassInfo;
import com.test.diff.services.consts.FileConst;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.entity.RepoInfo;
import com.test.diff.services.enums.DiffTypeEnum;
import com.test.diff.services.exceptions.FileException;
import com.test.diff.services.exceptions.GitException;
import com.test.diff.services.internal.BaseRepository;
import com.test.diff.services.internal.DiffWorkFlow;
import com.test.diff.services.internal.RefHandle;
import com.test.diff.services.internal.RepositoryFactory;
import com.test.diff.services.params.ProjectDiffParams;
import com.test.diff.services.service.ProjectInfoService;
import com.test.diff.services.service.RepoInfoService;
import com.test.diff.services.utils.FileUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class GitWorkFlowTest {

    @Resource
    private RepoInfoService repoInfoService;

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private DiffWorkFlow diffWorkFlow;

    @Resource
    private RefHandle refHandle;

    @Resource
    private FileUtil fileUtil;

    @Test
    public void testGitClone(){
        int projectId = 1;
        String branch = "master";
        ProjectInfo projectInfo = projectInfoService.getById(projectId);
        RepoInfo repoInfo = repoInfoService.getById(projectInfo.getRepoId());
        BaseRepository repository = RepositoryFactory.create(repoInfo);
        String path = fileUtil.getRepoPath(projectInfo, branch);
        repository.clone(projectInfo.getProjectUrl(), path, branch);
    }

    @Test
    public void testGitPull(){
        int projectId = 3;
        String branch = "master";
        ProjectInfo projectInfo = projectInfoService.getById(projectId);
        RepoInfo repoInfo = repoInfoService.getById(projectInfo.getRepoId());
        BaseRepository repository = RepositoryFactory.create(repoInfo);
        String path = fileUtil.getRepoPath(projectInfo, branch);
        path = fileUtil.addPath(path, ".git");
        repository.pull(path, branch);
    }

    @Test
    public void testGetBranchList() throws GitException, FileException {
//        String branch = "master";
//        ProjectInfo projectInfo = projectInfoService.getById(1);
//        RepoInfo repoInfo = repoInfoService.getById(projectInfo.getRepoId());
//        BaseRepository repository = RepositoryFactory.create(repoInfo);
//        Collection<Ref> refs = repository.refsList(projectInfo.getProjectUrl());
//        System.out.println(refHandle.getBranchNameList(refs));
//        System.out.println(refHandle.getTagNameList(refs));
//        String path = fileUtil.getRepoPath(projectInfo, branch);
//        fileUtil.createDir(path);
//        long start = System.currentTimeMillis();
//        repository.clone(projectInfo.getProjectUrl(), path, branch);
//        System.out.println(System.currentTimeMillis()-start);
        String branch = "master";
        ProjectInfo projectInfo = projectInfoService.getById(1);
        RepoInfo repoInfo = repoInfoService.getById(projectInfo.getRepoId());
        BaseRepository repository = RepositoryFactory.create(repoInfo);
        String path = fileUtil.getRepoPath(projectInfo, branch);
        String gitPath = fileUtil.addPath(path, ".git");
        List<String> branchs = repository.lsLocalBranchList(gitPath);
        branchs.stream()
                .forEach(branchStr -> System.out.println(branchStr));
    }

    @Test
    public void testGetCommitList() throws IOException, GitAPIException {
        String branch = "master";
        ProjectInfo projectInfo = projectInfoService.getById(1);
        String brandDir = fileUtil.getRepoPath(projectInfo, branch);
//        String gitPath = fileUtil.addPath(brandDir, ".git");
        String gitPath = "C:\\Users\\wl\\code-diff\\11\\yami-server\\.git";
        Repository repo = new FileRepository(gitPath);
        Git git = new Git(repo);
        RevWalk walk = new RevWalk(repo);
        for(Ref ref: git.branchList().call()){
            System.out.println("branchName: "+ ref.getName());
        }
        Iterable<RevCommit> commits = git.log().call();
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
    public void testDiff() throws GitAPIException, IOException {
        String baseVersion = "master";
        String newVersion = "feature/v1.1.1";
        ProjectDiffParams params = new ProjectDiffParams();
        params.setId(1);
        params.setOldVersion(baseVersion);
        params.setNewVersion(newVersion);
        params.setOldCommitId("122155db3626a4b39b8be5254bfe65e8139e39a2");
        params.setNewCommitId("13509e9ddc4ea6ec240a7bd4628aeb82cfec9365");
//        params.setDiffTypeCode(DiffTypeEnum.BRANCH_DIFF.getCode());
        params.setDiffTypeCode(DiffTypeEnum.BRANCH_DIFF.getCode());
        params.setUpdateCode(true);

        List<ClassInfo>  classInfos = diffWorkFlow.diff(params);

        System.out.println(classInfos);

    }


}
