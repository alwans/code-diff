package com.test.diff.services.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.diff.services.entity.CoverageApp;
import com.test.diff.services.entity.ProjectInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.diff.services.params.ListProjectParams;

import java.util.List;

/**
 *
 */
public interface ProjectInfoService extends IService<ProjectInfo> {


    /**
     *
     * @param projectInfo
     * @return 返回主键id
     */
    Long create(ProjectInfo projectInfo);

    /**
     * 根据项目名查询
     * @param projectName
     * @return
     */
    List<ProjectInfo> getInfoByName(String projectName);

    /**
     * 拉取工程下所有运行中应用的jacoco探针数据
     * @param projectInfo
     */
    void pullExecData(ProjectInfo projectInfo);

    /**
     * 拉取单个应用的jacoco探针数据
     * @param projectInfo
     * @param coverageApp 应用
     */
    void pullExecData(ProjectInfo projectInfo, CoverageApp coverageApp);

    /**
     * 获取收集状态为收集中的项目列表
     * @return
     */
    List<ProjectInfo> getCollectProject();

    /**
     * 通过参数查询，返回列表数据
     * @param params
     * @return
     */
    Page<ProjectInfo> selectListByParams(ListProjectParams params);

}
