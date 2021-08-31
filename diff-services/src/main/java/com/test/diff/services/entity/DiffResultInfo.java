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
 * @TableName diff_result_info
 */
@TableName(value ="diff_result_info")
@Data
public class DiffResultInfo implements Serializable {
    /**
     * 自增id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 项目id
     */
    @TableField(value = "project_id")
    private Integer projectId;

    /**
     * diff类型：0:分支diff，1:commit diff
     */
    @TableField(value = "diff_type")
    private Integer diffType;

    /**
     * commit对应的分支名
     */
    @TableField(value = "commit_branch")
    private String commitBranch;

    /**
     * 对比分支名
     */
    @TableField(value = "old_branch")
    private String oldBranch;

    /**
     * 目标分支名
     */
    @TableField(value = "new_branch")
    private String newBranch;

    /**
     * 对比commitId
     */
    @TableField(value = "old_commit_id")
    private String oldCommitId;

    /**
     * 目标commitId
     */
    @TableField(value = "new_commit_id")
    private String newCommitId;

    /**
     * diff结果
     */
    @TableField(value = "diff_result")
    private String diffResult;

    /**
     * 创建时间
     */
    @TableField(value = "add_time")
    private Date addTime;

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
        DiffResultInfo other = (DiffResultInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getDiffType() == null ? other.getDiffType() == null : this.getDiffType().equals(other.getDiffType()))
            && (this.getCommitBranch() == null ? other.getCommitBranch() == null : this.getCommitBranch().equals(other.getCommitBranch()))
            && (this.getOldBranch() == null ? other.getOldBranch() == null : this.getOldBranch().equals(other.getOldBranch()))
            && (this.getNewBranch() == null ? other.getNewBranch() == null : this.getNewBranch().equals(other.getNewBranch()))
            && (this.getOldCommitId() == null ? other.getOldCommitId() == null : this.getOldCommitId().equals(other.getOldCommitId()))
            && (this.getNewCommitId() == null ? other.getNewCommitId() == null : this.getNewCommitId().equals(other.getNewCommitId()))
            && (this.getDiffResult() == null ? other.getDiffResult() == null : this.getDiffResult().equals(other.getDiffResult()))
            && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getDiffType() == null) ? 0 : getDiffType().hashCode());
        result = prime * result + ((getCommitBranch() == null) ? 0 : getCommitBranch().hashCode());
        result = prime * result + ((getOldBranch() == null) ? 0 : getOldBranch().hashCode());
        result = prime * result + ((getNewBranch() == null) ? 0 : getNewBranch().hashCode());
        result = prime * result + ((getOldCommitId() == null) ? 0 : getOldCommitId().hashCode());
        result = prime * result + ((getNewCommitId() == null) ? 0 : getNewCommitId().hashCode());
        result = prime * result + ((getDiffResult() == null) ? 0 : getDiffResult().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
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
        sb.append(", diffType=").append(diffType);
        sb.append(", commitBranch=").append(commitBranch);
        sb.append(", oldBranch=").append(oldBranch);
        sb.append(", newBranch=").append(newBranch);
        sb.append(", oldCommitId=").append(oldCommitId);
        sb.append(", newCommitId=").append(newCommitId);
        sb.append(", diffResult=").append(diffResult);
        sb.append(", addTime=").append(addTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}