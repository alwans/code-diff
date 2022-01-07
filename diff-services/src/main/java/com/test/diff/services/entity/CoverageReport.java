package com.test.diff.services.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName coverage_report
 */
@TableName(value ="coverage_report")
@Data
public class CoverageReport implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目id
     */
    @TableField(value = "project_id")
    private Integer projectId;

    /**
     * 自动生成的uuid
     */
    @TableField(value = "uuid")
    private String uuid;

    /**
     * 报告类型：0：全量，1：增量
     */
    @TableField(value = "report_type")
    private Integer reportType;

    /**
     * diff类型{@link com.test.diff.services.enums.DiffTypeEnum}
     */
    @TableField(value = "diff_type")
    private Integer diffType;

    /**
     * 是否正被启用：0：未被使用，1：被使用
     */
    @TableField(value = "is_used")
    private Boolean isUsed;

    /**
     * 基线分支
     */
    @TableField(value = "old_branch")
    private String oldBranch;

    /**
     * 当前分支
     */
    @TableField(value = "new_branch")
    private String newBranch;

    /**
     * 报告绝对路径
     */
    @TableField(value = "report_uri")
    private String reportUri;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Boolean isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "add_time")
    private Date addTime;

    /**
     * 更新时间
     */
    @TableField(value = "last_time")
    private Date lastTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CoverageReport other = (CoverageReport) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
            && (this.getReportType() == null ? other.getReportType() == null : this.getReportType().equals(other.getReportType()))
            && (this.getDiffType() == null ? other.getDiffType() == null : this.getDiffType().equals(other.getDiffType()))
            && (this.getIsUsed() == null ? other.getIsUsed() == null : this.getIsUsed().equals(other.getIsUsed()))
            && (this.getOldBranch() == null ? other.getOldBranch() == null : this.getOldBranch().equals(other.getOldBranch()))
            && (this.getNewBranch() == null ? other.getNewBranch() == null : this.getNewBranch().equals(other.getNewBranch()))
            && (this.getReportUri() == null ? other.getReportUri() == null : this.getReportUri().equals(other.getReportUri()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()))
            && (this.getLastTime() == null ? other.getLastTime() == null : this.getLastTime().equals(other.getLastTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getReportType() == null) ? 0 : getReportType().hashCode());
        result = prime * result + ((getDiffType() == null) ? 0 : getDiffType().hashCode());
        result = prime * result + ((getIsUsed() == null) ? 0 : getIsUsed().hashCode());
        result = prime * result + ((getOldBranch() == null) ? 0 : getOldBranch().hashCode());
        result = prime * result + ((getNewBranch() == null) ? 0 : getNewBranch().hashCode());
        result = prime * result + ((getReportUri() == null) ? 0 : getReportUri().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
        result = prime * result + ((getLastTime() == null) ? 0 : getLastTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", projectId=").append(projectId);
        sb.append(", uuid=").append(uuid);
        sb.append(", reportType=").append(reportType);
        sb.append(", diffType=").append(diffType);
        sb.append(", isUsed=").append(isUsed);
        sb.append(", oldBranch=").append(oldBranch);
        sb.append(", newBranch=").append(newBranch);
        sb.append(", reportUri=").append(reportUri);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", addTime=").append(addTime);
        sb.append(", lastTime=").append(lastTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}