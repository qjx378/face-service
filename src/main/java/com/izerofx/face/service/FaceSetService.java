package com.izerofx.face.service;

import com.izerofx.face.model.entity.FaceSet;

/**
 * className: FaceFeatureDbService<br>
 * description: 人脸库服务接口<br>
 * createDate: 2022年06月27日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public interface FaceSetService {


    /**
     * 插入数据
     *
     * @param faceSet 人脸库
     * @return 成功条数
     */
    long save(FaceSet faceSet);

    /**
     * 通过主键更新人脸库数量
     *
     * @param faceNumber 人脸数量
     * @param id         主键ID
     * @return 成功条数
     */
    int updateFaceNumberById(long faceNumber, long id);

    /**
     * 通过主键ID删除数据
     *
     * @param id 主键ID
     * @return 成功条数
     */
    int deleteById(long id);


    /**
     * 通过人脸库唯一标识查询
     *
     * @param faceSetToken 人脸库唯一标识
     * @return 人脸库
     */
    FaceSet selectByFaceSetToken(String faceSetToken);

    /**
     * 通过人脸库自定义标识查询
     *
     * @param outerId 人脸库自定义标识
     * @return 人脸库
     */
    FaceSet selectByOuterId(String outerId);

    /**
     * 根据自定义标识查询是否已存在
     *
     * @param outerId 自定义标识
     * @return 是否成功
     */
    boolean existByOuterId(String outerId);
}
