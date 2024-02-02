package com.izerofx.face.service.impl;

import com.izerofx.face.dao.FaceSetResDao;
import com.izerofx.face.model.entity.FaceSetRes;
import com.izerofx.face.service.FaceSetResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * className: FaceSetResServiceImpl<br>
 * description: 人脸库资源服务接口实现<br>
 * createDate: 2022年07月06日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Service
public class FaceSetResServiceImpl implements FaceSetResService {

    private final FaceSetResDao faceSetResDao;

    @Autowired
    public FaceSetResServiceImpl(FaceSetResDao faceSetResDao) {
        this.faceSetResDao = faceSetResDao;
    }

    @Override
    public long save(FaceSetRes faceSetRes) {
        return faceSetResDao.insert(faceSetRes);
    }

    @Override
    public int deleteById(long id) {
        return faceSetResDao.deleteById(id);
    }

    @Override
    public int deleteByFaceSetId(long faceSetId) {
        return faceSetResDao.deleteByFaceSetId(faceSetId);
    }

    @Override
    public FaceSetRes selectByFaceToken(String faceToken) {
        return faceSetResDao.selectByFaceToken(faceToken);
    }

    @Override
    public List<FaceSetRes> selectByFaceSetId(Long faceSetId) {
        return faceSetResDao.selectByFaceSetId(faceSetId);
    }

    @Override
    public List<FaceSetRes> selectByFaceToken(Long faceSetId, List<String> faceTokenList) {
        return faceSetResDao.selectByFaceToken(faceSetId, faceTokenList);
    }

}
