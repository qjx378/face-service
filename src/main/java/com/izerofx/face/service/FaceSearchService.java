package com.izerofx.face.service;

import com.izerofx.face.model.entity.FaceSet;
import com.izerofx.face.model.entity.FaceSetRes;

import java.io.IOException;
import java.util.List;

/**
 * className: FaceResESService<br>
 * description: 人脸资源ES服务接口<br>
 * createDate: 2023年10月10日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public interface FaceSearchService {

    /**
     * 添加人脸库
     *
     * @param faceSet 人脸集合实体
     */
    void addFaceSet(FaceSet faceSet);

    /**
     * 添加人脸
     *
     * @param faceSetRes 人脸资源
     */
    void addFace(String faceSetToken, FaceSetRes faceSetRes) throws IOException;

    /**
     * 更新人脸
     *
     * @param faceSetRes 人脸资源
     */
    void updateFace(String faceSetToken, FaceSetRes faceSetRes) throws IOException;

    /**
     * 删除人脸
     *
     * @param faceSetRes 人脸资源
     */
    void deleteFace(String faceSetToken, FaceSetRes faceSetRes) throws IOException;

    /**
     * 搜索人脸
     *
     * @param faceSetToken 人脸库唯一标识
     * @param faceEncoding 人脸特征向量
     * @param topN         返回数量
     * @return 结果
     */
    List<FaceSetRes> searchFace(String faceSetToken, float[] faceEncoding, int topN) throws IOException;
}
