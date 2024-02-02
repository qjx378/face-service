package com.izerofx.face.model.vo;

import com.izerofx.face.sdk.model.dto.SeetaPointF;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * className: FaceMarkPointVO<br>
 * description: 人脸关键点坐标返回结果<br>
 * createDate: 2022年06月23日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class FaceMarkPointVO implements Serializable {


    /**
     * 点横坐标
     */
    public double x;

    /**
     * 点纵坐标
     */
    public double y;

    public FaceMarkPointVO(SeetaPointF seetaPointF) {
        this.x = seetaPointF.x;
        this.y = seetaPointF.y;
    }
}
