package com.izerofx.face.model.vo;

import com.izerofx.face.sdk.model.dto.SeetaPointF;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * className: FaceLandmarkVO<br>
 * description: 人脸关键点返回结果,检测到的5点坐标循序依次为，左眼中心、右眼中心、鼻尖、左嘴角和右嘴角<br>
 * createDate: 2022年06月23日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@ToString
public class FaceLandmarkVO implements Serializable {

    /**
     * 左眼中心
     */
    private FaceMarkPointVO leftEyeCenter;

    /**
     * 右眼中心
     */
    private FaceMarkPointVO rightEyeCenter;

    /**
     * 鼻尖
     */
    private FaceMarkPointVO noseTip;

    /**
     * 左嘴角
     */
    private FaceMarkPointVO mouthLeftCorner;

    /**
     * 右嘴角
     */
    private FaceMarkPointVO mouthRightCorner;

    /**
     * 人脸关键点集合
     */
    private List<FaceMarkPointVO> facePoints;

    /**
     * 构造函数
     *
     * @param point 关键点坐标集合
     */
    public FaceLandmarkVO(SeetaPointF[] point) {
        if (point == null) {
            return;
        }
        if (point.length == 5) {// 5个关键点
            this.leftEyeCenter = new FaceMarkPointVO(point[0]);
            this.rightEyeCenter = new FaceMarkPointVO(point[1]);
            this.noseTip = new FaceMarkPointVO(point[2]);
            this.mouthLeftCorner = new FaceMarkPointVO(point[3]);
            this.mouthRightCorner = new FaceMarkPointVO(point[4]);
        } else if (point.length == 68) {// 68个关键点
            this.facePoints = Arrays.stream(point).map(FaceMarkPointVO::new).collect(Collectors.toList());
        }

    }
}
