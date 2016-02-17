package com.platform.common.web;

import com.platform.common.config.ResultCode;


/**
 * 功能描述：处理结果Bean.</p>
 *
 * @author   andy.zheng
 * @version  0.1.0, 2015年6月1日 上午10:41:26
 * @since    QN-War
 */
public class HandleResultBean<T> {

    private ResultCode resultCode;
    
    private T result;

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode code) {
        this.resultCode = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
