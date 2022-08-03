package com.bilibili.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户关注表
 *
 * @author sgh
 * @date 2022-8-2
 * @TableName t_user_following
 */
@TableName(value ="t_user_following")
@Data
public class UserFollowing implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 关注的用户Id
     */
    @TableField(value = "followingId")
    private Long followingId;

    /**
     * 关注分组id
     */
    @TableField(value = "groupId")
    private Long groupId;

    /**
     * 被关注的人/粉丝的基本信息
     */
    @TableField(exist = false)
    private UserInfo followingUserInfo;

    /**
     * 是否互相关注
     */
    @TableField(exist = false)
    private Boolean followed;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 取消关注 0-未取消 1-已取消关注
     */
    @TableField(value = "isUnfollowed")
    private Byte isUnfollowed;

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
        UserFollowing other = (UserFollowing) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getFollowingId() == null ? other.getFollowingId() == null : this.getFollowingId().equals(other.getFollowingId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getIsUnfollowed() == null ? other.getIsUnfollowed() == null : this.getIsUnfollowed().equals(other.getIsUnfollowed()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getFollowingId() == null) ? 0 : getFollowingId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getIsUnfollowed() == null) ? 0 : getIsUnfollowed().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", followingId=").append(followingId);
        sb.append(", groupId=").append(groupId);
        sb.append(", createTime=").append(createTime);
        sb.append(", isUnfollowed=").append(isUnfollowed);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}