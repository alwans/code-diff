package com.test.codediff.internal;

import com.test.codediff.entity.RepotInfo;
import com.test.codediff.exceptions.GitException;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

@Slf4j
@Getter
@Setter
public class GitRepository extends Repository{

    public GitRepository(RepotInfo repotInfo){super(repotInfo);};


    @Override
    public void clone(String url, String project_local_path, String branch) throws GitException {
        try {
            Git.cloneRepository()
                    .setURI(url)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(super.getRepotInfo().getUserName(),
                            super.getRepotInfo().getPasswd()))
                    .setBranch(branch)
                    .setDirectory(new File(project_local_path))
                    .call();
        } catch (GitAPIException e) {
            log.error("clone project:{} is failed", url);
            e.printStackTrace();
            throw new GitException("克隆失败");
        }
        log.info("clone success: {}", url);
    }

    @Override
    public void pull(String local_git_path, String branch) {

    }
}
