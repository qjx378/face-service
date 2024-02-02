package com.izerofx.common.model;

/**
 * className: ResultEnum.java<br>
 * description: 结果枚举<br>
 * 1000～1999 区间表示参数错误<br>
 * 2000～2999 区间表示用户错误<br>
 * 3000～3999 区间表示接口异常<br>
 * <p>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public enum ResultEnum {

    SUCCESS(0, "成功"),
    FAILURE(-1, "失败"),
    BAD_REQUEST(400, "错误的请求"),
    API_NOT_FOUND(404, "接口不存在"),
    HTTP_METHOD_NOT_SUPPORTED(405, "请求的http method不支持，请检查是否选择了正确的请求方式"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持当前媒体类型"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误，当此类错误发生时请再次请求，如果持续出现此类错误，请及时联系技术支持团队。"),
    PARAM_ERROR(10001, "参数错误，请参考API文档"),
    PARAMETER_ABSENT(10002, "缺少某个必选参数"),
    PARAMETER_CONFLICT(10003, "同时传入了要求是二选一或多选一的参数");


    private final Integer code;

    private final String message;


    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    @Override
    public String toString() {
        return Integer.toString(code);
    }


    public static ResultEnum valueOf(int code) {
        for (ResultEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + code + "]");
    }

}
