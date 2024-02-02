package com.izerofx.face.controller;

import com.izerofx.common.model.ResultEnum;
import com.izerofx.common.model.ResultVO;
import com.izerofx.face.model.vo.FaceDetectVO;
import com.izerofx.face.service.FaceDetectService;
import com.izerofx.face.util.SeetaFace6Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * className: FaceRecognizerController<br>
 * description: 人脸检测接口控制类<br>
 * createDate: 2022年06月21日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Slf4j
@RestController
@RequestMapping("/v1")
public class FaceDetectController {

    private final FaceDetectService faceDetectService;

    @Autowired
    public FaceDetectController(FaceDetectService faceDetectService) {
        this.faceDetectService = faceDetectService;
    }

    /**
     * 传入图片进行人脸检测和人脸分析
     * 如果同时传入了 image_file\image_base64参数，image_file优先
     *
     * @param imageFile        二进制图片文件
     * @param imageBase64      base64 编码的二进制图片数据
     * @param returnLandmark   是否检测并返回人脸关键点
     * @param returnAttributes 是否检测并返回根据人脸特征判断出的年龄、性别等属性
     * @return 人脸检测结果
     */
    @PostMapping(value = "/face-detect")
    public ResultVO faceDetect(@RequestParam(name = "image_file", required = false) MultipartFile imageFile,
                               @RequestParam(name = "image_base64", required = false) String imageBase64,
                               @RequestParam(name = "return_landmark", required = false, defaultValue = "0") int returnLandmark,
                               @RequestParam(name = "return_attributes", required = false, defaultValue = "0") String returnAttributes) {

        if ((imageFile == null || imageFile.isEmpty()) && StringUtils.isBlank(imageBase64)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }

        // 获取输入流，判断文件是否合法
        InputStream stream;
        try {
            stream = (imageFile != null && !imageFile.isEmpty()) ? imageFile.getInputStream() : new ByteArrayInputStream(Base64.getDecoder().decode(imageBase64));
        } catch (IOException e) {
            log.error("图片转换失败", e);
            return ResultVO.failure(400, "参数<image_xx>对应的图像无法正确解析，有可能不是一个图像文件、或有数据破损。");
        }

        // 检测是否为图片文件
        if (!SeetaFace6Util.isImage(stream)) {
            return ResultVO.failure(400, "参数<image_xx>对应的图像无法正确解析，有可能不是一个图像文件、或有数据破损。");
        }

        // 人脸检测
        FaceDetectVO result = faceDetectService.detect(stream, returnLandmark, returnAttributes);

        return ResultVO.success(result);
    }
}
