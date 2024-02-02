package com.izerofx.face.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.izerofx.face.util.ConfidenceSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * className: FaceCompareVo<br>
 * description: 人脸比对返回结果<br>
 * createDate: 2022年06月22日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class FaceCompareVO implements Serializable {

    /**
     * 比对结果置信度，范围 [0,100]，小数点后3位有效数字，数字越大表示两个人脸越可能是同一个人
     */
    @JsonSerialize(using = ConfidenceSerialize.class)
    private Float similarity;

    /**
     * 通过 first_image_file、first_image_base64 传入的图片中检测出的人脸数组，采用数组中的第一个人脸进行人脸比对
     */
    private FaceInfoVO firstFace;

    /**
     * 通过 second_image_file、second_image_base64 传入的图片中检测出的人脸数组，采用数组中的第一个人脸进行人脸比对
     */
    private FaceInfoVO secondFace;

}
