package com.izerofx.common.util;

import lombok.NoArgsConstructor;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

/**
 * className: RequestIdUtil<br>
 * description: 请求ID工具类<br>
 * createDate: 2024年01月04日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@NoArgsConstructor
public class RequestIdUtil {

    private static final String REQUEST_ID = "RequestId";

    /**
     * 当RequestId为空时，显示的RequestId。随意。
     */
    private static final String DEFAULT_REQUEST_ID = "0";

    /**
     * 设置请求ID
     *
     * @param requestId 请求ID
     */
    public static void set(String requestId) {
        // 如果参数为空，则设置默认请求ID
        requestId = !StringUtils.hasText(requestId) ? DEFAULT_REQUEST_ID : requestId;
        // 将请求ID放到MDC中
        MDC.put(REQUEST_ID, requestId);
    }

    /**
     * 获取请求ID
     *
     * @return 请求ID
     */
    public static String get() {
        // 获取
        String requestId = MDC.get(REQUEST_ID);
        // 如果请求ID为空，则返回默认值
        return !StringUtils.hasText(requestId) ? DEFAULT_REQUEST_ID : requestId;
    }

    /**
     * 判断请求ID为默认值
     *
     * @param requestId 请求ID
     * @return true:默认值 false:非默认值
     */
    public static Boolean isDefault(String requestId) {
        return DEFAULT_REQUEST_ID.equals(requestId);
    }

    /**
     * 生成请求ID
     *
     * @return 请求ID
     */
    public static String genRequestId() {
        return NanoIdUtils.randomNanoId(16, "_-".toCharArray()).toLowerCase();
    }
}
