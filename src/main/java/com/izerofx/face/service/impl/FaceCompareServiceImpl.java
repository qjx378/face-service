package com.izerofx.face.service.impl;

import com.izerofx.face.model.vo.FaceBoundingBoxVO;
import com.izerofx.face.model.vo.FaceCompareVO;
import com.izerofx.face.model.vo.FaceInfoVO;
import com.izerofx.face.sdk.model.dto.SeetaImageData;
import com.izerofx.face.sdk.model.dto.SeetaRect;
import com.izerofx.face.service.FaceCompareService;
import com.izerofx.face.util.SeetaFace6Util;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * className: FaceCompareServiceImpl<br>
 * description: <br>
 * createDate: 2022年07月02日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Slf4j
@Service
public class FaceCompareServiceImpl implements FaceCompareService {

    @Resource
    private SeetaFace6Manager seetaFace6JNI;

    @Override
    public FaceCompareVO compareImages(InputStream firstStream, InputStream secondStream) {
        if (firstStream == null || secondStream == null) {
            log.warn("第一张图片或第二张图片为空");
            return null;
        }

        // 图片流转ARGB格式图像数据
        SeetaImageData firstImageData = SeetaFace6Util.toSeetaImageData(firstStream);
        SeetaImageData secondImageData = SeetaFace6Util.toSeetaImageData(secondStream);

        // 人脸1检测
        FaceInfoVO firstsFace = getFaceInfo(firstImageData);

        // 人脸2检测
        FaceInfoVO secondFace = getFaceInfo(secondImageData);

        // //获取图片中最大人脸进行特征值计算
        // float[] firstImageFaceFeature = seetaFace6JNI.extractMaxFace(firstImageData);
        // float[] secondImageFaceFeature = seetaFace6JNI.extractMaxFace(secondImageData);
        //
        // //计算特征值相似度
        // float confidence = seetaFace6JNI.calculateSimilarity(firstImageFaceFeature, secondImageFaceFeature);

        //计算特征值相似度
        float confidence = seetaFace6JNI.compare(firstImageData, secondImageData);

        //封装结果
        FaceCompareVO result = new FaceCompareVO();
        result.setSimilarity(confidence);
        result.setFirstFace(firstsFace);
        result.setSecondFace(secondFace);

        return result;
    }

    /**
     * 人脸检测，获取最大人脸
     *
     * @param imageData
     * @return
     */
    private FaceInfoVO getFaceInfo(SeetaImageData imageData) {
        SeetaRect[] seetaRects = seetaFace6JNI.detect(imageData);
        SeetaRect seetaRect = Arrays.stream(seetaRects).max(Comparator.comparingDouble(SeetaRect::getWidth)).get();

        FaceInfoVO faceInfoVO = new FaceInfoVO();
        faceInfoVO.setBoundingBox(new FaceBoundingBoxVO(seetaRect));

        return faceInfoVO;
    }
}
