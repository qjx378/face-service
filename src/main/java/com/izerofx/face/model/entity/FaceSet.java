package com.izerofx.face.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * className: FaceSet<br>
 * description: 人脸库实体类<br>
 * createDate: 2022年06月26日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class FaceSet implements Serializable {

    /**
     * 主键id
     */
    @JsonIgnore
    private long id;

    /**
     * 人脸集合的标识
     */
    private String faceSetToken;

    /**
     * 全局唯一的人脸库自定义标识
     */
    private String outerId;

    /**
     * 人脸集合的名字
     */
    private String displayName;

    /**
     * 人脸集合最大库容
     */
    private long faceSetCapacity;

    /**
     * 人脸库当中的人脸数量
     */
    private long faceNumber;

    /**
     * 删除标记（0：正常; 1：删除）
     */
    @JsonIgnore
    private int delFlag;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 特征库数据库字段映射
     */
    public static final BeanPropertyRowMapper<FaceSet> faceSetMapper = new BeanPropertyRowMapper<>() {
        public FaceSet mapRow(ResultSet rs, int rowNum) throws SQLException {
            FaceSet faceSet = new FaceSet();
            faceSet.setId(rs.getInt("id"));
            faceSet.setFaceSetToken(rs.getString("face_set_token"));
            faceSet.setOuterId(rs.getString("outer_id"));
            faceSet.setDisplayName(rs.getString("display_name"));
            faceSet.setFaceSetCapacity(rs.getLong("face_set_capacity"));
            faceSet.setFaceNumber(rs.getLong("face_number"));
            faceSet.setDelFlag(rs.getInt("del_flag"));
            faceSet.setRemarks(rs.getString("remarks"));
            faceSet.setCreatedTime(rs.getTimestamp("created_time"));
            faceSet.setUpdatedTime(rs.getTimestamp("updated_time"));
            return faceSet;
        }
    };
}
