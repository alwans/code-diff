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
 * @TableName diff_project_info
 */
@TableName(value ="diff_project_info")
@Data
public class ProjectInfo implements Serializable {
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 仓库id
     */
    @TableField(value = "repo_id")
    private Integer repoId;

    /**
     * 环境名
     */
    @TableField(value = "env")
    private String env;

    /**
     * 所属项目组名
     */
    @TableField(value = "project_group")
    private String projectGroup;

    /**
     * 项目名
     */
    @TableField(value = "project_name")
    private String projectName;

    /**
     * 项目仓库地址
     */
    @TableField(value = "project_url")
    private String projectUrl;

    /**
     * 收集状态：0：未收集，1：收集中，2：暂停收集
     */
    @TableField(value = "collect_status")
    private Integer collectStatus;

    /**
     * 报告状态：0：未生成，1：生成中，2：生成成功，3：生成失败
     */
    @TableField(value = "report_status")
    private Integer reportStatus;

    /**
     * 是否启用
     */
    @TableField(value = "is_disable")
    private Boolean isDisable;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Boolean isDelete;

    /**
     * 添加时间
     */
    @TableField(value = "add_time")
    private Date addTime;

    /**
     * 最后修改时间
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
        ProjectInfo other = (ProjectInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRepoId() == null ? other.getRepoId() == null : this.getRepoId().equals(other.getRepoId()))
            && (this.getProjectGroup() == null ? other.getProjectGroup() == null : this.getProjectGroup().equals(other.getProjectGroup()))
            && (this.getProjectName() == null ? other.getProjectName() == null : this.getProjectName().equals(other.getProjectName()))
            && (this.getProjectUrl() == null ? other.getProjectUrl() == null : this.getProjectUrl().equals(other.getProjectUrl()))
            && (this.getIsDisable() == null ? other.getIsDisable() == null : this.getIsDisable().equals(other.getIsDisable()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()))
            && (this.getLastTime() == null ? other.getLastTime() == null : this.getLastTime().equals(other.getLastTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRepoId() == null) ? 0 : getRepoId().hashCode());
        result = prime * result + ((getProjectGroup() == null) ? 0 : getProjectGroup().hashCode());
        result = prime * result + ((getProjectName() == null) ? 0 : getProjectName().hashCode());
        result = prime * result + ((getProjectUrl() == null) ? 0 : getProjectUrl().hashCode());
        result = prime * result + ((getIsDisable() == null) ? 0 : getIsDisable().hashCode());
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
        sb.append(", repoId=").append(repoId);
        sb.append(", projectGroup=").append(projectGroup);
        sb.append(", projectName=").append(projectName);
        sb.append(", projectUrl=").append(projectUrl);
        sb.append(", isDisable=").append(isDisable);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", addTime=").append(addTime);
        sb.append(", lastTime=").append(lastTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}