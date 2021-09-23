package com.test.diff.services;


import com.test.diff.services.consts.FileConst;
import com.test.diff.services.entity.ProjectInfo;
import com.test.diff.services.entity.RepoInfo;
import com.test.diff.services.exceptions.FileException;
import com.test.diff.services.exceptions.GitException;
import com.test.diff.services.internal.BaseRepository;
import com.test.diff.services.internal.RepositoryFactory;
import com.test.diff.services.service.ProjectInfoService;
import com.test.diff.services.service.RepoInfoService;
import com.test.diff.services.utils.FileUtil;
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
        RepoInfo repoInfo = repoInfoService.getById(projectInfo.getRepoId());
        BaseRepository repository = RepositoryFactory.create(repoInfo);
        String path = new FileUtil().addPath(FileConst.DIFF_ROOT_PATH, projectInfo.getProjectName(), version, ".git");
        repository.pull(path, version);

    }
}
