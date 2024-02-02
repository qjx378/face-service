package com.izerofx.face;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Floats;
import com.izerofx.face.model.entity.FaceSet;
import com.izerofx.face.model.entity.FaceSetRes;
import com.izerofx.face.service.impl.SeetaFace6Manager;
import com.izerofx.face.util.SeetaFace6Util;
import io.milvus.client.MilvusClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.grpc.DataType;
import io.milvus.grpc.SearchResults;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.index.CreateIndexParam;
import io.milvus.response.SearchResultsWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * className: MilvusTests<br>
 * description: <br>
 * createDate: 2024年01月15日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@SpringBootTest
@Slf4j
public class MilvusTests {

    @Resource
    private MilvusClient milvusClient;

    @Resource
    private JdbcClient jdbcClient;

    @Resource
    private SeetaFace6Manager seetaFace6Mager;


    void testAddCollection() {
        String sql = "select * from face_set where del_flag=0";
        List<FaceSet> result = jdbcClient.sql(sql).query(FaceSet.faceSetMapper).list();


        for (FaceSet faceSet : result) {
            R<Boolean> hasCollection = milvusClient.hasCollection(HasCollectionParam.newBuilder().withCollectionName(faceSet.getFaceSetToken()).build());
            if (hasCollection.getData()) {
                continue;
            }

            CreateCollectionParam faceCollection = CreateCollectionParam.newBuilder()
                    .withCollectionName(faceSet.getFaceSetToken())
                    .withDescription(faceSet.getRemarks())
                    .withShardsNum(2)
                    .addFieldType(FieldType.newBuilder()
                            .withName("face_id")
                            .withDataType(DataType.Int64)
                            .withPrimaryKey(true)
                            .withAutoID(false)
                            .build())
                    .addFieldType(FieldType.newBuilder()
                            .withName("face_encoding")
                            .withDataType(DataType.FloatVector)
                            .withDimension(1024)
                            .build())
                    .addFieldType(FieldType.newBuilder()
                            .withName("face_token")
                            .withDataType(DataType.VarChar)
                            .withMaxLength(128)
                            .build())
                    .build();

            milvusClient.createCollection(faceCollection);
        }
    }

    @Test
    void testAddData() {
        String sql = "select * from face_set_resource where del_flag=0 and face_set_id = 10";
        List<FaceSetRes> result = jdbcClient.sql(sql).query(FaceSetRes.faceSetResMapper).list();

        List<Long> faceIdList = new ArrayList<>();
        List<String> faceNameList = new ArrayList<>();
        List<List<Float>> faceEncodingList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            FaceSetRes faceSetRes = result.get(i);

            faceIdList.add(faceSetRes.getId());
            faceNameList.add(faceSetRes.getFaceToken());

            float[] feature = SeetaFace6Util.bytesToFloats(BaseEncoding.base64().decode(faceSetRes.getFaceFeature()));
            faceEncodingList.add(Floats.asList(feature));

            if (faceIdList.size() >= 1000) {
                List<InsertParam.Field> fields = new ArrayList<>();
                fields.add(new InsertParam.Field("face_id", faceIdList));
                fields.add(new InsertParam.Field("face_token", faceNameList));
                fields.add(new InsertParam.Field("face_encoding", faceEncodingList));

                milvusClient.insert(InsertParam.newBuilder()
                        .withCollectionName("iZP05LmQomGT6Rr0mmaFyQNcvPXJRakN")
                        .withFields(fields)
                        .build());

                faceIdList.clear();
                faceNameList.clear();
                faceEncodingList.clear();
            }
        }


        List<InsertParam.Field> fields = new ArrayList<>();
        fields.add(new InsertParam.Field("face_id", faceIdList));
        fields.add(new InsertParam.Field("face_token", faceNameList));
        fields.add(new InsertParam.Field("face_encoding", faceEncodingList));

        milvusClient.insert(InsertParam.newBuilder()
                .withCollectionName("iZP05LmQomGT6Rr0mmaFyQNcvPXJRakN")
                .withFields(fields)
                .build());


        milvusClient.createIndex(CreateIndexParam.newBuilder()
                .withCollectionName("iZP05LmQomGT6Rr0mmaFyQNcvPXJRakN")
                .withIndexName("face_encoding_ip_index")
                .withFieldName("face_encoding")
                .withIndexType(IndexType.IVF_FLAT)
                .withMetricType(MetricType.IP)
                .withExtraParam("{\"nlist\":1024}")
                .build());
    }

    //@Test
    void testQuery() {
        //milvusClient.loadCollection(LoadCollectionParam.newBuilder().withCollectionName("tN9AUdhLfMDRDAKqV1tipjvmkOrwWuOk").build());

        try (InputStream inputStream = Files.newInputStream(Paths.get("E:\\face_test\\秦家学.jpg"))) {

            float[] imageFeature = seetaFace6Mager.extractMaxFace(inputStream);

            List<String> search_output_fields = List.of("face_id", "face_name");
            List<List<Float>> search_vectors = List.of(Floats.asList(imageFeature));
            SearchParam searchParam = SearchParam.newBuilder()
                    .withCollectionName("tN9AUdhLfMDRDAKqV1tipjvmkOrwWuOk")
                    .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
                    .withMetricType(MetricType.IP)
                    .withOutFields(search_output_fields)
                    .withTopK(5)
                    .withVectors(search_vectors)
                    .withVectorFieldName("face_encoding")
                    .withParams("{\"nprobe\":10, \"offset\":0}")
                    .build();
            R<SearchResults> respSearch = milvusClient.search(searchParam);
            SearchResultsWrapper wrapperSearch = new SearchResultsWrapper(respSearch.getData().getResults());
            System.out.println(wrapperSearch.getIDScore(0));
            System.out.println(wrapperSearch.getFieldData("face_id", 0));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
