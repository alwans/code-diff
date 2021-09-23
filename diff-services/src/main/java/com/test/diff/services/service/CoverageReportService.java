package com.test.diff.services.service;

import com.test.diff.services.entity.CoverageReport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.diff.services.base.controller.result.BaseResult;
import com.test.diff.services.params.ProjectDiffParams;
import com.test.diff.services.params.ReportParams;

/**
 *
 * @author wl
 */
public interface CoverageReportService extends IService<CoverageReport> {

    /**
     * 插入一条新记录
     * @param projectId
     * @return 返回id
     */
    int create(int projectId);

    /**
     * 指定项目收集中{@link CoverageReport#getIsUsed()}的报告记录
     * @param projectId
     * @return
     */
    CoverageReport selectUsedByProjectId(long projectId);

    /**
     * 生成全量/增量报告
     * @param params
     * @return
     */
    BaseResult report(ReportParams params);
}
