package com.izerofx.face.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * className: FaceDTO<br>
 * description: 人脸数据传输对象<br>
 * createDate: 2023年10月10日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
public class FaceDTO implements Serializable {

    /**
     * 人脸名称
     */
    @JsonAlias(value = "face_name")
    @JsonProperty(value = "face_name")
    private String faceName;

    /**
     * 人脸特征值
     */
    @JsonAlias(value = "face_encoding")
    @JsonProperty(value = "face_encoding")
    private float[] faceEncoding;

    public FaceDTO() {
    }

    public FaceDTO(String faceName, float[] faceEncoding) {
        this.faceName = faceName;
        this.faceEncoding = faceEncoding;
    }
}
