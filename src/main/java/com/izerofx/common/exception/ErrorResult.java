package com.izerofx.common.exception;

import com.izerofx.common.model.ResultVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * className: ErrorResult.java<br>
 * description: 错误信息结果<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 *
 */
@Data
public class ErrorResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 异常时间
     */
    private Date date;

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 异常类名
     */
    private String type;

    /**
     * 异常信息
     */
    private String message;

    /**
     * 详细异常堆栈信息
     */
    private String stackTrace;

    /**
     * 异常子级
     */
    private ResultVO child;

}
