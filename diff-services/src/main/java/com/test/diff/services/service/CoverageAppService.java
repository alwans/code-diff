package com.test.diff.services.service;

import com.test.diff.services.entity.CoverageApp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface CoverageAppService extends IService<CoverageApp> {

    List<CoverageApp> getListByProjectId(long projectId);

    int create(int projectId, CoverageApp app);

    /**
     * 根据projectId和jacocoPort，查出正在收集中的应用
     * @param projectId
     * @param jacocoPort
     * @return
     */
    CoverageApp getAppByProjectIdAndPort(int projectId, int jacocoPort);
}
