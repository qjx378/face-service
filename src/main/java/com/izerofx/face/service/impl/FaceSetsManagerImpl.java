package com.izerofx.face.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.izerofx.common.model.ResultEnum;
import com.izerofx.common.model.ResultVO;
import com.izerofx.common.util.NanoIdUtils;
import com.izerofx.face.model.dto.FaceSetDTO;
import com.izerofx.face.model.entity.FaceSet;
import com.izerofx.face.model.entity.FaceSetRes;
import com.izerofx.face.model.vo.FaceBoundingBoxVO;
import com.izerofx.face.model.vo.FaceInfoVO;
import com.izerofx.face.model.vo.FaceSearchVO;
import com.izerofx.face.model.vo.FaceSetAddFaceVO;
import com.izerofx.face.sdk.model.dto.FaceCorpResult;
import com.izerofx.face.sdk.model.dto.SeetaImageData;
import com.izerofx.face.service.FaceSearchService;
import com.izerofx.face.service.FaceSetResService;
import com.izerofx.face.service.FaceSetService;
import com.izerofx.face.service.FaceSetsManager;
import com.izerofx.face.util.SeetaFace6Util;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * className: FaceSetsManagerImpl<br>
 * description: 人脸库管理实现类<br>
 * createDate: 2022年07月14日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Slf4j
@Service
public class FaceSetsManagerImpl implements FaceSetsManager {

    /**
     * 人脸图片目录
     */
    @Value("${seetaface.face-image-path:}")
    private String faceImgPath;

    /**
     * 人脸相关操作处理类
     */
    private final SeetaFace6Manager seetaFace6Manager;

    /**
     * 人脸库服务接口
     */
    private final FaceSetService faceSetService;

    /**
     * 人脸库资源服务接口
     */
    private final FaceSetResService faceSetResService;

    /**
     * 人脸资源搜索服务接口
     */
    private final FaceSearchService faceSearchService;

    public FaceSetsManagerImpl(SeetaFace6Manager seetaFace6Manager,
                               FaceSetService faceSetService,
                               FaceSetResService faceSetResService,
                               @Qualifier("faceSearchMilvusService") FaceSearchService faceSearchService) {
        this.seetaFace6Manager = seetaFace6Manager;
        this.faceSetService = faceSetService;
        this.faceSetResService = faceSetResService;
        this.faceSearchService = faceSearchService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVO create(FaceSetDTO faceSetDto) {

        if (faceSetService.existByOuterId(faceSetDto.getOuterId())) {
            return ResultVO.failure(400, "想要创建的人脸库已经存在");
        }

        FaceSet faceSet = new FaceSet();
        faceSet.setFaceSetToken(NanoIdUtils.randomNanoId(32, new char[]{'_', '-'}));
        faceSet.setOuterId(faceSetDto.getOuterId());
        faceSet.setDisplayName(faceSetDto.getDisplayName());
        faceSet.setFaceSetCapacity(10000);
        faceSet.setFaceNumber(0);
        faceSet.setRemarks(faceSetDto.getRemarks());
        faceSet.setCreatedTime(new Date());
        faceSet.setUpdatedTime(new Date());

        // 保存并获取ID
        long id = faceSetService.save(faceSet);
        faceSet.setId(id);

        // 创建人脸向量数据库
        if (id > 0) {
            faceSearchService.addFaceSet(faceSet);
        }

        return id > 0 ? ResultVO.success(faceSet) : ResultVO.failure(-1, "创建失败，请稍后重试");
    }

    @Override
    public FaceSet getDetail(String faceSetToken, String outerId) {
        FaceSet faceSet = null;
        if (StringUtils.isNotBlank(faceSetToken)) {
            faceSet = faceSetService.selectByFaceSetToken(faceSetToken);
        } else if (StringUtils.isNotBlank(outerId)) {
            faceSet = faceSetService.selectByOuterId(outerId);
        }
        return faceSet;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO delete(String faceSetToken, String outerId) {
        FaceSet faceSet = getDetail(faceSetToken, outerId);
        if (faceSet == null) {
            return ResultVO.failure(404, "人脸库不存在");
        }

        try {
            // 删除磁盘文件
            String rootPath = faceImgPath + File.separator + faceSet.getFaceSetToken();
            Files.deleteIfExists(Paths.get(rootPath));

            // 删除数据库数据
            faceSetResService.deleteByFaceSetId(faceSet.getId());

            // 删除人脸库
            faceSetService.deleteById(faceSet.getId());
        } catch (IOException e) {
            log.error("删除人脸库异常", e);
            return ResultVO.failure(ResultEnum.INTERNAL_SERVER_ERROR);
        }

        return ResultVO.success(faceSet);
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public FaceSetAddFaceVO addFace(FaceSet faceSet, InputStream imageStream, boolean single) {

        // 获取缓存流
        ByteArrayOutputStream baos = SeetaFace6Util.toByteArrayOutputStream(imageStream);

        // 人脸检测
        SeetaImageData imageData = SeetaFace6Util.toSeetaImageData(new ByteArrayInputStream(baos.toByteArray()));
        FaceCorpResult[] faces = seetaFace6Manager.cropFaces(imageData);

        if (faces == null || faces.length == 0) {
            return null;
        }

        // 保存原图
        String rootPath = faceImgPath + File.separator + faceSet.getFaceSetToken();
        String fileName = NanoIdUtils.randomNanoId(8);
        byte[] imageByte = SeetaFace6Util.toBytes(new ByteArrayInputStream(baos.toByteArray()));
        String imagePath = SeetaFace6Util.writerImage(imageByte, rootPath, fileName + ".jpg");
        String imageHash = Hashing.sha256().hashBytes(imageByte).toString();

        // 组装数据库对象
        FaceSetRes faceSetRes = new FaceSetRes();
        faceSetRes.setFaceSetId(faceSet.getId());
        faceSetRes.setOriginalImagePath(imagePath);
        faceSetRes.setOriginalImageHash(imageHash);
        faceSetRes.setCreatedTime(new Date());
        faceSetRes.setUpdatedTime(new Date());

        // 返回结果
        FaceSetAddFaceVO result = new FaceSetAddFaceVO();
        result.setFaceSetToken(faceSet.getFaceSetToken());
        result.setOuterId(faceSet.getOuterId());
        result.setDisplayName(faceSet.getDisplayName());

        // 人脸集合
        List<FaceInfoVO> faceList = Lists.newArrayList();

        // 计算人脸区域特征
        for (int i = 0; i < faces.length; i++) {

            FaceCorpResult face = faces[i];

            // 计算特征值
            float[] faceFeature = seetaFace6Manager.extractCroppedFace(face.data);

            // 保存人脸照片
            String faceImagePath = SeetaFace6Util.writerCorpImage(face.data, rootPath, fileName + "_face" + i + ".jpg");

            // 组装数据库对象
            faceSetRes.setFaceEncoding(faceFeature);
            faceSetRes.setFaceFeature(SeetaFace6Util.encodeFeature(faceFeature));
            faceSetRes.setFaceToken(NanoIdUtils.randomNanoId(12, new char[]{'_', '-'}));
            faceSetRes.setFaceImagePath(faceImagePath);

            // 入库
            long id = faceSetResService.save(faceSetRes);

            // 入库成功
            if (id > 0) {
                // 保存向量数据库
                faceSearchService.addFace(faceSet.getFaceSetToken(), faceSetRes);

                // 更新人脸库数量
                faceSetService.updateFaceNumberById(1, faceSet.getId());

                // 封装返回结果
                FaceInfoVO faceInfoVO = new FaceInfoVO();
                faceInfoVO.setFaceToken(faceSetRes.getFaceToken());
                faceInfoVO.setBoundingBox(new FaceBoundingBoxVO(face.x, face.y, face.width, face.height, face.score));
                faceList.add(faceInfoVO);
            }

            // 如果只取一张
            if (single) {
                break;
            }
        }
        result.setFaces(faceList);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO batchImportFace(FaceSet faceSet, String imagePath) {
        Path path = Paths.get(imagePath);
        if (!path.toFile().exists()) {
            return ResultVO.failure(-1, "图片目录不存在");
        }

        List<FaceInfoVO> faceList = Lists.newArrayList();

        // 获取目录下所有图片
        try (Stream<Path> fileList = Files.list(path)) {
            List<Path> imageList = fileList.filter(SeetaFace6Util::isImage).toList();
            for (Path image : imageList) {
                InputStream imageStream = Files.newInputStream(image);
                FaceSetAddFaceVO result = addFace(faceSet, imageStream, false);

                if (result != null && !result.getFaces().isEmpty()) {
                    faceList.addAll(result.getFaces());
                }
            }
        } catch (IOException e) {
            log.error("获取图片文件发生异常", e);
            return ResultVO.failure(500, "获取图片文件发生异常");
        }

        // 返回结果
        FaceSetAddFaceVO result = new FaceSetAddFaceVO();
        result.setFaceSetToken(faceSet.getFaceSetToken());
        result.setOuterId(faceSet.getOuterId());
        result.setDisplayName(faceSet.getDisplayName());
        result.setFaces(faceList);

        return ResultVO.success(result);
    }

    @Override
    public ResultVO removeFace(FaceSet faceSet, String faceTokens) {

        // 返回结果
        Map<String, Object> result = Maps.newHashMap();

        // 人脸保存路径
        String rootPath = faceImgPath + File.separator + faceSet.getFaceSetToken();

        // 删除所有人脸
        if ("RemoveAllFaceTokens".equals(faceTokens)) {
            try {
                Path path = Paths.get(rootPath);
                if (Files.exists(path)) {
                    // 删除磁盘文件
                    MoreFiles.deleteRecursively(path, RecursiveDeleteOption.ALLOW_INSECURE);
                }

                // 删除数据库数据
                faceSetResService.deleteByFaceSetId(faceSet.getId());
                faceSetService.updateFaceNumberById(-faceSet.getFaceNumber(), faceSet.getId());
                result.put("face_removed", faceSet.getFaceNumber());
            } catch (IOException e) {
                return ResultVO.failure(ResultEnum.INTERNAL_SERVER_ERROR);
            }
        } else {
            List<String> faceTokenList = Lists.newArrayList(faceTokens.split(","));
            int faceRemovedNum = 0;
            result.put("face_count", faceTokenList.size());

            // 查询数据库人脸并删除
            List<FaceSetRes> faceList = faceSetResService.selectByFaceToken(faceSet.getId(), faceTokenList);
            for (FaceSetRes face : faceList) {
                SeetaFace6Util.deleteFile(rootPath + face.getOriginalImagePath());
                SeetaFace6Util.deleteFile(rootPath + face.getFaceImagePath());
                faceSetResService.deleteById(face.getId());
                faceSetService.updateFaceNumberById(-1, faceSet.getId());
                faceRemovedNum++;
            }
            result.put("face_removed", faceRemovedNum);
        }

        return ResultVO.success(result);
    }

    @Override
    public List<FaceSearchVO> search(FaceSet faceSet, InputStream imageStream, Integer topN) {
        // 获取输入人脸的特征值
        float[] imageFeature = seetaFace6Manager.extractMaxFace(imageStream);

        // 向量数据库搜索
        List<FaceSearchVO> result = searchWithMilvus(faceSet.getFaceSetToken(), imageFeature, topN);

        // 根据结果获取图片信息
        if (result != null && !result.isEmpty()) {
            for (FaceSearchVO faceSearchVO : result) {
                FaceSetRes faceSetRes = faceSetResService.selectByFaceToken(faceSearchVO.getFaceToken());
                faceSearchVO.setImageUrl(faceSetRes.getOriginalImagePath());
            }
        }

        return result;
    }

    @Override
    public List<FaceSearchVO> search(FaceSet faceSet, String faceToken, Integer topN) {

        // 获取输入人脸的特征值
        FaceSetRes faceSetRes = faceSetResService.selectByFaceToken(faceToken);
        float[] imageFeature = SeetaFace6Util.bytesToFloats(BaseEncoding.base64().decode(faceSetRes.getFaceFeature()));

        return searchWithMilvus(faceSet.getFaceSetToken(), imageFeature, topN);
    }

    private List<FaceSearchVO> searchWithES(String faceSetToken, float[] imageFeature, Integer topN) {
        List<FaceSetRes> faceSetResResult = Lists.newArrayList();

        // 采用ES相识度查询
        try {
            faceSetResResult = faceSearchService.searchFace(faceSetToken, imageFeature, topN);
        } catch (Exception e) {
            log.error("人脸资源Elasticsearch查询发生异常", e);
        }
        if (faceSetResResult.isEmpty()) {
            return Collections.emptyList();
        }

        // 封装返回结果
        List<FaceSearchVO> result = Lists.newArrayList();
        for (FaceSetRes face : faceSetResResult) {
            FaceSearchVO faceSearchVO = new FaceSearchVO();
            faceSearchVO.setFaceToken(face.getFaceToken());
            faceSearchVO.setSimilarity(face.getConfidence());
            result.add(faceSearchVO);
        }

        return result;
    }

    private List<FaceSearchVO> searchWithMilvus(String faceSetToken, float[] imageFeature, Integer topN) {
        List<FaceSetRes> faceSetResResult = Lists.newArrayList();
        // 采用Milvus相识度查询
        try {
            faceSetResResult = faceSearchService.searchFace(faceSetToken, imageFeature, topN);
        } catch (Exception e) {
            log.error("人脸资源Elasticsearch查询发生异常", e);
        }

        if (faceSetResResult == null || faceSetResResult.isEmpty()) {
            return Collections.emptyList();
        }

        // 封装返回结果
        List<FaceSearchVO> result = Lists.newArrayList();
        for (FaceSetRes face : faceSetResResult) {
            FaceSearchVO faceSearchVO = new FaceSearchVO();
            faceSearchVO.setFaceToken(face.getFaceToken());
            faceSearchVO.setSimilarity(face.getConfidence());
            result.add(faceSearchVO);
        }

        return result;
    }
}
