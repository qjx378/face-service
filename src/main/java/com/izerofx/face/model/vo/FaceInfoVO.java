package com.izerofx.face.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * className: FaceInfoVO<br>
 * description: 人脸信息结果<br>
 * createDate: 2022年06月23日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class FaceInfoVO implements Serializable {

    /**
     * 人脸ID
     */
    private String faceToken;

    /**
     * 人脸在图像中的位置
     */
    private FaceBoundingBoxVO boundingBox;

    /**
     * 人脸5个关键点坐标
     */
    private FaceLandmarkVO landmark;

    /**
     * 人脸关键属性
     */
    private FaceAttributeVO attributes;

}
