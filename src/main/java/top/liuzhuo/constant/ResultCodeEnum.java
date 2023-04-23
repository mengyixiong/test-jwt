package top.liuzhuo.constant;

import lombok.Data;


public enum ResultCodeEnum {


    FORBIDDEN(403, "没有权限"),
    UNAUTHORIZED(401, "未登录" );

    private Integer code;
    private String msg;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.msg;
    }
}
