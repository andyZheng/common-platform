/*
 * 文件名：JmsExceptionListener.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台 V1.1
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.jms.consumer;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 功能描述：<code>JmsExceptionListener</code>是JMS运行异常捕获监听器。
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2014年4月1日 下午3:31:18
 * @since    Common Platform/MessageComponent 1.0
 */
@Component
public class JmsExceptionListener implements ExceptionListener{

    /** 日志对象 */
    private static Logger logger = LoggerFactory.getLogger(JmsExceptionListener.class);
    
    /** 
     * 捕获JMS异常
     * 
     * @param e   当前异常信息
     * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
     */
    public void onException(JMSException e) {
        logger.error("The JMS runtime error.", e);
    }
}