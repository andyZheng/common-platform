package com.platform.common.web;

import java.util.HashMap;
import java.util.Map;

import com.platform.common.config.ResultCode;

/**
 * 响应结果数据包装器。</p>
 *
 * Created by andy on 2015/5/20.
 */
public final class HandleResultWrapper {

    /** 响应消息ID Key */
    private final static String MESSAGE_ID_KEY = "msgid";

    /** 处理结果Key */
    private final static String HANDLE_RESULT_CODE_KEY = "result_code";

    /** 处理结果描述Key */
    private final static String HANDLE_RESULT_MESSAGE_KEY = "result_msg";

    /** 数据列表Key */
    private final static String RESULT_DATA_KEY = "result_data";

    /** 结果数据包装器 */
    private static HandleResultWrapper resultDataWrapper = new HandleResultWrapper();

    private HandleResultWrapper() {

    }

    public static HandleResultWrapper getInstance() {
        return resultDataWrapper;
    }

    /**
     * 包装响应数据。
     *
     * @param msgId
     *            响应消息ID.
     * @param result
     *            处理结果。
     * @return 响应数据。
     */
    public Map<String, Object> wrapped(int msgId, ResultCode result) {
        Map<String, Object> results = new HashMap<>();
        results.put(MESSAGE_ID_KEY, msgId);
        results.put(HANDLE_RESULT_CODE_KEY, result.getCode());
        results.put(HANDLE_RESULT_MESSAGE_KEY, result.getMsg());

        return results;
    }

    /**
     * 包装响应数据。
     *
     * @param msgId
     *            响应消息ID.
     * @param resultBean
     *            处理结果。
     * @return 包装后的响应数据。
     */
    public Map<String, Object> wrapped(int msgId, HandleResultBean<?> resultBean) {
        Map<String, Object> results = this.wrapped(msgId, resultBean.getResultCode());
        Object data = resultBean.getResult();
        if (null != data) {
            results.put(RESULT_DATA_KEY, data);
        }

        return results;
    }
}
