package com.izerofx.face.controller;

import com.izerofx.common.model.ResultEnum;
import com.izerofx.common.model.ResultVO;
import com.izerofx.face.model.dto.FaceSetDTO;
import com.izerofx.face.model.entity.FaceSet;
import com.izerofx.face.model.vo.FaceSearchVO;
import com.izerofx.face.service.FaceSetsManager;
import com.izerofx.face.util.SeetaFace6Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

/**
 * className: FaceSetController<br>
 * description: <br>
 * createDate: 2022年06月27日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Slf4j
@RestController
@RequestMapping("/v1")
public class FaceSetController {

    private final FaceSetsManager faceSetsManager;

    @Autowired
    public FaceSetController(FaceSetsManager faceSetsManager) {
        this.faceSetsManager = faceSetsManager;
    }


    /**
     * 创建人脸库
     *
     * @param faceSetDto 人脸库对象
     * @return 人脸库信息
     */
    @PostMapping("/face-sets")
    public ResultVO create(@RequestBody FaceSetDTO faceSetDto) {

        if (faceSetDto == null) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }

        return faceSetsManager.create(faceSetDto);
    }

    /**
     * 查询人脸库信息
     *
     * @param faceSetToken 人脸集合的标识
     * @param outerId      自定义人脸集合标识
     * @return 人脸库信息
     */
    @GetMapping(value = "/face-sets")
    public ResultVO getDetail(@RequestParam(name = "face_set_token", required = false) String faceSetToken,
                              @RequestParam(name = "outer_id", required = false) String outerId) {
        if (StringUtils.isAllBlank(faceSetToken, outerId)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }

        if (StringUtils.isNoneBlank(faceSetToken, outerId)) {
            return ResultVO.failure(ResultEnum.PARAMETER_CONFLICT);
        }

        FaceSet faceSet = faceSetsManager.getDetail(faceSetToken, outerId);


        return faceSet == null ? ResultVO.failure(404, "无效的face_set_token或outer_id") : ResultVO.success(faceSet);
    }

    /**
     * 删除人脸库
     *
     * @param faceSetToken 人脸集合的标识
     * @param outerId      自定义人脸集合标识
     * @return 结果
     */
    @PostMapping(value = "/face-sets/delete")
    public ResultVO delete(@RequestParam(name = "face_set_token", required = false) String faceSetToken,
                           @RequestParam(name = "outer_id", required = false) String outerId) {

        if (StringUtils.isAllBlank(faceSetToken, outerId)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }

        if (StringUtils.isNoneBlank(faceSetToken, outerId)) {
            return ResultVO.failure(ResultEnum.PARAMETER_CONFLICT);
        }

        return faceSetsManager.delete(faceSetToken, outerId);
    }

    /**
     * 人脸搜索
     *
     * @param faceSetToken 人脸集合唯一标识
     * @param outerId      人脸集合自定义标识
     * @param imageFile    图片文件
     * @param imageBase64  图片Base64
     * @param faceToken    人脸标识
     * @param topN         返回条数
     * @return 人脸结果
     */
    @PostMapping(value = "/face-sets/search")
    public ResultVO search(@RequestParam(name = "face_set_token", required = false) String faceSetToken,
                           @RequestParam(name = "outer_id", required = false) String outerId,
                           @RequestParam(name = "image_file", required = false) MultipartFile imageFile,
                           @RequestParam(name = "image_base64", required = false) String imageBase64,
                           @RequestParam(name = "face_token", required = false) String faceToken,
                           @RequestParam(name = "top_n", defaultValue = "5") Integer topN) {

        if (StringUtils.isAllBlank(faceSetToken, outerId)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }
        if (imageFile == null && StringUtils.isAllBlank(imageBase64, faceToken)) {
            return ResultVO.failure(ResultEnum.PARAMETER_ABSENT);
        }

        if (StringUtils.isNoneBlank(faceSetToken, outerId)) {
            return ResultVO.failure(ResultEnum.PARAMETER_CONFLICT);
        }

        if (imageFile != null && (StringUtils.isNotBlank(imageBase64) || StringUtils.isNotBlank(faceToken))) {
            return ResultVO.failure(ResultEnum.PARAMETER_CONFLICT);
        }

        // 获取人脸库
        FaceSet faceSet = faceSetsManager.getDetail(faceSetToken, outerId);
        if (faceSet == null) {
            return ResultVO.failure(400, "人脸库不存在");
        }

        List<FaceSearchVO> faceSetResResult;
        if (StringUtils.isNotBlank(faceToken)) {
            faceSetResResult = faceSetsManager.search(faceSet, faceToken, topN);
        } else {
            // 获取输入流，判断文件是否合法
            InputStream stream;
            try {
                if (imageFile != null && !imageFile.isEmpty()) {
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

            faceSetResResult = faceSetsManager.search(faceSet, stream, topN);
        }

        return ResultVO.success(faceSetResResult);
    }

}
