/*
 * 文件名：MessageExecutorService.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台v1.1
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.jms.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 功能描述：<code>StandardService</code>为任务服务类，基于JDK 5.0线程池提供多线程服务。<p>
 *
 * @author   Andy.zheng andy.zheng0807@gmail.com
 * @see      java.util.concurrent.ExecutorService#submit(Runnable)
 * @version  1.0, 2014年4月1日 下午5:50:27
 * @since    Common-Platform/Message Service 1.0
 */
public final class MessageExecutorService {
    
    /** 消息任务执行器实例 */
    private static MessageExecutorService messageExecutorService = new MessageExecutorService();

    /** 创建任务管理执行器对象 */
    private static ExecutorService service = Executors.newCachedThreadPool();
    
    /**
     * 功能描述：构造器私有化。
     *
     */
    private MessageExecutorService(){
        
    }
    
    /**
     * 功能描述：提交当前任务到执行器中。
     *
     * @param task  当前任务。
     * @return      返回当前任务执行结果。
     */
    public Future<?> submit(Runnable task){
        return service.submit(task);
    }
    
    /**
     * 功能描述：异步执行任务。
     *
     * @param task  当前任务。
     */
    public void execute(Runnable task){
       service.execute(task);
    }
    
    /**
     * 功能说明：获取当前服务对象。
     *
     * @return ExecutorService the service 返回值描述信息
     */
    public static ExecutorService getService() {
        return service;
    }

    /**
     * 功能说明：获取当前消息任务执行器实例。
     *
     * @return ExecutorService the service 消息任务执行器实例
     */
    public static MessageExecutorService getInstance() {
        return messageExecutorService;
    }
}