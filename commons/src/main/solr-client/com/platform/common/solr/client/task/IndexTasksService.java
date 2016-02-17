/*
 * 文件名：IndexTasksService.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 全文检索(Solr)客户端程序
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.solr.client.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.platform.common.solr.client.config.ConfigurationKey;
import com.platform.common.solr.client.config.ConfigurationReader;

/**
 * 功能描述：<code>IndexTasksService</code>为索引多任务管理服务类
 * <p>
 * 功能详细描述：<code>IndexTasksHandler</code>按照一定的规则将待索引的数据细分为多个任务，并注册到多任务管理服务中。最后通过本服务统一
 * 调度索引任务。
 *
 * @author   andy.zheng zhengwei09@founder.com.cn
 * @see      IndexTasksHandler
 * @version  1.0, 2013-3-28 下午5:06:57
 * @since    SolrClient 1.0
 */
public final class IndexTasksService {

    /** 索引服务对象 */
    private static ExecutorService service = Executors.newFixedThreadPool(3);
    
    /** 索引任务列表 */
    private static List<Runnable> taskLists = new ArrayList<Runnable>();
    
    static{
        ConfigurationReader reader = ConfigurationReader.getInstance();
        int workThreads = reader.getInt(ConfigurationKey.SOLR_CLIENT_WORK_THREAD_AMOUNT);
        if(workThreads > 0){
            service = Executors.newFixedThreadPool(workThreads);
        }
    }
    
    /**
     * 功能描述：构造器私有化
     *
     */
    private IndexTasksService(){
        
    }
    
    /**
     * 功能描述：注册索引任务<p>
     * 功能详细描述：支持注册索引单任务
     *
     * @see   Runnable#run()
     * @see   ExecutorService
     * @param indexTask      待注册是单索引任务
     */
    public static void registerTask(Runnable indexTask){
        taskLists.add(indexTask);
    }
    
    /**
     * 功能描述：注册索引任务
     * <p>
     * 功能详细描述：支持注册索引多任务
     *
     * @see   Runnable#run()
     * @see   ExecutorService
     * @param indexTasks     待注册的多索引任务
     */
    public static void registerTasks(List<Runnable> indexTasks){
        taskLists.addAll(indexTasks);
    }
    
    
    /**
     * 功能描述：执行单个任务
     *
     */
    public static void execute(Runnable task){
        service.execute(task);
    }
    
    /**
     * 功能描述：执行所有注册的任务
     *
     */
    public static void executeAllTasks(){
        for (Runnable task : taskLists) {
            service.execute(task);
        }
    }
}
