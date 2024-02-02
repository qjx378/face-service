package com.izerofx.face.service;

import com.izerofx.face.model.vo.FaceCompareVO;

import java.io.InputStream;

/**
 * className: FaceCompareService<br>
 * description: 人脸比对服务接口<br>
 * createDate: 2022年07月02日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public interface FaceCompareService {

    /**
     * 两图比对
     *
     * @param firstStream  第一张图片输入流
     * @param secondStream 第二张图片输入流
     * @return 人脸比对结果
     */
    FaceCompareVO compareImages(InputStream firstStream, InputStream secondStream);
}
