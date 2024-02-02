package com.izerofx.face.util;

import com.google.common.io.ByteStreams;
import com.izerofx.face.sdk.model.dto.SeetaImageData;
import com.izerofx.face.sdk.model.dto.SeetaRect;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

/**
 * className: SeetaFace6Util<br>
 * description: SeetaFace6工具类<br>
 * createDate: 2022年06月21日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Slf4j
public class SeetaFace6Util {

    private static final int[] BGR_TYPE = {0, 1, 2};

    /**
     * 时间格式化
     */
    private static final FastDateFormat FAST_DATE_FORMAT = FastDateFormat.getInstance("yyyyMMdd");

    private SeetaFace6Util() {
    }

    /**
     * BufferedImage转为SeetaImage
     *
     * @param bufferedImage 图片
     * @return SeetaFace图片数据
     */
    public static SeetaImageData toSeetaImageData(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            throw new NullPointerException("图片不能为空.");
        }
        try {
            SeetaImageData imageData = new SeetaImageData();
            imageData.width = bufferedImage.getWidth();
            imageData.height = bufferedImage.getHeight();
            imageData.channels = 3;
            imageData.data = getBgr(bufferedImage);
            return imageData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            bufferedImage.getGraphics().dispose();
        }
    }

    /**
     * 图片转bgr字节数组
     *
     * @param image 图片
     * @return bgr字节数组
     */
    private static byte[] getBgr(BufferedImage image) {
        byte[] matrixBGR;
        if (isBgr(image)) {
            matrixBGR = (byte[]) image.getData().getDataElements(0, 0, image.getWidth(), image.getHeight(), null);
        } else {
            // ARGB格式图像数据
            int[] intrgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
            matrixBGR = new byte[image.getWidth() * image.getHeight() * 3];
            // ARGB转BGR格式
            for (int i = 0; i < intrgb.length; i++) {
                matrixBGR[i * 3] = (byte) (intrgb[i] & 0xff);
                matrixBGR[i * 3 + 1] = (byte) ((intrgb[i] >> 8) & 0xff);
                matrixBGR[i * 3 + 2] = (byte) ((intrgb[i] >> 16) & 0xff);
            }
        }
        return matrixBGR;
    }

    /**
     * 判断BufferedImage是否为bgr
     *
     * @param image 图片
     * @return 是否为bgr
     */
    private static boolean isBgr(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_3BYTE_BGR && image.getData().getSampleModel() instanceof ComponentSampleModel sampleModel) {
            return Arrays.equals(sampleModel.getBandOffsets(), BGR_TYPE);
        }
        return false;
    }

    /**
     * Java.io.File转seetaImageData
     *
     * @param path 文件路径
     * @return SeetaFace图片数据
     */
    public static SeetaImageData toSeetaImageData(String path) {
        return toSeetaImageData(new File(path));
    }

    /**
     * 转为seetaImageData
     *
     * @param file 文件
     * @return SeetaFace图片数据
     */
    public static SeetaImageData toSeetaImageData(File file) {
        return toSeetaImageData(toBufferedImage(file));
    }

    /**
     * 转为seetaImageData
     *
     * @param imageStream 图片流
     * @return SeetaFace图片数据
     */
    public static SeetaImageData toSeetaImageData(InputStream imageStream) {
        return toSeetaImageData(toBufferedImage(imageStream));
    }

    /**
     * 文件转BufferedImage
     *
     * @param path 图片路径
     * @return BufferedImage
     */
    public static BufferedImage toBufferedImage(String path) {
        return toBufferedImage(new File(path));
    }

    /**
     * Java.io.File转BufferedImage
     *
     * @param file 文件
     * @return BufferedImage
     */
    public static BufferedImage toBufferedImage(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            log.error("转换异常", e);
        }
        return image;
    }

    /**
     * InputStream转BufferedImage
     *
     * @param imageStream 图片流
     * @return BufferedImage
     */
    public static BufferedImage toBufferedImage(InputStream imageStream) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageStream);
        } catch (IOException e) {
            log.error("转换异常", e);
        }
        return image;
    }

    /**
     * bgr转图片
     *
     * @param data   字节数据
     * @param width  宽
     * @param height 高
     * @return BufferedImage
     */
    public static BufferedImage toBufferedImage(byte[] data, int width, int height) {
        int type = BufferedImage.TYPE_3BYTE_BGR;
        // bgr to rgb
        byte b;
        for (int i = 0; i < data.length; i = i + 3) {
            b = data[i];
            data[i] = data[i + 2];
            data[i + 2] = b;
        }
        BufferedImage image = new BufferedImage(width, height, type);
        image.getRaster().setDataElements(0, 0, width, height, data);
        return image;
    }

    /**
     * SeetaImageData转BufferedImage
     *
     * @param image SeetaFace图片数据
     * @return BufferedImage
     */
    public static BufferedImage toBufferedImage(SeetaImageData image) {
        return toBufferedImage(image.data, image.width, image.height);
    }

    /**
     * 绘制边框
     *
     * @param image BufferedImage
     * @param rect  边框
     * @return BufferedImage
     */
    public static BufferedImage writeRect(BufferedImage image, SeetaRect rect) {
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics gra = bi.getGraphics();
        gra.drawImage(image, 0, 0, null);
        gra.setColor(Color.RED);
        gra.drawRect(rect.x, rect.y, rect.width, rect.height);
        gra.dispose();
        return bi;
    }

    /**
     * 获取文件类型
     *
     * @param stream 输入流
     * @return 文件类型
     */
    public static MediaType getMediaType(InputStream stream) {
        try {
            return new DefaultDetector().detect(stream, new Metadata());
        } catch (IOException e) {
            log.error("获取文件类型发生异常", e);
        }

        return null;
    }

    /**
     * 检查是否为图片
     *
     * @param stream 输入流
     * @return 是否为图片
     */
    public static boolean isImage(InputStream stream) {
        MediaType mediaType = getMediaType(stream);

        if (log.isDebugEnabled()) {
            log.debug("mediaType: {}", mediaType);
        }

        if (mediaType == null) {
            return true;
        }
        return mediaType.toString().startsWith("image/");
    }

    /**
     * 检测是否图片
     *
     * @param path 文件路径
     * @return 是否为图片
     */
    public static boolean isImage(Path path) {
        if (path.toFile().isDirectory()) {
            return true;
        }
        boolean result = false;
        try (InputStream is = Files.newInputStream(path);
             BufferedInputStream bis = new BufferedInputStream(is)
        ) {
            result = isImage(bis);
        } catch (IOException e) {
            log.error("判断是否图片出错了", e);
        }
        return result;
    }

    /**
     * BufferedImage转byte[]
     *
     * @param image BufferedImage转byte
     * @return 字节数组
     */
    public static byte[] toBytes(BufferedImage image) {
        byte[] bytes = null;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, "PNG", out);
            bytes = out.toByteArray();
        } catch (IOException e) {
            log.error("BufferedImage转byte[]", e);
        }
        return bytes;
    }

    /**
     * 输入流转byte[]
     *
     * @param stream 输入流
     * @return 字节数组
     */
    public static byte[] toBytes(InputStream stream) {
        byte[] bytes = null;
        try {
            bytes = ByteStreams.toByteArray(stream);
        } catch (IOException e) {
            log.error("输入流转byte[]发生异常", e);
        }
        return bytes;
    }

    /**
     * float数组转byte数组
     *
     * @param floats float数组
     * @return byte数组
     */
    public static byte[] floatsToBytes(float[] floats) {
        ByteBuffer buffer = ByteBuffer.allocate(4 * floats.length);
        FloatBuffer floatBuffer = buffer.asFloatBuffer();
        floatBuffer.put(floats);
        return buffer.array();
    }

    /**
     * byte数组转float数组
     *
     * @param bytes byte数组
     * @return float数组
     */
    public static float[] bytesToFloats(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        FloatBuffer fb = buffer.asFloatBuffer();
        float[] floatArray = new float[fb.limit()];
        fb.get(floatArray);
        return floatArray;
    }

    /**
     * 将特征值编码为Base64字符串
     *
     * @param features float数组特征值
     * @return Base64字符串
     */
    public static String encodeFeature(float[] features) {
        byte[] byteFeatures = floatsToBytes(features);
        return Base64.getEncoder().encodeToString(byteFeatures);
    }

    /**
     * 将base64字符串特征值解码为float数组
     *
     * @param features 特征值Base64字符串
     * @return float数组特征值
     */
    public static float[] encodeFeature(String features) {
        byte[] byteFeatures = Base64.getDecoder().decode(features);
        return bytesToFloats(byteFeatures);
    }

    /**
     * 写图片文件
     *
     * @param bytes    图片二进制数组
     * @param rootPath 根目录
     * @param fileName 文件名
     * @return 文件路径
     */
    public static String writerImage(byte[] bytes, String rootPath, String fileName) {
        String filePath = File.separator + FAST_DATE_FORMAT.format(new Date()) + File.separator + fileName;
        try {
            File faceImageFile = new File(rootPath + filePath);
            com.google.common.io.Files.createParentDirs(faceImageFile);
            com.google.common.io.Files.write(bytes, faceImageFile);
        } catch (IOException e) {
            log.error("写图片文件发生异常", e);
        }
        return filePath;
    }

    /**
     * 裁剪后的BGR图片保存到文件
     *
     * @param bgrBytes BGR图片二进制数组
     * @param rootPath 根目录
     * @param fileName 文件名
     * @return 文件路径
     */
    public static String writerCorpImage(byte[] bgrBytes, String rootPath, String fileName) {
        String filePath = "";
        BufferedImage image = toBufferedImage(bgrBytes, 256, 256);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, FilenameUtils.getExtension(fileName), out);
            byte[] rgbBytes = out.toByteArray();
            filePath = writerImage(rgbBytes, rootPath, fileName);
        } catch (IOException e) {
            log.error("BGR转RGB发生异常", e);
        }
        return filePath;
    }

    /**
     * BGR二进制数据转RGB图片Base64字符串
     *
     * @param bgrBytes BGR二进制数组
     * @return 图片Base64字符串
     */
    public static String toBase64WithCorpImage(byte[] bgrBytes) {
        if (bgrBytes == null || bgrBytes.length == 0) {
            return "";
        }
        BufferedImage image = toBufferedImage(bgrBytes, 256, 256);
        byte[] rgbBytes = toBytes(image);
        return Base64.getEncoder().encodeToString(rgbBytes);
    }

    /**
     * 输入流转byte数组
     *
     * @param inputStream 输入流
     * @return byte数组
     */
    public static ByteArrayOutputStream toByteArrayOutputStream(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ByteStreams.copy(inputStream, baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return baos;
    }

    /**
     * 删除指定文件
     *
     * @param fullPath 完整路径包括文件名
     */
    public static void deleteFile(String fullPath) {
        File file = new File(fullPath);
        // 如果是文件夹
        if (!file.isDirectory() && file.isFile()) {
            try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                log.error("删除文件发生异常", e);
            }
        }
    }
}
