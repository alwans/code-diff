package com.test.codediff;

import com.test.codediff.consts.FileConst;
import com.test.codediff.entity.ProjectInfo;
import com.test.codediff.entity.RepoInfo;
import com.test.codediff.exceptions.FileException;
import com.test.codediff.exceptions.GitException;
import com.test.codediff.internal.BaseRepository;
import com.test.codediff.internal.RepositoryFactory;
import com.test.codediff.service.ProjectInfoService;
import com.test.codediff.service.RepoInfoService;
import com.test.codediff.utils.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RepositoryTest {

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private RepoInfoService repoInfoService;

    @Test
    public void pullTest() throws GitException, FileException {
        String version = "develop";
        ProjectInfo projectInfo =  projectInfoService.getById(2);
        RepoInfo repoInfo = repoInfoService.getById(projectInfo.getRepotId());
        BaseRepository repository = RepositoryFactory.create(repoInfo);
        String path = new FileUtil().addPath(FileConst.DIFF_ROOT_PATH, projectInfo.getProjectName(), version, ".git");
        repository.pull(path, version);

    }
}
