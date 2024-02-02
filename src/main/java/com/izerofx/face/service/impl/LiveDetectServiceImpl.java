package com.izerofx.face.service.impl;

import com.izerofx.face.model.vo.LiveDetectVO;
import com.izerofx.face.sdk.model.dto.PredictImageResult;
import com.izerofx.face.sdk.model.enmu.FaceAntiSpoofingStatus;
import com.izerofx.face.service.LiveDetectService;
import com.izerofx.face.util.SeetaFace6Util;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * className: LiveDetectServiceImpl<br>
 * description: 活体检测服务接口<br>
 * createDate: 2022年07月04日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Slf4j
@Service
public class LiveDetectServiceImpl implements LiveDetectService {

    @Resource
    private SeetaFace6Manager seetaFace6JNI;

    @Override
    public LiveDetectVO liveDetectFace(InputStream imageStream) {
        if (imageStream == null) {
            return null;
        }

        // 静默活体检测
        PredictImageResult predictImageResult = seetaFace6JNI.liveDetectFace(imageStream);

        //结果封装
        LiveDetectVO result = new LiveDetectVO();
        result.setAlive(predictImageResult.status == 0);
        result.setStatus(FaceAntiSpoofingStatus.valueOf(predictImageResult.status).getDesc());
        result.setConfidence(predictImageResult.reality);
        result.setFaceImageBase64(SeetaFace6Util.toBase64WithCorpImage(predictImageResult.picture));

        return result;
    }
}
