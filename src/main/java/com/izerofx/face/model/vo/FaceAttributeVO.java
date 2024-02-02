package com.izerofx.face.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * className: FaceAttributeVO<br>
 * description: 人脸属性返回结果<br>
 * createDate: 2022年06月23日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class FaceAttributeVO implements Serializable {

    /**
     * 年龄分析结果
     */
    private Integer age;

    /**
     * 性别分析结果 0-女，1-男
     */
    private Integer gender;

    /**
     * 是否带口罩
     */
    private Boolean mask;


    public FaceAttributeVO() {
    }

}
