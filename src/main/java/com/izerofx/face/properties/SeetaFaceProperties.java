package com.izerofx.face.properties;

import com.izerofx.face.sdk.model.enmu.SeetaDevice;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * className: SeetaFaceProperties<br>
 * description: 人脸配置属性文件<br>
 * createDate: 2022年06月20日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.seetaface")
public class SeetaFaceProperties {

    /**
     * 模型目录
     */
    private String modelPath = "./models";

    /**
     * 设备类型
     */
    private SeetaDevice device = SeetaDevice.AUTO;

    /**
     * GPU设备ID
     */
    private int deviceId = 0;

    /**
     * 人脸图片存放目录
     */
    private String faceImagePath;
}
