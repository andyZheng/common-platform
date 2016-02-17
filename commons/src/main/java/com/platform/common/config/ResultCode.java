package com.platform.common.config;

/**
 * 处理结果枚举器。</p>
 * 根据系统业务相关需求定义操作编码以及操作结果描述消息。约定规则如下:
 * <ol>
 *  <li> 当code > 0时，表示操作成功, 反之表示操作失败;</li>
 *  <li> 当操作编码绝对值即[code] = 1XX时，表示用户相关操作;</li>
 * </ol>
 *
 * Created by andy on 2015/5/20.
 */
public enum ResultCode {

    /************************ 操作成功   ****************/
    SUCCESS(1, "成功"),
    

    /************************ 操作失败   ****************/
    FAIL(0, "失败"),
    SESSION_TIME_OUT(-1, "会话超时"),
    
    
    ILLEGAL_REQUEST_PARAMETERS_ERROR(-50, "非法的请求参数"),
    
    
    // -1XX 用户相关操作
    USER_ACCOUNT_HAS_EXISTED(-100, "帐号名已存在"),
    USER_LOGIN_ERROR(-101, "帐号名不存在或者密码错误"),
   
    ROLE_NAME_EXISTED(-110, "角色名已存在")
    
    ;

    // 编码
    private int code;

    // 消息
    private String msg;

    private ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
