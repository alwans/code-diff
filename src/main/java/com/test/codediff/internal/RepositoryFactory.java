package com.test.codediff.internal;

import com.test.codediff.convert.ModelConvert;
import com.test.codediff.convert.ReptInfoVOConvert;
import com.test.codediff.entity.RepoInfo;
import com.test.codediff.enums.StatusCode;
import com.test.codediff.exceptions.GitException;
import com.test.codediff.vo.RepotInfoVO;


/**
 * @author wl
 */
public  class RepositoryFactory {

    public static BaseRepository create(RepoInfo repoInfo) throws GitException {
        ModelConvert<RepoInfo, RepotInfoVO> repoInfoVOModelConvert = new ReptInfoVOConvert();
        RepotInfoVO repotInfoVO = repoInfoVOModelConvert.convert(repoInfo);
        switch (repotInfoVO.getDepotType()){
            case SVN:
                new GitException(StatusCode.GIT_REPO_TYPE_NOT_EXISTS);
            case GIT:
                return new GitRepository(repoInfo);
            default:
                throw new GitException(StatusCode.GIT_REPO_TYPE_NOT_EXISTS);
        }


    }
}
