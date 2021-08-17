package com.test.codediff.internal;

import com.test.codediff.entity.RepotInfo;
import com.test.codediff.exceptions.GitException;
import lombok.Data;

@Data
abstract class Repository{

    private RepotInfo repotInfo;

    Repository(RepotInfo repotInfo){
        this.repotInfo = repotInfo;
    }

    public abstract void clone(String url, String project_local_path, String branch) throws GitException;

    public abstract void pull(String local_git_path, String branch);


}
