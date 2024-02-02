package com.izerofx.face.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * className: FaceSetDto<br>
 * description: 人脸库数据传输对象<br>
 * createDate: 2022年06月27日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class FaceSetDTO implements Serializable {

    /**
     * 全局唯一的人脸库自定义标识
     */
    private String outerId;

    /**
     * 人脸集合的名字
     */
    private String displayName;

    /**
     * 备注
     */
    private String remarks;
}
