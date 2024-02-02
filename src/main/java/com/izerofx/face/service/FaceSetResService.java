package com.izerofx.face.service;

import com.izerofx.face.model.entity.FaceSetRes;

import java.util.List;

/**
 * className: FaceSetResService<br>
 * description: 人脸库资源服务接口<br>
 * createDate: 2022年07月06日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public interface FaceSetResService {

    /**
     * 插入数据
     *
     * @param faceSetRes 人脸库资源
     * @return 成功条数
     */
    long save(FaceSetRes faceSetRes);

    /**
     * 通过主键ID删除数据
     *
     * @param id 主键ID
     * @return 成功条数
     */
    int deleteById(long id);

    /**
     * 通过人脸库ID删除数据
     *
     * @param faceSetId 人脸库ID
     * @return 成功条数
     */
    int deleteByFaceSetId(long faceSetId);

    /**
     * 通过人脸标识查询
     *
     * @param faceToken 人脸标识
     * @return 人脸库资源
     */
    FaceSetRes selectByFaceToken(String faceToken);

    /**
     * 通过人脸库ID查询人脸库资源集合
     *
     * @param faceSetId 人脸库ID
     * @return 人脸库资源集合
     */
    List<FaceSetRes> selectByFaceSetId(Long faceSetId);

    /**
     * 根据人脸标识查询人脸库资源
     *
     * @param faceSetId     人脸库ID
     * @param faceTokenList 人脸标识集合
     * @return 人脸库资源集合
     */
    List<FaceSetRes> selectByFaceToken(Long faceSetId, List<String> faceTokenList);

}
