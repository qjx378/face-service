package com.izerofx.configuration;

import com.izerofx.face.properties.SeetaFaceProperties;
import com.izerofx.face.sdk.SeetaFace6;
import com.izerofx.face.sdk.model.enmu.SeetaDevice;
import com.izerofx.face.util.NativeLibLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * className: SeetaFaceConfiguration<br>
 * description: SeetaFace6配置类<br>
 * createDate: 2024年01月07日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Configuration
public class SeetaFaceConfiguration {

    /**
     * 必要库依赖
     */
    private static final String[] NATIVE_LIBS = {
            "tennis", "SeetaAuthorize",
            "SeetaMaskDetector200", "SeetaEyeStateDetector200", "SeetaPoseEstimation600", "SeetaQualityAssessor300",
            "SeetaAgePredictor600", "SeetaFaceAntiSpoofingX600", "SeetaFaceLandmarker600", "SeetaFaceDetector600",
            "SeetaFaceTracking600", "SeetaGenderPredictor600", "SeetaFaceRecognizer610",
            "SeetaFace6"};

    @Bean
    public SeetaFace6 seetaFace6(SeetaFaceProperties properties) {
        boolean isGPU = SeetaDevice.GPU.equals(properties.getDevice());
        NativeLibLoader.load(NATIVE_LIBS, isGPU);
        return new SeetaFace6();
    }
}
