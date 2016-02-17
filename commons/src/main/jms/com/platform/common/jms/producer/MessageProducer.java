/*
 * 文件名：MessageProducer.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台 v1.1
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.jms.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * 功能描述：<code>MessageProducer</code>是消息生产者实现类，本系统基于JMS消息中间件实现消息通讯。
 *
 * @author   Andy.zheng andy.zheng0807@gmail.com
 * @version  1.0, 2014年4月1日 下午4:19:45
 * @since    Common-Platform/MesssageComponent 1.0
 */
@Component
public class MessageProducer {

    /** 日志对象 */
    private static Logger logger = LoggerFactory.getLogger(MessageProducer.class);
    
    /** 动态注入JMS模板对象 */
    @Autowired
    private JmsTemplate jmsTemplate;
    
    
    /**
     * 功能描述：发送消息。
     *
     * @param message           待发送的消息。
     * @param destinationName   待发送消息到消息队列的名称。
     */
    public void send(final Object message, final String... destinationName){
        try {
            if(null != destinationName && 1 == destinationName.length){
                this.jmsTemplate.convertAndSend(destinationName[0], message);
            }else{
                this.jmsTemplate.convertAndSend(message);
            }
            logger.debug(String.format("The message [%s] send success!", message));
        } catch (JmsException e) {
            logger.error(String.format("The message [%s] send fail!\n", e));
        }
    }
    
}