package com.izerofx.face.controller;

import com.izerofx.common.model.ResultEnum;
import com.izerofx.common.model.ResultVO;
import com.izerofx.face.model.entity.FaceSet;
import com.izerofx.face.model.vo.FaceSetAddFaceVO;
import com.izerofx.face.service.FaceSetsManager;
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
 * className: FaceSetResController<br>
 * description: 人脸库资源接口控制器<br>
 * createDate: 2022年07月06日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Slf4j
@RestController
@RequestMapping("/v1")
public class FaceSetResController {

    private final FaceSetsManager faceSetsManager;


    @Autowired
    public FaceSetResController(FaceSetsManager faceSetsManager) {
        this.faceSetsManager = faceSetsManager;
    }

    /**
     * 添加人脸
     *
     * @param imageFile    人脸二进制图片
     * @param imageBase64  人脸Base64图片
     * @param faceSetToken 人脸库唯一标识
     * @param outerId      人脸库自定义唯一标识
     * @param single       是否只取一张
     * @return 结果
     */
    @PostMapping("/face-sets/addface")
    public ResultVO addface(@RequestParam(name = "image_file", required = false) MultipartFile imageFile,
                            @RequestParam(name = "image_base64", required = false) String imageBase64,
                            @RequestParam(name = "face_set_token", required = false) String faceSetToken,
                            @RequestParam(name = "outer_id", required = false) String outerId,
                            @RequestParam(name = "single", required = false, defaultValue = "0") Boolean single) {

        if (StringUtils.isAllBlank(faceSetToken, outerId)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }
        if ((imageFile == null || imageFile.isEmpty()) && StringUtils.isBlank(imageBase64)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }
        if (StringUtils.isNoneBlank(faceSetToken, outerId)) {
            return ResultVO.failure(ResultEnum.PARAMETER_CONFLICT);
        }
        if (imageFile != null && StringUtils.isNotBlank(imageBase64)) {
            return ResultVO.failure(ResultEnum.PARAMETER_CONFLICT);
        }

        // 获取输入流，判断文件是否合法
        InputStream stream;
        try {
            if (imageFile != null) {
                stream = imageFile.getInputStream();
            } else {
                stream = new ByteArrayInputStream(Base64.getDecoder().decode(imageBase64));
            }
        } catch (IOException e) {
            log.error("图片转换失败", e);
            return ResultVO.failure(400, "参数<image_xx>对应的图像无法正确解析，有可能不是一个图像文件、或有数据破损。");
        }

        if (!SeetaFace6Util.isImage(stream)) {
            return ResultVO.failure(400, "参数<image_xx>对应的图像无法正确解析，有可能不是一个图像文件、或有数据破损。");
        }

        // 查询人脸库信息
        FaceSet faceSet = faceSetsManager.getDetail(faceSetToken, outerId);
        if (faceSet == null) {
            return ResultVO.failure(400, "人脸库不存在");
        }

        // 添加人脸到人脸库
        FaceSetAddFaceVO result = faceSetsManager.addFace(faceSet, stream, single);

        return ResultVO.success(result);
    }

    /**
     * 删除人脸
     *
     * @param faceSetToken 人脸库唯一标识
     * @param outerId      人脸库自定义唯一标识
     * @param faceTokens   需要移除的人脸标识字符串，可以是一个或者多个face_token组成，用逗号分隔
     * @return 结果
     */
    @PostMapping(value = "/face-sets/removeface")
    public ResultVO removeFace(@RequestParam(name = "face_set_token", required = false) String faceSetToken,
                               @RequestParam(name = "outer_id", required = false) String outerId,
                               @RequestParam(name = "face_tokens") String faceTokens) {

        if (StringUtils.isAllBlank(faceSetToken, outerId) || StringUtils.isBlank(faceTokens)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }

        if (StringUtils.isNoneBlank(faceSetToken, outerId)) {
            return ResultVO.failure(ResultEnum.PARAMETER_CONFLICT);
        }

        // 查询人脸库信息
        FaceSet faceSet = faceSetsManager.getDetail(faceSetToken, outerId);
        if (faceSet == null) {
            return ResultVO.failure(400, "人脸库不存在");
        }

        return faceSetsManager.removeFace(faceSet, faceTokens);
    }

    /**
     * 批量导入人脸
     *
     * @param faceSetToken 人脸库唯一标识
     * @param outerId      人脸库自定义唯一标识
     * @param imagePath    服务器图片目录
     * @return 结果
     */
    @PostMapping(value = "/face-sets/importface")
    public ResultVO batchImportFace(@RequestParam(name = "face_set_token", required = false) String faceSetToken,
                                    @RequestParam(name = "outer_id", required = false) String outerId,
                                    @RequestParam(name = "image_path") String imagePath) {

        if (StringUtils.isAllBlank(faceSetToken, outerId) || StringUtils.isBlank(imagePath)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }

        if (StringUtils.isNoneBlank(faceSetToken, outerId)) {
            return ResultVO.failure(ResultEnum.PARAMETER_CONFLICT);
        }

        // 查询人脸库信息
        FaceSet faceSet = faceSetsManager.getDetail(faceSetToken, outerId);
        if (faceSet == null) {
            return ResultVO.failure(400, "人脸库不存在");
        }

        return faceSetsManager.batchImportFace(faceSet, imagePath);
    }
}
