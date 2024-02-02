package com.izerofx.common.util;

import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * className: NanoIdUtils.java<br>
 * description: 用于生成唯一字符串 ID 的类，此实现的逻辑基于JavaScript的NanoId实现，见：https://github.com/ai/nanoid<br>
 *
 * @author David Klebanoff
 */
public final class NanoIdUtils {

    private NanoIdUtils() {
    }

    /**
     * 默认随机数生成器，使用{@link SecureRandom}确保健壮性
     */
    public static final SecureRandom DEFAULT_NUMBER_GENERATOR = new SecureRandom();

    /**
     * 默认随机字母表，使用URL安全的Base64字符
     */
    public static final char[] DEFAULT_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 默认长度
     */
    public static final int DEFAULT_SIZE = 21;

    /**
     * 生成随机的NanoId字符串，长度为默认的{@link #DEFAULT_SIZE}，使用密码安全的随机生成器
     *
     * @return 随机生成的 NanoId 字符串
     */
    public static String randomNanoId() {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, DEFAULT_SIZE);
    }

    /**
     * 生成随机的NanoId字符串
     *
     * @param size ID长度
     * @return 随机生成的 NanoId 字符串
     */
    public static String randomNanoId(int size) {
        return randomNanoId(null, null, size);
    }

    /**
     * 生成随机的NanoId字符串
     *
     * @param size        ID长度
     * @param notAlphabet 不包含字母表数组
     * @return 随机生成的 NanoId 字符串
     */
    public static String randomNanoId(int size, char[] notAlphabet) {
        String alphabet = new String(DEFAULT_ALPHABET);
        if (notAlphabet != null) {
            for (char c : notAlphabet) {
                alphabet = StringUtils.replace(alphabet, String.valueOf(c), "");
            }
        }
        return randomNanoId(null, alphabet.toCharArray(), size);
    }

    /**
     * 生成随机的NanoId字符串
     *
     * @param random   随机数生成器
     * @param alphabet 随机字母表
     * @param size     ID长度
     * @return 随机生成的 NanoId 字符串
     */
    public static String randomNanoId(Random random, char[] alphabet, final int size) {

        if (random == null) {
            random = DEFAULT_NUMBER_GENERATOR;
        }

        if (alphabet == null) {
            alphabet = DEFAULT_ALPHABET;
        }

        if (alphabet.length == 0 || alphabet.length >= 256) {
            throw new IllegalArgumentException("alphabet must contain between 1 and 255 symbols.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than zero.");
        }

        final int mask = (2 << (int) Math.floor(Math.log(alphabet.length - 1) / Math.log(2))) - 1;
        final int step = (int) Math.ceil(1.6 * mask * size / alphabet.length);

        final StringBuilder idBuilder = new StringBuilder();

        while (true) {
            final byte[] bytes = new byte[step];
            random.nextBytes(bytes);
            for (int i = 0; i < step; i++) {
                final int alphabetIndex = bytes[i] & mask;
                if (alphabetIndex < alphabet.length) {
                    idBuilder.append(alphabet[alphabetIndex]);
                    if (idBuilder.length() == size) {
                        return idBuilder.toString();
                    }
                }
            }
        }
    }
}