package com.izerofx.common.exception;

import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * className: ParameterValidException.java<br>
 * description:参数校验异常<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 *
 */
public class ParameterValidException extends RuntimeException {

    private static final long serialVersionUID = -5553782057112867647L;
    
    /**
     * 参数静态校验错误信息
     */
    private final List<ObjectError> allErrors;

    /**
     * 构造函数
     *
     * @param message 异常信息
     * @param allErrors 参数静态校验错误信息
     */
    public ParameterValidException(String message, List<ObjectError> allErrors) {
        super(message);
        this.allErrors = allErrors;
    }

    /**
     * 构造函数
     *
     * @param cause 堆栈
     * @param allErrors 参数静态校验错误信息
     */
    public ParameterValidException(Throwable cause, List<ObjectError> allErrors) {
        super(cause);
        this.allErrors = allErrors;
    }

    /**
     * 构造函数
     *
     * @param message 异常信息
     * @param cause 堆栈
     * @param allErrors 参数静态校验错误信息
     */
    public ParameterValidException(String message, Throwable cause, List<ObjectError> allErrors) {
        super(message, cause);
        this.allErrors = allErrors;
    }

    /**
     * 获取allErrors
     *
     * @return the allErrors
     */
    public List<ObjectError> getAllErrors() {
        return allErrors;
    }
}
