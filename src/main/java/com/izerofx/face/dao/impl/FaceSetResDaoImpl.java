package com.izerofx.face.dao.impl;

import com.izerofx.face.dao.FaceSetResDao;
import com.izerofx.face.model.entity.FaceSetRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * className: FaceSetResDaoImpl<br>
 * description: <br>
 * createDate: 2022年06月28日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Repository
public class FaceSetResDaoImpl implements FaceSetResDao {

    private final JdbcClient jdbcClient;

    @Autowired
    public FaceSetResDaoImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public long insert(FaceSetRes faceSetRes) {
        String sql = "insert into face_set_resource(face_set_id, face_token, face_feature, original_image_path, original_image_hash, face_image_path, del_flag, created_time) values(?, ?, ?, ?, ?, ?, 0, now())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = jdbcClient
                .sql(sql)
                .param(faceSetRes.getFaceSetId())
                .param(faceSetRes.getFaceToken())
                .param(faceSetRes.getFaceFeature())
                .param(faceSetRes.getOriginalImagePath())
                .param(faceSetRes.getOriginalImageHash())
                .param(faceSetRes.getFaceImagePath())
                .update(keyHolder, "id");
        return result > 0 ? Objects.requireNonNull(keyHolder.getKey()).longValue() : 0;
    }

    @Override
    public int update(FaceSetRes faceSetRes) {
        return 0;
    }

    @Override
    public int deleteById(long id) {
        String sql = "delete from face_set_resource where id = ?";
        return jdbcClient.sql(sql).param(id).update();
    }

    @Override
    public int deleteByFaceSetId(long faceSetId) {
        String sql = "update face_set_resource set del_flag = 1 where face_set_id = ?";
        return jdbcClient.sql(sql).param(faceSetId).update();
    }

    @Override
    public FaceSetRes selectById(long id) {
        String sql = "select * from face_set_resource where del_flag = 0 and id = ?";
        List<FaceSetRes> result = jdbcClient.sql(sql).param(id).query(FaceSetRes.faceSetResMapper).list();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public FaceSetRes selectByFaceToken(String faceToken) {
        String sql = "select * from face_set_resource where del_flag = 0 and face_token = ?";
        List<FaceSetRes> result = jdbcClient.sql(sql).param(faceToken).query(FaceSetRes.faceSetResMapper).list();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public List<FaceSetRes> selectByFaceSetId(Long faceSetId) {
        String sql = "select * from face_set_resource where del_flag = 0 and face_set_id = ?";
        List<FaceSetRes> result = jdbcClient.sql(sql).param(faceSetId).query(FaceSetRes.faceSetResMapper).list();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    @Override
    public List<FaceSetRes> selectByFaceToken(Long faceSetId, List<String> faceTokenList) {
        String sql = "select * from face_set_resource where del_flag = 0 and face_set_id =:faceSetId and face_token in (:faceTokenList)";

        Map<String, Object> params = new HashMap<>(2);
        params.put("faceSetId", faceSetId);
        params.put("faceTokenList", faceTokenList);

        List<FaceSetRes> result = jdbcClient.sql(sql).params(params).query(FaceSetRes.faceSetResMapper).list();

        return result.isEmpty() ? Collections.emptyList() : result;
    }
}
