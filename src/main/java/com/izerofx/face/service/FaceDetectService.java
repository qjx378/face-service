package com.izerofx.face.service;

import com.izerofx.face.model.vo.FaceDetectVO;

import java.io.InputStream;

/**
 * className: FaceDetectService<br>
 * description: 人脸检测服务接口<br>
 * createDate: 2022年07月02日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public interface FaceDetectService {

    /**
     * 人脸检测
     *
     * @param imageStream      图片输入流
     * @param returnLandmark   是否检测并返回人脸关键点
     * @param returnAttributes 是否检测并返回根据人脸特征判断出的年龄、性别等属性
     * @return 人脸识别结果
     */
    FaceDetectVO detect(InputStream imageStream, int returnLandmark, String returnAttributes);
}
