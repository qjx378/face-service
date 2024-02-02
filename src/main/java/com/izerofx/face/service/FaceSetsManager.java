package com.izerofx.face.service;

import com.izerofx.common.model.ResultVO;
import com.izerofx.face.model.vo.FaceSearchVO;
import com.izerofx.face.model.entity.FaceSet;
import com.izerofx.face.model.vo.FaceSetAddFaceVO;
import com.izerofx.face.model.dto.FaceSetDTO;

import java.io.InputStream;
import java.util.List;

/**
 * className: FaceSetsManager<br>
 * description: 人脸库管理接口<br>
 * createDate: 2022年07月14日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public interface FaceSetsManager {

    /**
     * 创建人脸库
     *
     * @param faceSetDto 人脸库数据传输对象
     * @return 结果
     */
    ResultVO create(FaceSetDTO faceSetDto);

    /**
     * 通过唯一标识获取人脸库信息
     *
     * @param faceSetToken 人脸库唯一标识
     * @param outerId      自定义标识
     * @return 人脸库信息
     */
    FaceSet getDetail(String faceSetToken, String outerId);

    /**
     * 删除人脸库
     *
     * @param faceSetToken 人脸库唯一标识
     * @param outerId      自定义标识
     * @return 结果
     */
    ResultVO delete(String faceSetToken, String outerId);

    /**
     * 添加人脸
     *
     * @param faceSet     人脸库信息
     * @param imageStream 人脸图片流
     * @param single      是否单人脸
     * @return 人脸库添加人脸返回结果
     */
    FaceSetAddFaceVO addFace(FaceSet faceSet, InputStream imageStream, boolean single);

    /**
     * 批量导入人脸
     *
     * @param faceSet   人脸库信息
     * @param imagePath 人脸图片路径
     * @return 结果
     */
    ResultVO batchImportFace(FaceSet faceSet, String imagePath);

    /**
     * 移除人脸
     *
     * @param faceSet    人脸库信息
     * @param faceTokens 人脸标识
     * @return 结果
     */
    ResultVO removeFace(FaceSet faceSet, String faceTokens);

    /**
     * 人脸搜索
     *
     * @param faceSet     人脸库信息
     * @param imageStream 人脸图片流
     * @param topN        最多返回人脸数量
     * @return 人脸搜索结果
     */
    List<FaceSearchVO> search(FaceSet faceSet, InputStream imageStream, Integer topN);

    /**
     * 人脸搜索
     *
     * @param faceSet   人脸库信息
     * @param faceToken 人脸标识
     * @param topN      最多返回人脸数量
     * @return 人脸搜索结果
     */
    List<FaceSearchVO> search(FaceSet faceSet, String faceToken, Integer topN);
}
