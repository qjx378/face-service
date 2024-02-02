package com.izerofx.face.dao;

import com.izerofx.face.model.entity.FaceSet;

/**
 * className: FaceSetDao<br>
 * description: 人脸库数据访问接口<br>
 * createDate: 2022年06月26日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public interface FaceSetDao {

    /**
     * 插入数据
     *
     * @param faceSet 人脸库信息
     * @return 影响行数
     */
    long insert(FaceSet faceSet);

    /**
     * 更新数据
     *
     * @param faceSet 人脸库信息
     * @return 影响行数
     */
    int update(FaceSet faceSet);

    /**
     * 通过主键更新人脸库数量
     *
     * @param faceNumber 人脸数量
     * @param id         主键ID
     * @return 影响行数
     */
    int updateFaceNumberById(long faceNumber, long id);

    /**
     * 通过主键ID删除数据
     *
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(long id);

    /**
     * 通过主键ID查询
     *
     * @param id 主键ID
     * @return 人脸库信息
     */
    FaceSet selectById(long id);

    /**
     * 通过主键ID查询
     *
     * @param faceSetToken 人脸库唯一标识
     * @return 人脸库信息
     */
    FaceSet selectByFaceSetToken(String faceSetToken);

    /**
     * 通过特征库名称查询
     *
     * @param outerId 人脸库自定义标识
     * @return 人脸库信息
     */
    FaceSet selectByOuterId(String outerId);


    /**
     * 根据自定义标识查询数量
     *
     * @param outerId 人脸库自定义标识
     * @return 条数
     */
    long countByOuterId(String outerId);


}
