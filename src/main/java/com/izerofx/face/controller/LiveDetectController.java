package com.izerofx.face.controller;

import com.izerofx.common.model.ResultEnum;
import com.izerofx.common.model.ResultVO;
import com.izerofx.face.model.vo.LiveDetectVO;
import com.izerofx.face.service.LiveDetectService;
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
import java.util.Objects;

/**
 * className: LiveDetectController<br>
 * description: 活体检测接口控制类<br>
 * createDate: 2022年07月04日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Slf4j
@RestController
@RequestMapping("/v1")
public class LiveDetectController {


    private final LiveDetectService liveDetectService;

    @Autowired
    public LiveDetectController(LiveDetectService liveDetectService) {
        this.liveDetectService = liveDetectService;
    }


    /**
     * 静默活体检测
     *
     * @param imageFile   二进制图片文件
     * @param imageBase64 base64 编码的二进制图片数据
     * @return 静默活体检测结果
     */
    @PostMapping(value = "/live-detect-face")
    public ResultVO liveDetectFace(@RequestParam(name = "image_file", required = false) MultipartFile imageFile,
                                   @RequestParam(name = "image_base64", required = false) String imageBase64) {

        if ((imageFile == null || imageFile.isEmpty()) && StringUtils.isBlank(imageBase64)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }

        // 获取输入流，判断文件是否合法
        InputStream imageStream;
        try {
            imageStream = !Objects.requireNonNull(imageFile).isEmpty() ? imageFile.getInputStream() : new ByteArrayInputStream(Base64.getDecoder().decode(imageBase64));
        } catch (IOException e) {
            log.error("图片转换失败", e);
            return ResultVO.failure(400, "参数<image_xx>对应的图像无法正确解析，有可能不是一个图像文件、或有数据破损。");
        }

        // 检测是否为图片文件
        if (!SeetaFace6Util.isImage(imageStream)) {
            return ResultVO.failure(400, "参数<image_xx>对应的图像无法正确解析，有可能不是一个图像文件、或有数据破损。");
        }

        // 静默活体检测
        LiveDetectVO result = liveDetectService.liveDetectFace(imageStream);

        return ResultVO.success(result);
    }

}
