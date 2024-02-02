package com.izerofx.face.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * className: FaceSetAddFaceVo<br>
 * description: 人脸库添加人脸返回结果<br>
 * createDate: 2022年07月06日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class FaceSetAddFaceVO implements Serializable {

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
     * 被检测出的人脸数组,如果没有检测出人脸则为空数组
     */
    private List<FaceInfoVO> faces;
}
