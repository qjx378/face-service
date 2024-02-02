package com.izerofx.face.service.impl;

import com.izerofx.face.dao.FaceSetDao;
import com.izerofx.face.model.entity.FaceSet;
import com.izerofx.face.service.FaceSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * className: FaceSetServiceImpl<br>
 * description: 人脸库服务接口实现类<br>
 * createDate: 2022年06月27日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Service
public class FaceSetServiceImpl implements FaceSetService {

    private final FaceSetDao faceSetDao;

    @Autowired
    public FaceSetServiceImpl(FaceSetDao faceSetDao) {
        this.faceSetDao = faceSetDao;
    }

    @Override
    public long save(FaceSet faceSet) {
        return faceSetDao.insert(faceSet);
    }

    @Override
    public int updateFaceNumberById(long faceNumber, long id) {
        return faceSetDao.updateFaceNumberById(faceNumber, id);
    }

    @Override
    public int deleteById(long id) {
        return faceSetDao.deleteById(id);
    }


    @Override
    public FaceSet selectByFaceSetToken(String faceSetToken) {
        return faceSetDao.selectByFaceSetToken(faceSetToken);
    }


    @Override
    public FaceSet selectByOuterId(String outerId) {
        return faceSetDao.selectByOuterId(outerId);
    }

    @Override
    public boolean existByOuterId(String outerId) {
        return faceSetDao.countByOuterId(outerId) > 0;
    }
}
