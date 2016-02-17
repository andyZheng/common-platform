/*
 * 文件名：ConfigurationKey.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 全文检索(Solr)客户端程序
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.solr.client.config;


/**
 * 功能描述：<code>ConfigurationKey</code>提供Solr客户端配置项key容器。
 * <p>
 * 功能详细描述：本类管理Solr客户端配置项key全局配置，所声明的常量与<b>Solr_client_config.properties</b>配置文件中的key一一对应。
 *
 * @author   andy.zheng zhengwei09@founder.com.cn
 * @version  1.0, 2013-3-29 下午1:41:24
 * @since    SolrClient 1.0
 */
public final class ConfigurationKey {

    /** solr服务地址配置key */
    public final static String SOLR_BASE_URL = "app.solr.server.base.url";
    
    /** solr索引库列表配置key */
    public final static String SOLR_OTHERS_INDEX_STORE_LIST = "app.solr.server.others.store.list";
    
    /** 当前客户端请求的索引库配置key*/
    public final static String SOLR_CLIENT_REQUEST_STORE = "app.solr.client.request.store";
    
    /** 当前客户端任务处理周期配置key */
    public final static String SOLR_CLIENT_TASK_PEROID = "app.solr.client.task.handler.peroid";
    
    /** 当前客户端索引任务工作线程数配置key */
    public final static String SOLR_CLIENT_WORK_THREAD_AMOUNT = "app.solr.client.work.thread.count";
    
    /** 当前客户端索引数据分批临界值配置key */
    public final static String SOLR_CLIENT_INDEX_DATA_STEP = "app.solr.client.index.data.step";
    
    /** 索引资源请求配置key */
    public final static String INDEX_RES_REQUEST_URL = "index.res.request.url";
    
    /** 更新已索引资源状态请求配置key */
    public final static String UPDATE_INDEX_STATUS_REQUEST_URL = "update.index.status.request.url";
   
}
