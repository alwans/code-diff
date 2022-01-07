package com.test.diff.services.service;

import com.test.diff.services.entity.CoverageReport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.diff.services.base.controller.result.BaseResult;
import com.test.diff.services.params.ProjectDiffParams;
import com.test.diff.services.params.ReportParams;

import java.util.List;

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
     * 正序| 倒序 返回指定项目的所有未删除报告记录列表
     * @param projectId 项目id
     * @param isDesc 是否倒序
     * @return
     */
    List<CoverageReport> selectListByProjectId(long projectId, boolean isDesc);

    /**
     * 生成全量/增量报告
     * @param params
     * @return
     */
    BaseResult report(ReportParams params);

    /**
     * 获取指定项目的报告地址
     * 如果该项目有收集中的记录，且该记录有生成过报告，那么就返回这条收集中的报告记录；如果收集中且未生成过报告，即返回为空
     * 如果该项目没有收集中的记录，那么返回最近一条报告，状态成功：返回报告地址，状态失败：返回空
     * @param projectId
     * @return
     */
    BaseResult getReportURI(long projectId);
}
