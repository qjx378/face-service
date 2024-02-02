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
 * className: FaceSetRes<br>
 * description: 人脸库资源实体类<br>
 * createDate: 2022年06月28日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class FaceSetRes implements Serializable {

    /**
     * 主键ID
     */
    private long id;

    /**
     * 特征库ID
     */
    private long faceSetId;

    /**
     * 人脸标识
     */
    private String faceToken;

    /**
     * 人脸特征向量
     */
    private float[] faceEncoding;

    /**
     * 人脸特征值Base64编码
     */
    private String faceFeature;

    /**
     * 原图路径
     */
    private String originalImagePath;

    /**
     * 原图hash值（图片文件MD5）
     */
    private String originalImageHash;

    /**
     * 人脸裁剪图路径
     */
    private String faceImagePath;

    /**
     * 删除标记（0：正常; 1：删除）
     */
    @JsonIgnore
    private int delFlag;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 人脸矩形框的位置的可信程度
     */
    private float confidence;


    /**
     * 特征库数据库字段映射
     */
    public static final BeanPropertyRowMapper<FaceSetRes> faceSetResMapper = new BeanPropertyRowMapper<>() {
        public FaceSetRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            FaceSetRes faceSetRes = new FaceSetRes();
            faceSetRes.setId(rs.getInt("id"));
            faceSetRes.setFaceSetId(rs.getLong("face_set_id"));
            faceSetRes.setFaceToken(rs.getString("face_token"));
            faceSetRes.setFaceFeature(rs.getString("face_feature"));
            faceSetRes.setOriginalImagePath(rs.getString("original_image_path"));
            faceSetRes.setOriginalImageHash(rs.getString("original_image_hash"));
            faceSetRes.setFaceImagePath(rs.getString("face_image_path"));
            faceSetRes.setDelFlag(rs.getInt("del_flag"));
            faceSetRes.setCreatedTime(rs.getTimestamp("created_time"));
            faceSetRes.setUpdatedTime(rs.getTimestamp("updated_time"));
            return faceSetRes;
        }
    };
}
