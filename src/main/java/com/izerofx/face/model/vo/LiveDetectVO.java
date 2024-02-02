package com.izerofx.face.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.izerofx.face.util.ConfidenceSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * className: LiveDetectVo<br>
 * description: 活体检测返回结果<br>
 * createDate: 2022年07月04日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class LiveDetectVO implements Serializable {

    /**
     * 是否活体
     */
    private Boolean alive;

    /**
     * 状态
     */
    private String status;

    /**
     * 置信度,当clarity>0.3,reality>0.8时，alive=true，否则alive=false
     */
    @JsonSerialize(using = ConfidenceSerialize.class)
    private Float confidence;

    /**
     * 检测出最大人脸的图片base64字符串
     */
    private String faceImageBase64;
}
