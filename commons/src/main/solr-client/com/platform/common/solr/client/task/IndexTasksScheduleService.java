/*
 * 文件名：IndexTasksScheduleService.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 全文检索(Solr)客户端程序
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.solr.client.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.platform.common.solr.client.config.ConfigurationKey;
import com.platform.common.solr.client.config.ConfigurationReader;

/**
 * 功能描述：索引任务定时调度服务器
 * <p>
 * 功能详细描述：周期性的调度索引任务处理器
 *
 * @author   andy.zheng zhengwei09@founder.com.cn
 * @see      IndexTasksHandler
 * @version  1.0, 2013-3-29 上午10:39:08
 * @since    SolrClient 1.0
 */
public final class IndexTasksScheduleService {
    
    /** 调用周期单位 */
    private final static TimeUnit INVOKE_TIME_UNIT = TimeUnit.HOURS;
    
    /** 任务调度对象 */
    private static ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    
    /** 调用周期  默认为24小时 */
    private static long invoke_period = 24L;
    
    static{
        ConfigurationReader reader = ConfigurationReader.getInstance();
        int period = reader.getInt(ConfigurationKey.SOLR_CLIENT_TASK_PEROID);
        if(period > 0){
            invoke_period = period;
        }
    }
    
    /**
     * 构造器私有化
     */
    private IndexTasksScheduleService(){
        
    }
    
    /**
     * 执行调度服务
     * 
     * @param tasks  当前任务
     */
    public static void  execute(Runnable tasks){
        long initialDelayTime = 0L;
        scheduledExecutor.scheduleAtFixedRate(tasks, initialDelayTime, invoke_period, INVOKE_TIME_UNIT);
    }

}
