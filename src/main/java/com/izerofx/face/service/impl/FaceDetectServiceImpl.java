package com.izerofx.face.service.impl;

import com.google.common.collect.Lists;
import com.izerofx.face.model.vo.*;
import com.izerofx.face.sdk.model.dto.SeetaImageData;
import com.izerofx.face.sdk.model.dto.SeetaPointF;
import com.izerofx.face.sdk.model.dto.SeetaRect;
import com.izerofx.face.service.FaceDetectService;
import com.izerofx.face.util.SeetaFace6Util;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * className: FaceDetectServiceImpl<br>
 * description: 人脸检测服务实现<br>
 * createDate: 2022年07月02日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Slf4j
@Service
public class FaceDetectServiceImpl implements FaceDetectService {

    @Resource
    private SeetaFace6Manager seetaFace6JNI;

    @Override
    public FaceDetectVO detect(InputStream imageStream, int returnLandmark, String returnAttributes) {
        SeetaImageData imageData = SeetaFace6Util.toSeetaImageData(imageStream);

        // 人脸检测
        SeetaRect[] seetaRects = seetaFace6JNI.detect(imageData);

        if (seetaRects == null || seetaRects.length == 0) {
            log.warn("未检测到人脸信息");
            return FaceDetectVO.builder().faceNum(0).build();
        }

        // 结果转换
        List<FaceInfoVO> faces = Lists.newArrayList();
        for (SeetaRect seetaRect : seetaRects) {

            // 人脸信息
            FaceInfoVO faceInfo = new FaceInfoVO();
            // 人脸坐标及置信度
            faceInfo.setBoundingBox(new FaceBoundingBoxVO(seetaRect));

            // 返回人脸关键点
            if (returnLandmark == 1) {
                SeetaPointF[] seetaPointFs = detectLandmark(imageData, seetaRect);
                faceInfo.setLandmark(new FaceLandmarkVO(seetaPointFs));
            }

            // 返回人脸属性
            if (StringUtils.isNotBlank(returnAttributes)) {
                FaceAttributeVO faceAttributeVO = detectAttributes(returnAttributes, imageData, seetaRect);
                faceInfo.setAttributes(faceAttributeVO);
            }

            faces.add(faceInfo);
        }

        // 结果组装
        return FaceDetectVO.builder().faceNum(faces.size()).faces(faces).build();
    }

    /**
     * 检测人脸关键点
     *
     * @param imageData
     * @param seetaRect
     * @return
     */
    private SeetaPointF[] detectLandmark(SeetaImageData imageData, SeetaRect seetaRect) {
        return seetaFace6JNI.mark(imageData, seetaRect);
    }

    /**
     * 检测人脸属性
     *
     * @param attributes
     * @param imageData
     * @param seetaRect
     * @return
     */
    private FaceAttributeVO detectAttributes(String attributes, SeetaImageData imageData, SeetaRect seetaRect) {
        FaceAttributeVO faceAttribute = new FaceAttributeVO();

        // 检测关键点
        SeetaPointF[] seetaPointFs = detectLandmark(imageData, seetaRect);

        // 年龄
        if (attributes.contains("age")) {
            int age = seetaFace6JNI.predictAgeWithCrop(imageData, seetaPointFs);
            faceAttribute.setAge(age);
        }

        // 性别
        if (attributes.contains("gender")) {
            int gender = seetaFace6JNI.predictGenderWithCrop(imageData, seetaPointFs);
            faceAttribute.setGender(gender);
        }

        // 是否戴口罩
        if (attributes.contains("mask")) {
            boolean mask = seetaFace6JNI.maskDetect(imageData, seetaRect);
            faceAttribute.setMask(mask);
        }
        return faceAttribute;
    }
}
