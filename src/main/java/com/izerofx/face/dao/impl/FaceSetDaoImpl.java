package com.izerofx.face.dao.impl;

import com.google.common.base.Strings;
import com.izerofx.face.dao.FaceSetDao;
import com.izerofx.face.model.entity.FaceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * className: FaceSetDaoImpl<br>
 * description: 人脸库数据访问接口实现类<br>
 * createDate: 2022年06月26日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Repository
public class FaceSetDaoImpl implements FaceSetDao {

    private final JdbcClient jdbcClient;

    @Autowired
    public FaceSetDaoImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public long insert(FaceSet faceSet) {
        String sql = "insert into face_set(face_set_token, outer_id, display_name, face_set_capacity, del_flag, remarks, created_time) values(?, ?, ?, ?, 0, ?, now())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = jdbcClient.sql(sql)
                .param(faceSet.getFaceSetToken())
                .param(faceSet.getOuterId())
                .param(faceSet.getDisplayName())
                .param(faceSet.getFaceSetCapacity())
                .param(faceSet.getRemarks())
                .update(keyHolder, "id");
        return result > 0 ? Objects.requireNonNull(keyHolder.getKey()).longValue() : 0;
    }

    @Override
    public int update(FaceSet faceSet) {
        String sql = "update face_set set ";
        List<Object> params = new LinkedList<>();

        if (!Strings.isNullOrEmpty(faceSet.getDisplayName())) {
            sql += "display_name = ?,";
            params.add(faceSet.getDisplayName());
        }

        if (faceSet.getFaceSetCapacity() > 0) {
            sql += "face_set_capacity = ?,";
            params.add(faceSet.getDisplayName());
        }

        if (faceSet.getFaceNumber() > -1) {
            sql += "face_number = ?,";
            params.add(faceSet.getFaceNumber());
        }

        if (faceSet.getRemarks() != null) {
            sql += "remarks = ?,";
            params.add(faceSet.getRemarks());
        }

        if (!params.isEmpty()) {
            sql += " updated_time = now() where del_flag = 0 and id = ?";
            params.add(faceSet.getId());
            return jdbcClient.sql(sql).params(params.toArray()).update();
        } else {
            return 0;
        }
    }

    @Override
    public int updateFaceNumberById(long faceNumber, long id) {
        String sql = "update face_set set face_number = (face_number + ?) where del_flag = 0 and id = ?";
        return jdbcClient.sql(sql).params(faceNumber, id).update();
    }

    @Override
    public int deleteById(long id) {
        String sql = "update face_set set del_flag = 1 where id = ?";
        return jdbcClient.sql(sql).param(id).update();
    }

    @Override
    public FaceSet selectById(long id) {
        String sql = "select * from face_set where del_flag = 0 and id = ?";
        List<FaceSet> result = jdbcClient.sql(sql).param(id).query(FaceSet.faceSetMapper).list();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public FaceSet selectByFaceSetToken(String faceSetToken) {
        String sql = "select * from face_set where del_flag = 0 and face_set_token = ?";
        List<FaceSet> result = jdbcClient.sql(sql).param(faceSetToken).query(FaceSet.faceSetMapper).list();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public FaceSet selectByOuterId(String dbName) {
        String sql = "select * from face_set where del_flag = 0 and outer_id = ?";
        List<FaceSet> result = jdbcClient.sql(sql).param(dbName).query(FaceSet.faceSetMapper).list();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public long countByOuterId(String outerId) {
        String sql = "select count(1) from face_set where del_flag = 0 and outer_id = ?";
        return jdbcClient.sql(sql).param(outerId).query(Long.class).single();
    }
}
