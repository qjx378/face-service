package com.izerofx.face.controller;

import com.google.common.base.Strings;
import com.izerofx.common.model.ResultEnum;
import com.izerofx.common.model.ResultVO;
import com.izerofx.face.model.vo.FaceCompareVO;
import com.izerofx.face.service.FaceCompareService;
import com.izerofx.face.util.SeetaFace6Util;
import lombok.extern.slf4j.Slf4j;
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
 * className: FaceCompareController<br>
 * description: 人脸比对接口控制类<br>
 * createDate: 2022年07月01日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Slf4j
@RestController
@RequestMapping("/v1")
public class FaceCompareController {


    private final FaceCompareService faceCompareService;

    @Autowired
    public FaceCompareController(FaceCompareService faceCompareService) {
        this.faceCompareService = faceCompareService;
    }

    /**
     * 将两个人脸进行比对，来判断是否为同一个人，返回比对结果置信度
     * 如果同时传入了 image_file\image_base64参数，image_file优先
     *
     * @param firstImageFile    第一张二进制图片文件
     * @param firstImageBase64  第一张base64 编码的二进制图片数据
     * @param secondImageFile   第二张二进制图片文件
     * @param secondImageBase64 第二张base64 编码的二进制图片数据
     * @return 人脸比对结果
     */
    @PostMapping(value = "/face-compare")
    public ResultVO faceCompare(@RequestParam(name = "first_image_file", required = false) MultipartFile firstImageFile,
                                @RequestParam(name = "first_image_base64", required = false) String firstImageBase64,
                                @RequestParam(name = "second_image_file", required = false) MultipartFile secondImageFile,
                                @RequestParam(name = "second_image_base64", required = false) String secondImageBase64) {

        if ((firstImageFile == null || firstImageFile.isEmpty()) && Strings.isNullOrEmpty(firstImageBase64)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }

        if ((secondImageFile == null || secondImageFile.isEmpty()) && Strings.isNullOrEmpty(secondImageBase64)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }

        // 获取输入流，判断文件是否合法
        InputStream firstStream;
        try {
            firstStream = firstImageFile != null && !firstImageFile.isEmpty() ? firstImageFile.getInputStream() : new ByteArrayInputStream(Base64.getDecoder().decode(firstImageBase64));
        } catch (IOException e) {
            log.error("图片转换失败", e);
            return ResultVO.failure(10012, "参数<first_image_xx>对应的图像无法正确解析，有可能不是一个图像文件、或有数据破损。");
        }

        if (!SeetaFace6Util.isImage(firstStream)) {
            return ResultVO.failure(10012, "参数<first_image_xx>对应的图像无法正确解析，有可能不是一个图像文件、或有数据破损。");
        }

        // 获取输入流，判断文件是否合法
        InputStream secondStream;
        try {
            secondStream = secondImageFile!=null && !secondImageFile.isEmpty() ? secondImageFile.getInputStream() : new ByteArrayInputStream(Base64.getDecoder().decode(secondImageBase64));
        } catch (IOException e) {
            log.error("图片转换失败", e);
            return ResultVO.failure(10012, "参数<second_image_xx>对应的图像无法正确解析，有可能不是一个图像文件、或有数据破损。");
        }

        if (!SeetaFace6Util.isImage(secondStream)) {
            return ResultVO.failure(10012, "参数<second_image_xx>对应的图像无法正确解析，有可能不是一个图像文件、或有数据破损。");
        }

        // 比对
        FaceCompareVO result = faceCompareService.compareImages(firstStream, secondStream);

        if (result != null) {
            return ResultVO.success(result);
        }

        return ResultVO.failure(ResultEnum.INTERNAL_SERVER_ERROR);
    }
}
