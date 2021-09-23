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
 * @TableName diff_repo_info
 */
@TableName(value ="diff_repo_info")
@Data
public class RepoInfo implements Serializable {
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 仓库名
     */
    @TableField(value = "depot_name")
    private String depotName;

    /**
     * 仓库类型：0:SVN, 1:GIT
     */
    @TableField(value = "depot_type")
    private Integer depotType;

    /**
     * 仓库地址：例127.0.0.1:888
     */
    @TableField(value = "depot_url")
    private String depotUrl;

    /**
     * 登录账号
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 登录密码
     */
    @TableField(value = "passwd")
    private String passwd;

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
     * 创建时间
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
        RepoInfo other = (RepoInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDepotName() == null ? other.getDepotName() == null : this.getDepotName().equals(other.getDepotName()))
            && (this.getDepotType() == null ? other.getDepotType() == null : this.getDepotType().equals(other.getDepotType()))
            && (this.getDepotUrl() == null ? other.getDepotUrl() == null : this.getDepotUrl().equals(other.getDepotUrl()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getPasswd() == null ? other.getPasswd() == null : this.getPasswd().equals(other.getPasswd()))
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
        result = prime * result + ((getDepotName() == null) ? 0 : getDepotName().hashCode());
        result = prime * result + ((getDepotType() == null) ? 0 : getDepotType().hashCode());
        result = prime * result + ((getDepotUrl() == null) ? 0 : getDepotUrl().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getPasswd() == null) ? 0 : getPasswd().hashCode());
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
        sb.append(", depotName=").append(depotName);
        sb.append(", depotType=").append(depotType);
        sb.append(", depotUrl=").append(depotUrl);
        sb.append(", userName=").append(userName);
        sb.append(", passwd=").append(passwd);
        sb.append(", isDisable=").append(isDisable);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", addTime=").append(addTime);
        sb.append(", lastTime=").append(lastTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}