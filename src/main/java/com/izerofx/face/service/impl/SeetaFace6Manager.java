package com.izerofx.face.service.impl;

import com.izerofx.face.properties.SeetaFaceProperties;
import com.izerofx.face.sdk.SeetaFace6;
import com.izerofx.face.sdk.model.dto.*;
import com.izerofx.face.util.NativeLibLoader;
import com.izerofx.face.util.SeetaFace6Util;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

/**
 * className: SeetaFace6Manager<br>
 * description: SeetaFace6接口管理类<br>
 * createDate: 2024年01月08日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Component
@Slf4j
public class SeetaFace6Manager {

    private final static int CROP_SIZE = 256 * 256 * 3;

    @Resource
    private SeetaFaceProperties seetaFaceProperties;

    /**
     * 人脸识别JNI接口
     */
    @Resource
    private SeetaFace6 seetaFace6;

    /**
     * 是否加载模型
     */
    private static volatile boolean isLoadModel = false;

    /**
     * 初始化方法
     */
    @PostConstruct
    private void initModel() {
        if (new File(seetaFaceProperties.getModelPath()).exists()) {
            if (seetaFace6 != null && NativeLibLoader.isLoaded) {
                seetaFace6.initModel(seetaFaceProperties.getModelPath(), seetaFaceProperties.getDevice(), seetaFaceProperties.getDeviceId());
                isLoadModel = true;
                log.info("SeetaFace6 Init Model Completed.");
            }
        } else {
            log.error("SeetaFace6 Model Path Not Exists.");
        }
    }


    /**
     * 人脸识别
     *
     * @param imageStream 图片流
     * @return 人脸矩形数
     */
    public SeetaRect[] detect(InputStream imageStream) {
        if (imageStream == null) {
            return null;
        }
        // 人脸检测
        SeetaImageData imageData = SeetaFace6Util.toSeetaImageData(imageStream);
        return detect(imageData);
    }

    /**
     * 人脸识别
     *
     * @param imageData 图片数据
     * @return 人脸矩形数
     */
    public SeetaRect[] detect(SeetaImageData imageData) {
        if (imageData == null) {
            return null;
        }
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return null;
        }
        return seetaFace6.detect(imageData);
    }

    /**
     * 人脸特征检测
     *
     * @param imageData 图片数据
     * @param faceRect  人脸矩形
     * @return 人脸点位数组
     */
    public SeetaPointF[] mark(SeetaImageData imageData, SeetaRect faceRect) {
        if (imageData == null || faceRect == null) {
            return null;
        }
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return null;
        }
        return seetaFace6.mark(imageData, faceRect);
    }

    /**
     * 预测年龄
     *
     * @param imageData    图片数据
     * @param seetaPointFs 人脸关键点坐标
     * @return 年龄
     */
    public int predictAgeWithCrop(SeetaImageData imageData, SeetaPointF[] seetaPointFs) {
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return -1;
        }
        return seetaFace6.predictAgeWithCrop(imageData, seetaPointFs);
    }

    /**
     * 预测性别
     *
     * @param imageData    图片数据
     * @param seetaPointFs 人脸关键点坐标
     * @return 性别
     */
    public int predictGenderWithCrop(SeetaImageData imageData, SeetaPointF[] seetaPointFs) {
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return -1;
        }
        return seetaFace6.predictGenderWithCrop(imageData, seetaPointFs);
    }

    /**
     * 裁剪人脸
     *
     * @param imageStream 图片流
     * @return 人脸二进制数组
     */
    public byte[][] cropFace(InputStream imageStream) {
        if (imageStream == null) {
            return null;
        }
        SeetaImageData imageData = SeetaFace6Util.toSeetaImageData(imageStream);
        return cropFace(imageData);
    }

    /**
     * 裁剪人脸
     *
     * @param imageData 图片数据
     * @return 人脸二进制数组
     */
    public byte[][] cropFace(SeetaImageData imageData) {
        if (imageData == null) {
            return null;
        }
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return null;
        }
        return seetaFace6.crop(imageData);
    }

    /**
     * 裁剪人脸
     *
     * @param imageData 图片数据
     * @return 人脸结果数组
     */
    public FaceCorpResult[] cropFaces(SeetaImageData imageData) {
        if (imageData == null) {
            return null;
        }
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return null;
        }
        return seetaFace6.cropFaces(imageData);
    }

    /**
     * 提取人脸区域特性
     *
     * @param face 裁剪后的二进制图片
     * @return 人脸特征
     */
    public float[] extractCroppedFace(byte[] face) {
        if (face.length != CROP_SIZE) {
            log.warn("参数无效，必须为裁剪后的人脸照片，大小应该为{}，实际为：{}", CROP_SIZE, face.length);
            return null;
        }
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return null;
        }
        return seetaFace6.extractCroppedFace(face);
    }

    /**
     * 提取一个图像中最大人脸的特征
     *
     * @param imageStream 图片流
     * @return 人脸特征
     */
    public float[] extractMaxFace(InputStream imageStream) {
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return null;
        }
        SeetaImageData imageData = SeetaFace6Util.toSeetaImageData(imageStream);
        return seetaFace6.extractMaxFace(imageData);
    }

    /**
     * @param imageData 图片数据
     * @return 人脸特征
     */
    public float[] extractMaxFace(SeetaImageData imageData) {
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return null;
        }
        return seetaFace6.extractMaxFace(imageData);
    }

    /**
     * 计算人脸特征相似度
     *
     * @param firstImageStream  第一张人脸图片流
     * @param secondImageStream 第二张人脸图片流
     * @return 相似度
     */
    public float compare(InputStream firstImageStream, InputStream secondImageStream) {
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return 0;
        }
        SeetaImageData firstImage = SeetaFace6Util.toSeetaImageData(firstImageStream);
        SeetaImageData secondImage = SeetaFace6Util.toSeetaImageData(secondImageStream);
        return seetaFace6.compare(firstImage, secondImage);
    }

    /**
     * 计算人脸特征相似度
     *
     * @param firstImage  第一张人脸图片数据
     * @param secondImage 第二张人脸图片数据
     * @return 相似度
     */
    public float compare(SeetaImageData firstImage, SeetaImageData secondImage) {
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return 0;
        }

        return seetaFace6.compare(firstImage, secondImage);
    }

    /**
     * 计算人脸特征相似度
     *
     * @param features1 人脸特征1
     * @param features2 人脸特征2
     * @return 相似度
     */
    public float calculateSimilarity(float[] features1, float[] features2) {
        if (features1 == null || features2 == null) {
            return -1;
        }
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return -1;
        }
        return seetaFace6.calculateSimilarity(features1, features2);
    }

    /**
     * 静默活体检测
     *
     * @param imageStream 图片流
     * @return 结果
     */
    public PredictImageResult liveDetectFace(InputStream imageStream) {
        SeetaImageData imageData = SeetaFace6Util.toSeetaImageData(imageStream);
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return null;
        }
        return seetaFace6.predictImage(imageData);
    }

    /**
     * 口罩检测
     *
     * @param imageData 图片数据
     * @param face      人脸
     * @return 结果
     */
    public Boolean maskDetect(SeetaImageData imageData, SeetaRect face) {
        if (imageData == null || face == null) {
            return null;
        }
        if (!isLoadModel) {
            log.error("SeetaFace6 Model Not Loaded.");
            return null;
        }
        return seetaFace6.maskDetect(imageData, face);
    }
}
