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
    public void getBranchListTest() throws GitException, FileException {
        String branch = "develop";
        ProjectInfo projectInfo = projectInfoService.getById(2);
        RepoInfo repoInfo = repoInfoService.getById(projectInfo.getRepotId());
        BaseRepository repository = RepositoryFactory.create(repoInfo);
        Collection<Ref> refs = repository.refsList(projectInfo.getProjectUrl());
        System.out.println(refHandle.getBranchNameList(refs));
        System.out.println(refHandle.getTagNameList(refs));
        String path = fileUtil.addPath(FileConst.DIFF_ROOT_PATH, projectInfo.getProjectName(), branch);
        fileUtil.createDir(path);
        long start = System.currentTimeMillis();
        repository.clone(projectInfo.getProjectUrl(), path, branch);
        System.out.println(System.currentTimeMillis()-start);
    }

    @Test
    public void getCommitListTest() throws IOException, GitAPIException {
        String branch = "develop";
        ProjectInfo projectInfo = projectInfoService.getById(2);
        String gitPath = fileUtil.addPath(FileConst.DIFF_ROOT_PATH, projectInfo.getProjectName(), branch, ".git");
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
            String commitId  = commit.getId().toString();
            System.out.println("commitId:"+commitId+", commit user: "+commitUser+
                    " , commit date: "+commitDate+ " ,commit msg: "+commitMsg);
        }
    }

    @Test
    public void diffTest() throws GitAPIException, IOException {
        String baseVersion = "master";
        String newVersion = "develop";
        ProjectDiffParams params = new ProjectDiffParams();
        params.setId(2);
        params.setOldVersion(baseVersion);
        params.setNewVersion(newVersion);
        params.setOldCommitId("75fea70f5276edd0d79e8bf4acdaadea3217f09c");
        params.setNewCommitId("8fb04f2a7edea8c3ed533a8863d538092a3b6a21");
//        params.setDiffTypeCode(DiffTypeEnum.BRANCH_DIFF.getCode());
        params.setDiffTypeCode(DiffTypeEnum.COMMIT_DIFF.getCode());
        params.setUpdateCode(true);

        List<ClassInfo>  classInfos = diffWorkFlow.diff(params);

        System.out.println(classInfos);

    }


}
