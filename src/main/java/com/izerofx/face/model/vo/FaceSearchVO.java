package com.izerofx.face.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.izerofx.face.util.ConfidenceSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * className: FaceSearchVO<br>
 * description: 人脸搜索返回结果<br>
 * createDate: 2022年07月14日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class FaceSearchVO implements Serializable {

    /**
     * 人脸ID
     */
    private String faceToken;

    /**
     * 人脸在图像中的位置
     */
    private FaceBoundingBoxVO boundingBox;

    /**
     * 比对结果置信度，范围 [0,100]，小数点后3位有效数字，数字越大表示两个人脸越可能是同一个人
     */
    @JsonSerialize(using = ConfidenceSerialize.class)
    private Float similarity;

    /**
     * 人脸图片base64字符串
     */
    private String imageBase64;

    /**
     * 图片地址
     */
    private String imageUrl;

}
