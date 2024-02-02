package com.izerofx.common.exception;

/**
 * className: SystemRuntimeException.java<br>
 * description: 系统运行时异常<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 *
 */
public class SystemRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public SystemRuntimeException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param cause 堆栈
     */
    public SystemRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     *
     * @param message 异常信息
     * @param cause 堆栈
     */
    public SystemRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
