package com.izerofx.common.exception;

/**
 * className: SystemException.java<br>
 * description: 系统异常<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 *
 */
public class SystemException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public SystemException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param cause 堆栈
     */
    public SystemException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     *
     * @param message 异常信息
     * @param cause 堆栈
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

}
