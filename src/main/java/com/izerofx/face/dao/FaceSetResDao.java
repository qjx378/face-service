package com.izerofx.face.dao;

import com.izerofx.face.model.entity.FaceSetRes;

import java.util.List;

/**
 * className: FaceSetResDao<br>
 * description: <br>
 * createDate: 2022年06月28日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public interface FaceSetResDao {

    /**
     * 插入数据
     *
     * @param faceSetRes 人脸库资源信息
     * @return 影响行数
     */
    long insert(FaceSetRes faceSetRes);

    /**
     * 更新数据
     *
     * @param faceSetRes 人脸库资源信息
     * @return 影响行数
     */
    int update(FaceSetRes faceSetRes);

    /**
     * 通过主键ID删除数据
     *
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(long id);

    /**
     * 通过人脸库ID删除数据
     *
     * @param faceSetId 人脸库ID
     * @return 影响行数
     */
    int deleteByFaceSetId(long faceSetId);

    /**
     * 通过主键ID查询
     *
     * @param id 主键ID
     * @return 人脸库资源信息
     */
    FaceSetRes selectById(long id);

    /**
     * 通过人脸标识查询
     *
     * @param faceToken 人脸标识
     * @return 人脸库资源信息
     */
    FaceSetRes selectByFaceToken(String faceToken);

    /**
     * 通过人脸库ID查询人脸库资源集合
     *
     * @param faceSetId 人脸库ID
     * @return 人脸库资源信息集合
     */
    List<FaceSetRes> selectByFaceSetId(Long faceSetId);

    /**
     * 根据人脸标识查询人脸库资源
     *
     * @param faceSetId     人脸库ID
     * @param faceTokenList 人脸唯一标识集合
     * @return 人脸库资源信息集合
     */
    List<FaceSetRes> selectByFaceToken(Long faceSetId, List<String> faceTokenList);
}
