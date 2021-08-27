package com.test.codediff;

import com.test.codediff.consts.FileConst;
import com.test.codediff.entity.ProjectInfo;
import com.test.codediff.entity.RepoInfo;
import com.test.codediff.enums.DiffTypeEnum;
import com.test.codediff.exceptions.FileException;
import com.test.codediff.exceptions.GitException;
import com.test.codediff.internal.BaseRepository;
import com.test.codediff.internal.DiffWorkFlow;
import com.test.codediff.internal.RefHandle;
import com.test.codediff.internal.RepositoryFactory;
import com.test.codediff.params.ProjectDiffParams;
import com.test.codediff.service.ProjectInfoService;
import com.test.codediff.service.RepoInfoService;
import com.test.codediff.utils.FileUtil;
import com.test.codediff.vo.ClassInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
