package com.bilibili.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 视频投稿记录表
 *
 * @TableName t_video
 */
@TableName(value = "t_video")
@Data
@Document(indexName = "videos")
public class Video implements Serializable {
    /**
     * 主键id
     * es主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "userId")
    @Field(type = FieldType.Long)
    private Long userId;

    /**
     * 视频链接
     */
    @TableField(value = "url")
    @Field(type = FieldType.Text)
    private String url;

    /**
     * 封面链接
     */
    @TableField(value = "thumbnail")
    private String thumbnail;

    /**
     * 视频标题
     */
    @TableField(value = "title")
    @Field(type = FieldType.Text)
    private String title;

    /**
     * 视频类型 0-原创 1-转载
     */
    @TableField(value = "type")
    private Byte type;

    /**
     * 视频时长
     */
    @TableField(value = "duration")
    private String duration;

    /**
     * 所在分区
     */
    @TableField(value = "area")
    private String area;

    /**
     * 标签列表
     */
    @TableField(exist = false)
    private List<VideoTag> videoTagList;

    /**
     * 视频简介
     */
    @TableField(value = "description")
    @Field(type = FieldType.Text)
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    @Field(type = FieldType.Date)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    @Field(type = FieldType.Date)
    private Date updateTime;

    /**
     * 删除 0-未删除 1-已删除
     */
    @TableField(value = "isDeleted")
    private Byte isDeleted;

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
        Video other = (Video) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
                && (this.getThumbnail() == null ? other.getThumbnail() == null : this.getThumbnail().equals(other.getThumbnail()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getDuration() == null ? other.getDuration() == null : this.getDuration().equals(other.getDuration()))
                && (this.getArea() == null ? other.getArea() == null : this.getArea().equals(other.getArea()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getThumbnail() == null) ? 0 : getThumbnail().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getDuration() == null) ? 0 : getDuration().hashCode());
        result = prime * result + ((getArea() == null) ? 0 : getArea().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
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
        sb.append(", url=").append(url);
        sb.append(", thumbnail=").append(thumbnail);
        sb.append(", title=").append(title);
        sb.append(", type=").append(type);
        sb.append(", duration=").append(duration);
        sb.append(", area=").append(area);
        sb.append(", description=").append(description);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}