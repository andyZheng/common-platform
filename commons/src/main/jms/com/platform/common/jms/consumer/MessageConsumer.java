/*
 * 文件名：QueueListener.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 游戏数据采集平台v1.1
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.jms.consumer;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

import com.platform.common.jms.handler.MessageHandler;

/**
 * 功能描述：<code>MessageConsumer</code>是消息消费者实现类，本系统基于JMS消息中间件实现消息通讯。
 *
 * @author   Andy.zheng andy.zheng0807@gmail.com
 * @version  1.0, 2014年4月1日 下午3:26:00
 * @since    Common Platform/MessageComponent 1.0
 */
@Component
public class MessageConsumer implements MessageListener{
    
    /** 消息处理器 */
    private MessageHandler messageHandler;
 
    /**
     * 获取待消费的消息信息。
     * 
     * @param message 当前消息对象。
     * 
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(final Message message) {
        // 判定当前消息类型
        if(message instanceof TextMessage){ // 文本消息
            messageHandler.processTextMessage((TextMessage)message);
        }else if(message instanceof MapMessage){ // 键值对结构
            messageHandler.processMapMessage((MapMessage)message);
        }
    }
 
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
}