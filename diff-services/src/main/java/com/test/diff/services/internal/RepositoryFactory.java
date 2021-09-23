package com.test.diff.services.internal;


import com.test.diff.services.convert.ModelConvert;
import com.test.diff.services.convert.RepoInfoVOConvert;
import com.test.diff.services.entity.RepoInfo;
import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.exceptions.GitException;
import com.test.diff.services.vo.RepoInfoVO;

/**
 * @author wl
 */
public  class RepositoryFactory {

    public static BaseRepository create(RepoInfo repoInfo) throws GitException {
        ModelConvert<RepoInfo, RepoInfoVO> repoInfoVOModelConvert = new RepoInfoVOConvert();
        RepoInfoVO repoInfoVO = repoInfoVOModelConvert.convert(repoInfo);
        switch (repoInfoVO.getDepotType()){
            case SVN:
                new GitException(StatusCode.GIT_REPO_TYPE_NOT_EXISTS);
            case GIT:
                return new GitRepository(repoInfo);
            default:
                throw new GitException(StatusCode.GIT_REPO_TYPE_NOT_EXISTS);
        }
    }

}
