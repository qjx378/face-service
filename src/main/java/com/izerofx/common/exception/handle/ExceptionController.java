package com.izerofx.common.exception.handle;

import com.izerofx.common.exception.ErrorResult;
import com.izerofx.common.model.ResultVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * className: ExceptionController.java<br>
 * description: 异常页面控制<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 *
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ExceptionController extends AbstractErrorController {

    /**
     * exception
     */
    private static final String KEY_EXCEPTION = "exception";

    /**
     * message
     */
    private static final String KEY_MESSAGE = "message";

    /**
     * errorProperties
     */
    private final ErrorProperties errorProperties;

    /**
     * exceptionCors
     */
    @Autowired
    private ExceptionCors exceptionCors;

    /**
     * Create a new {@link ExceptionController} instance.
     *
     * @param errorAttributes the error attributes
     * @param serverProperties configuration properties
     * @param errorViewResolvers error view resolvers
     */
    @Autowired
    public ExceptionController(ErrorAttributes errorAttributes, ServerProperties serverProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(serverProperties.getError(), "ErrorProperties must not be null");
        this.errorProperties = serverProperties.getError();
    }

    /**
     * html错误
     *
     * @param request request
     * @param response response
     * @return ModelAndView
     */
    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        // cors特殊处理
        exceptionCors.fixCors(response);

        // 异常信息处理
        HttpStatus status = getStatus(request);

        Map<String, Object> model = getErrorAttributes(request, isIncludeStackTrace(request));
        ResultVO restResponse = this.getRestResponse(request, status, model);
        status = HttpStatus.valueOf(restResponse.getCode());
        model.put("apiResponse", restResponse);
        model.put(KEY_EXCEPTION, restResponse.getError().getType());
        model.put(KEY_MESSAGE, restResponse.getError().getMessage());
        response.setStatus(status.value());
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView == null ? new ModelAndView(this.errorProperties.getPath(), model) : modelAndView);
    }

    /**
     * json错误
     *
     * @param request request
     * @param response response
     * @return ResponseEntity
     */
    @RequestMapping
    @ResponseBody
    public ResponseEntity<ResultVO> error(HttpServletRequest request, HttpServletResponse response) {
        // cors特殊处理
        exceptionCors.fixCors(response);

        // 异常信息处理
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request));
        HttpStatus status = getStatus(request);
        ResultVO restResponse = getRestResponse(request, status, body);
        return new ResponseEntity<>(restResponse, HttpStatus.valueOf(restResponse.getCode()));
    }

    /**
     * 获得RestResponse
     *
     * @param request request
     * @param status status
     * @param body body
     * @return RestResponse
     */
    private ResultVO getRestResponse(HttpServletRequest request, HttpStatus status, Map<String, Object> body) { // NOSONAR
        ErrorResult errorResult = new ErrorResult();
        if (status == HttpStatus.NOT_FOUND) { // 404处理
            errorResult.setType(NoHandlerFoundException.class.getName());
            errorResult.setMessage(body.get("path").toString());
        } else { // 非404处理
            Object object = request.getAttribute("jakarta.servlet.error.exception");
            if (object instanceof Exception exception) { // 上下文中能拿到异常的情况
                errorResult = ExceptionHandle.buildError(exception);
            } else { // 上下文中拿不到异常的情况
                errorResult.setType(body.containsKey(KEY_EXCEPTION) ? body.get(KEY_EXCEPTION).toString() : "unknow exception");
                errorResult.setMessage(body.containsKey(KEY_MESSAGE) ? body.get(KEY_MESSAGE).toString() : "unknow message");
            }
        }
        errorResult.setDate(new Date());
        return ResultVO.failure(errorResult, status);
    }

    /**
     * Determine if the stacktrace attribute should be included.
     *
     * @param request the source request
     * @return if the stacktrace attribute should be included
     */
    protected ErrorAttributeOptions isIncludeStackTrace(HttpServletRequest request) { // NOSONAR
        ErrorProperties.IncludeAttribute include = getErrorProperties().getIncludeStacktrace();
        boolean bool = include == ErrorProperties.IncludeAttribute.ALWAYS || include == ErrorProperties.IncludeAttribute.ON_PARAM && getTraceParameter(request);
        return bool ? ErrorAttributeOptions.of(Include.STACK_TRACE) : ErrorAttributeOptions.defaults();
    }

    /**
     * Provide access to the error properties.
     *
     * @return the error properties
     */
    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }

}
