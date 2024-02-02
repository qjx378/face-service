package com.izerofx.face.model.vo;

import com.izerofx.face.sdk.model.dto.SeetaRect;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * className: FaceBoundingBoxVO<br>
 * description: 人脸边界框坐标返回结果<br>
 * createDate: 2022年06月23日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class FaceBoundingBoxVO implements Serializable {

    /**
     * 矩形框左上角像素点的纵坐标
     */
    private Integer topLeftX;

    /**
     * 矩形框左上角像素点的横坐标
     */
    private Integer topLeftY;

    /**
     * 矩形框的宽度
     */
    private Integer width;

    /**
     * 矩形框的高度
     */
    private Integer height;

    /**
     * 人脸矩形框的位置的可信程度
     */
    private Float confidence;


    public FaceBoundingBoxVO(Integer topLeftX, Integer topLeftY, Integer width, Integer height, Float confidence) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.width = width;
        this.height = height;
        this.confidence = confidence;
    }
    /**
     * 构造函数
     *
     * @param seetaRect
     */
    public FaceBoundingBoxVO(SeetaRect seetaRect) {
        this.topLeftX = seetaRect.x;
        this.topLeftY = seetaRect.y;
        this.width = seetaRect.width;
        this.height = seetaRect.height;
        this.confidence = seetaRect.score;
    }
}
