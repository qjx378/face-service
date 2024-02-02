package com.izerofx.common.exception.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.izerofx.common.exception.ErrorResult;
import com.izerofx.common.exception.ParameterValidException;
import com.izerofx.common.exception.SystemException;
import com.izerofx.common.exception.SystemRuntimeException;
import com.izerofx.common.model.ResultVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Date;

/**
 * className: ExceptionHandle.java<br>
 * description: 异常处理器<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@ControllerAdvice
public class ExceptionHandle extends ResponseEntityExceptionHandler {

    /**
     * objectMapper
     */
    @Resource
    private ObjectMapper objectMapper;

    /**
     * exceptionCors
     */
    @Resource
    private ExceptionCors exceptionCors;

    /**
     * httpServletResponse
     */
    @Resource
    private HttpServletResponse httpServletResponse;


    /**
     * 异常处理
     *
     * @param ex      ex
     * @param request request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exception(Exception ex, WebRequest request) throws Exception {

        // 异常处理
        ResponseEntity<Object> objectResponseEntity = null;
        try {
            objectResponseEntity = this.handleException(ex, request);
        } catch (Exception e) {
            if (ex instanceof ParameterValidException ||
                    ex instanceof SystemRuntimeException ||
                    ex instanceof SystemException ||
                    ex instanceof RestClientResponseException ||
                    ex instanceof MethodArgumentNotValidException) {
                logger.warn("Unknown exception type: " + ex.getClass().getName());
                HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
                HttpHeaders headers = new HttpHeaders();
                return this.handleExceptionInternal(ex, null, headers, status, request);
            }
        }

        return this.handleExceptionInternal(ex, null, objectResponseEntity != null ? objectResponseEntity.getHeaders() : new HttpHeaders(), objectResponseEntity != null ? objectResponseEntity.getStatusCode() : HttpStatusCode.valueOf(500), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        // cors特殊处理
        exceptionCors.fixCors(httpServletResponse);

        // 异常处理
        HttpStatusCode localHttpStatus = statusCode;
        ErrorResult errorResult = buildError(ex);

        // 1.参数校验异常
        // 2.rest请求异常
        if (ex instanceof ParameterValidException) {
            localHttpStatus = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof RestClientResponseException) {
            try {
                RestClientResponseException restClientResponseException = (RestClientResponseException) ex;
                String data = restClientResponseException.getResponseBodyAsString();
                if (StringUtils.isNotBlank(data)) {
                    ResultVO child = objectMapper.readValue(data, objectMapper.getTypeFactory().constructParametricType(ResultVO.class, String.class));
                    errorResult.setChild(child);
                }
            } catch (IOException e) {
                throw new SystemRuntimeException(e);
            }
            // 参数校验异常
        } else if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            try {
                errorResult.setMessage(objectMapper.writeValueAsString(methodArgumentNotValidException.getBindingResult().getAllErrors()));
            } catch (JsonProcessingException e) {
                throw new SystemRuntimeException(e);
            }
        }
        ResultVO restResponse = ResultVO.failure(errorResult, localHttpStatus);
        return super.handleExceptionInternal(ex, restResponse, headers, localHttpStatus, request);
    }

    /**
     * 描述 : 构造错误响应对象
     *
     * @param throwable 异常
     * @return 错误响应对象
     */
    public static ErrorResult buildError(Throwable throwable) {
        ErrorResult error = new ErrorResult();
        error.setType(throwable.getClass().getName());
        error.setMessage(ExceptionUtils.getMessage(throwable));
        error.setStackTrace(ExceptionUtils.getStackTrace(throwable));
        error.setDate(new Date());
        return error;
    }
}
