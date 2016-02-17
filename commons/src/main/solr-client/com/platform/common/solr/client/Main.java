/*
 * 文件名：Main.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 全文检索(Solr)客户端程序
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.solr.client;

import com.platform.common.solr.client.task.IndexTasksHandler;
import com.platform.common.solr.client.task.IndexTasksScheduleService;

/**
 * 功能描述：<code>Main</code>提供周期性调度索引任务程序。
 *
 * @author   andy.zheng zhengwei09@founder.com.cn
 * @see      com.apabi.solr.client.task.IndexTasksHandler
 * @see      com.apabi.solr.client.task.IndexTasksScheduleService
 * @version  1.0, 2013-4-2 上午9:21:17
 * @since    SolrClient 1.0
 */
public class Main {

    /**
     * 功能描述：Solr客户端程序入口
     *
     * @param args  控制台参数
     */
    public static void main(String[] args) {
        IndexTasksHandler handler = new IndexTasksHandler();
        IndexTasksScheduleService.execute(handler);
    }

}
