/*
 * 文件名：MessageHandler.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.jms.handler;

import javax.jms.MapMessage;
import javax.jms.TextMessage;

/**
 * 功能描述：<code>MessageHandler</code>类是消息处理器，根据JMS支持的消息类型定义了相关处理接口。
 * <p>
 *
 * @author   andy.zheng  andy.zheng0807@gmail.com
 * @version  1.0, 2014年4月30日 上午11:54:24
 * @since    Common-Platform 1.0
 */
public interface MessageHandler {

    /**
     * 功能描述：处理消息。
     *
     * @param message
     */
    public void processTextMessage(TextMessage message);
    
    /**
     * 功能描述：处理消息。
     *
     * @param message   Map结构消息。
     */
    public void processMapMessage(MapMessage message);
}