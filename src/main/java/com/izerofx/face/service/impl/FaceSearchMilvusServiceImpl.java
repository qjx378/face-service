package com.izerofx.face.service.impl;

import com.google.common.collect.Lists;
import com.google.common.primitives.Floats;
import com.izerofx.face.model.entity.FaceSet;
import com.izerofx.face.model.entity.FaceSetRes;
import com.izerofx.face.model.enums.MilvusFieldEnum;
import com.izerofx.face.service.FaceSearchService;
import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.grpc.SearchResults;
import io.milvus.param.MetricType;
import io.milvus.param.R;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.collection.LoadCollectionParam;
import io.milvus.param.dml.DeleteParam;
import io.milvus.param.dml.InsertParam;
import io.milvus.param.dml.SearchParam;
import io.milvus.param.dml.UpsertParam;
import io.milvus.response.SearchResultsWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * className: FaceSearchMilvusServiceImpl<br>
 * description: Milvus向量数据库搜索服务实现类<br>
 * createDate: 2024年01月16日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Service("faceSearchMilvusService")
@RequiredArgsConstructor
@Slf4j
public class FaceSearchMilvusServiceImpl implements FaceSearchService {

    private final MilvusServiceClient milvusServiceClient;

    @Override
    public void addFaceSet(FaceSet faceSet) {
        // 判断是否存在
        R<Boolean> hasCollection = milvusServiceClient
                .withRetry(5)
                .hasCollection(HasCollectionParam.newBuilder().withCollectionName(faceSet.getFaceSetToken()).build());
        if (hasCollection.getData()) {
            return;
        }

        // 构建创建集合参数
        CreateCollectionParam faceCollection = CreateCollectionParam.newBuilder()
                .withCollectionName(faceSet.getFaceSetToken())
                .withDescription(faceSet.getRemarks())
                .withShardsNum(2)
                .addFieldType(FieldType.newBuilder()
                        .withName(MilvusFieldEnum.FACE_ID.getFieldName())
                        .withDataType(MilvusFieldEnum.FACE_ID.getFieldType())
                        .withPrimaryKey(true)
                        .withAutoID(false)
                        .build())
                .addFieldType(FieldType.newBuilder()
                        .withName(MilvusFieldEnum.FACE_ENCODING.getFieldName())
                        .withDataType(MilvusFieldEnum.FACE_ENCODING.getFieldType())
                        .withDimension(1024)
                        .build())
                .addFieldType(FieldType.newBuilder()
                        .withName(MilvusFieldEnum.FACE_TOKEN.getFieldName())
                        .withDataType(MilvusFieldEnum.FACE_TOKEN.getFieldType())
                        .withMaxLength(128)
                        .build())
                .build();

        // 发送创建集合请求
        milvusServiceClient.withRetry(5).createCollection(faceCollection);
    }

    @Override
    public void addFace(String faceSetToken, FaceSetRes faceSetRes) throws IOException {

        // 构建插入请求参数
        List<InsertParam.Field> fields = new ArrayList<>();
        fields.add(new InsertParam.Field(MilvusFieldEnum.FACE_ID.getFieldName(), List.of(faceSetRes.getId())));
        fields.add(new InsertParam.Field(MilvusFieldEnum.FACE_TOKEN.getFieldName(), List.of(faceSetRes.getFaceToken())));
        fields.add(new InsertParam.Field(MilvusFieldEnum.FACE_ENCODING.getFieldName(), List.of(Floats.asList(faceSetRes.getFaceEncoding()))));

        // 发送插入请求
        milvusServiceClient.withRetry(5).insert(InsertParam.newBuilder()
                .withCollectionName(faceSetToken)
                .withFields(fields)
                .build());
    }

    @Override
    public void updateFace(String faceSetToken, FaceSetRes faceSetRes) throws IOException {
        // 构建更新请求参数
        List<UpsertParam.Field> fields = new ArrayList<>();
        fields.add(new UpsertParam.Field(MilvusFieldEnum.FACE_ID.getFieldName(), List.of(faceSetRes.getId())));
        fields.add(new UpsertParam.Field(MilvusFieldEnum.FACE_TOKEN.getFieldName(), List.of(faceSetRes.getFaceToken())));
        fields.add(new UpsertParam.Field(MilvusFieldEnum.FACE_ENCODING.getFieldName(), List.of(Floats.asList(faceSetRes.getFaceEncoding()))));

        // 发送更新请求
        milvusServiceClient.withRetry(5).upsert(UpsertParam.newBuilder()
                .withCollectionName(faceSetToken)
                .withFields(fields)
                .build());
    }

    @Override
    public void deleteFace(String faceSetToken, FaceSetRes faceSetRes) throws IOException {

        // 发送删除请求
        String deleteExpr = String.format(MilvusFieldEnum.FACE_ID.getFieldName() + " in [%s]", faceSetRes.getId());
        milvusServiceClient.withRetry(5).delete(DeleteParam.newBuilder()
                .withCollectionName(faceSetToken)
                .withExpr(deleteExpr)
                .build());
    }

    @Override
    public List<FaceSetRes> searchFace(String faceSetToken, float[] faceEncoding, int topN) throws IOException {

        try {
            // 先加载
            milvusServiceClient.withRetry(5).loadCollection(LoadCollectionParam.newBuilder().withCollectionName(faceSetToken).build());

            // 采用Milvus相识度查询
            SearchParam searchParam = SearchParam.newBuilder()
                    .withCollectionName(faceSetToken)
                    .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
                    .withMetricType(MetricType.IP)
                    .withOutFields(List.of(MilvusFieldEnum.FACE_ID.getFieldName(), MilvusFieldEnum.FACE_TOKEN.getFieldName()))
                    .withTopK(topN)
                    .withVectors(List.of(Floats.asList(faceEncoding)))
                    .withVectorFieldName(MilvusFieldEnum.FACE_ENCODING.getFieldName())
                    .withParams("{\"nprobe\":10, \"offset\":0}")
                    .build();
            R<SearchResults> search = milvusServiceClient.withRetry(5).search(searchParam);

            // 封装返回结果
            if (search.getData() == null)
                return null;

            SearchResultsWrapper searchResultsWrapper = new SearchResultsWrapper(search.getData().getResults());
            List<SearchResultsWrapper.IDScore> scores = searchResultsWrapper.getIDScore(0);
            if (!scores.isEmpty()) {
                List<FaceSetRes> result = Lists.newArrayList();
                for (SearchResultsWrapper.IDScore idScore : scores) {
                    FaceSetRes faceSetRes = new FaceSetRes();
                    faceSetRes.setId((Long) idScore.get(MilvusFieldEnum.FACE_ID.getFieldName()));
                    faceSetRes.setFaceToken((String) idScore.get(MilvusFieldEnum.FACE_TOKEN.getFieldName()));
                    faceSetRes.setConfidence(idScore.getScore());
                    result.add(faceSetRes);
                }
                return result;
            }
        } catch (Exception e) {
            log.error("人脸资源Milvus查询发生异常", e);
        }
        return Collections.emptyList();
    }
}
