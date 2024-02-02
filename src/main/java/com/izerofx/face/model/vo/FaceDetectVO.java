package com.izerofx.face.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * className: DetectVo<br>
 * description: 人脸检测返回结果<br>
 * createDate: 2022年06月22日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
@Builder
public class FaceDetectVO implements Serializable {

    /**
     * 检测出的人脸数量
     */
    private Integer faceNum;

    /**
     * 被检测出的人脸数组,如果没有检测出人脸则为空数组
     */
    private List<FaceInfoVO> faces;
}
