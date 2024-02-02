package com.izerofx.common.web.filter;

import com.izerofx.common.util.RequestIdUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * className: LogMDCFilter<br>
 * description: 日志唯一追踪ID过滤器<br>
 * createDate: 2024年01月04日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public class LogMDCFilter implements Filter {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String REQUEST_ID = "requestId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            // traceId初始化
            initRequestId((HttpServletRequest) servletRequest);

            // 执行后续过滤器
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }

    /**
     * 初始化请求ID
     */
    private void initRequestId(HttpServletRequest request) {

        // 尝试获取http请求中的请求ID
        String requestId = request.getParameter(REQUEST_ID);
        if (!StringUtils.hasText(requestId)) {
            requestId = request.getHeader(REQUEST_ID_HEADER);
        }

        // 如果当前请求ID为空或者为默认请求ID，则生成新的请求ID
        if (!StringUtils.hasText(requestId) || RequestIdUtil.isDefault(requestId)) {
            requestId = RequestIdUtil.genRequestId();
        }

        // 设置请求ID
        RequestIdUtil.set(requestId);
    }
}
