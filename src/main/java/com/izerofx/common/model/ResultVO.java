package com.izerofx.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.izerofx.common.exception.ErrorResult;
import com.izerofx.common.util.RequestIdUtil;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * className: ResultVO<br>
 * description: 结果视图对象<br>
 * createDate: 2020年1月3日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Data
@JsonPropertyOrder({"request_id", "code", "msg", "data", "error"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -212265258752457096L;

    /**
     * 响应ID
     */
    private String requestId = RequestIdUtil.get();

    /**
     * 状态码(业务定义)
     */
    private Integer code = HttpStatus.OK.value();

    /**
     * 状态码描述(业务定义)
     */
    private String msg;

    /**
     * 数据
     */
    private Object data;

    /**
     * 错误
     */
    private ErrorResult error;

    /**
     * 创建一个新的实例 ApiResult.
     */
    public ResultVO() {
        super();
    }

    /**
     * 成功
     *
     * @return 结果视图对象
     */
    public static ResultVO success() {
        return ResultVO.success(null);
    }

    /**
     * 成功
     *
     * @param data 数据
     * @return 结果视图对象
     */
    public static ResultVO success(Object data) {
        ResultVO result = new ResultVO();
        result.setCode(ResultEnum.SUCCESS.code());
        result.setMsg(ResultEnum.SUCCESS.message());
        result.setData(data);
        return result;
    }

    /**
     * 失败
     *
     * @return 结果视图对象
     */
    public static ResultVO failure() {
        return ResultVO.failure(ResultEnum.FAILURE);
    }

    /**
     * 失败
     *
     * @param resultCode 错误码
     * @return 结果视图对象
     */
    public static ResultVO failure(ResultEnum resultCode) {
        ResultVO result = new ResultVO();
        result.setCode(resultCode.code());
        result.setMsg(resultCode.message());
        return result;
    }

    /**
     * 失败
     *
     * @param error  错误结果
     * @param status 状态码
     * @return 结果视图对象
     */
    public static ResultVO failure(ErrorResult error, HttpStatusCode status) {
        ResultVO result = new ResultVO();
        result.setCode(status.value());
        result.setError(error);
        return result;
    }


    /**
     * 失败
     *
     * @param error      错误结果
     * @param resultCode 错误码
     * @return 结果视图对象
     */
    public static ResultVO failure(ErrorResult error, ResultEnum resultCode) {
        ResultVO result = new ResultVO();
        result.setCode(resultCode.code());
        result.setMsg(resultCode.message());
        result.setError(error);
        return result;
    }

    /**
     * 失败
     *
     * @param errorCode 错误码
     * @param errorMsg  错误消息
     * @return 结果视图对象
     */
    public static ResultVO failure(Integer errorCode, String errorMsg) {
        ResultVO result = new ResultVO();
        result.setCode(errorCode);
        result.setMsg(errorMsg);
        return result;
    }

}
