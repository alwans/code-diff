package com.test.codediff.internal;

import com.test.codediff.entity.RepotInfo;
import com.test.codediff.exceptions.GitException;

public class SvnRepository extends Repository{

    SvnRepository(RepotInfo repotInfo) {
        super(repotInfo);
    }

    @Override
    public void clone(String url, String project_local_path, String branch) throws GitException {

    }

    @Override
    public void pull(String local_git_path, String branch) {

    }
}
