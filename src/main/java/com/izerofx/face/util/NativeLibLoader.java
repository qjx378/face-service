package com.izerofx.face.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * className: NativeLibLoader<br>
 * description: 本地库加载器<br>
 * createDate: 2024年01月05日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public class NativeLibLoader {
    private static final Logger logger = LoggerFactory.getLogger(NativeLibLoader.class);

    private static final String SEETAFACE6 = "seetaface6";

    private static final String SEETAFACE6_LIBRARY_PACKAGE = "com/izerofx/face/sdk";

    private static final String WINDOWS_LIBRARY_EXTENSION = ".dll";

    private static final String LINUX_LIBRARY_EXTENSION = ".so";

    /**
     * 是否加载完成
     */
    public static volatile boolean isLoaded = false;

    /**
     * 加载本地库
     *
     * @param libs  库名数组
     * @param isGPU 是否GPU
     */
    public synchronized static void load(String[] libs, boolean isGPU) {
        if (isLoaded) {
            return;
        }
        if (libs == null || libs.length == 0) {
            return;
        }

        int loadNum = 0;
        for (String libName : libs) {
            if (SystemUtils.IS_OS_WINDOWS && !StringUtils.endsWith(libName, WINDOWS_LIBRARY_EXTENSION)) {
                libName += WINDOWS_LIBRARY_EXTENSION;
            } else if (SystemUtils.IS_OS_LINUX && !StringUtils.endsWith(libName, LINUX_LIBRARY_EXTENSION)) {
                libName += LINUX_LIBRARY_EXTENSION;
            }
            if (loadFile(libName, isGPU)) {
                loadNum++;
            }
        }

        isLoaded = loadNum == libs.length;
    }

    /**
     * 加载文件
     *
     * @param fileName 原文件
     */
    public static boolean loadFile(String fileName, boolean isGPU) {
        String srcPath = getPrefix(isGPU) + fileName;
        try {
            File file = copyLibrary2TempDir(srcPath);
            if (file == null) {
                logger.warn("Get {} Error.", srcPath);
                return false;
            }
            String filePath = file.getAbsolutePath();
            System.load(filePath);
            logger.info("Load {} Successful.", filePath);
            return true;
        } catch (IOException e) {
            logger.error("Load Library Error.", e);
        }
        return false;
    }

    /**
     * 复制资源到临时目录
     *
     * @param srcPath 原路径
     * @return 临时目录文件
     * @throws IOException IO异常
     */
    private static File copyLibrary2TempDir(String srcPath) throws IOException {
        // 创建临时文件
        String tempDir = SystemUtils.JAVA_IO_TMPDIR + File.separator + SEETAFACE6 + File.separator;
        String fileName = FilenameUtils.getBaseName(srcPath) + FilenameUtils.EXTENSION_SEPARATOR + FilenameUtils.getExtension(srcPath);
        File tempFile = new File(tempDir + fileName);
        Files.createDirectories(tempFile.getParentFile().toPath());
        if (logger.isDebugEnabled()) {
            logger.debug("Copy {} To {}", srcPath, tempFile.getAbsolutePath());
        }

        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources(srcPath);
        if (resources.length == 0) {
            return null;
        }

        try (InputStream input = resources[0].getInputStream(); FileOutputStream output = new FileOutputStream(tempFile)) {
            FileCopyUtils.copy(input, output);
            tempFile.deleteOnExit();
            return tempFile;
        } catch (Exception e) {
            logger.error("Copy Resource To Temp Path Error.", e);
        }

        return null;
    }

    /**
     * 返回库文件路径前缀
     *
     * @return 库文件路径前缀
     */
    private static String getPrefix(boolean isGPU) {
        String os;
        if (SystemUtils.IS_OS_WINDOWS) {
            os = "/windows-x86_64";
        } else if (SystemUtils.IS_OS_LINUX) {
            os = "/linux-x86_64";
        } else {
            throw new RuntimeException("暂无支持该系统");
        }
        return SEETAFACE6_LIBRARY_PACKAGE + os + (isGPU ? "-gpu" : "") + File.separator;
    }
}
