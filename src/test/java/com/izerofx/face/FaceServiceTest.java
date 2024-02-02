package com.izerofx.face;

import com.izerofx.face.sdk.model.dto.SeetaImageData;
import com.izerofx.face.sdk.model.dto.SeetaPointF;
import com.izerofx.face.sdk.model.dto.SeetaRect;
import com.izerofx.face.service.impl.SeetaFace6Manager;
import com.izerofx.face.util.SeetaFace6Util;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * className: FaceServiceTest<br>
 * description: <br>
 * createDate: 2023年12月06日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@SpringBootTest
public class FaceServiceTest {

    @Resource
    private SeetaFace6Manager seetaFace6Manager;


    void faceDetect() {

        try (InputStream inputStream = Files.newInputStream(Paths.get("E:\\workspaces\\izerofx-face-demo\\src\\main\\resources\\static\\img\\demo\\d30.jpg"))) {
            SeetaImageData imageData = SeetaFace6Util.toSeetaImageData(inputStream);
            SeetaRect[] seetaRects = seetaFace6Manager.detect(imageData);

            for (SeetaRect seetaRect : seetaRects) {
                BufferedImage bufferedImage = SeetaFace6Util.writeRect(SeetaFace6Util.toBufferedImage(Files.newInputStream(Paths.get("E:\\workspaces\\izerofx-face-demo\\src\\main\\resources\\static\\img\\demo\\d30.jpg"))), seetaRect);

                SeetaFace6Util.writerImage(SeetaFace6Util.toBytes(bufferedImage), "E:\\workspaces\\izerofx-face-demo\\src\\main\\resources\\static\\img\\demo", "d30_1.jpg");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void faceLandmark() {
        try (InputStream inputStream = Files.newInputStream(Paths.get("E:\\workspaces\\izerofx-face-demo\\src\\main\\resources\\static\\img\\demo\\a1.jpg"))) {
            SeetaImageData imageData = SeetaFace6Util.toSeetaImageData(inputStream);
            SeetaRect[] seetaRects = seetaFace6Manager.detect(imageData);

            for (SeetaRect seetaRect : seetaRects) {
                SeetaPointF[] landmark = seetaFace6Manager.mark(imageData, seetaRect);
                Stream.of(landmark).forEach(item -> {
                    System.out.println("X：" + item.x + " Y：" + item.y);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
