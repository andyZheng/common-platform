/*
 * 文件名：IndexTask.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 全文检索(Solr)客户端程序
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.solr.client.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.platform.common.solr.client.config.ConfigurationKey;
import com.platform.common.solr.client.config.ConfigurationReader;

/**
 * 功能描述：<code>SolrClientFactory</code>类为Solr客户端对象管理工厂。
 * <p>
 * 功能详细描述：<p>本类提供Solr客户端对象管理流程如下<ol>
 * <li>根据Solr服务端索引库列表注册Solr客户端信息（参见：<code>SolrClientHolder</code>）；</li>
 * <li>当<code>SearchService<code>对象需要建立Solr客户端对象时，将通过该工厂定位Solr客户端持有者，Solr客户端持有者创建Solr客户端对象
 *   并初始化相关信息缓存入持有者对象中。</li></ol><p>
 * 获取Solr客户端对象实例代码如下：<pre>
 * SolrClientHolder solrClientHolder = SolrClientFactory.getSolrClientHolderByStoreName(ConfigurationKey.SOLR_CLIENT_REQUEST_STORE);
 * SolrClient solrClient = solrClientHolder.getSolrClient();
 * </pre>
 * 
 * @author   andy.zheng zhengwei09@founder.com
 * @see      com.apabi.solr.client.search.SearchService
 * @version  1.0, 2013-3-28 下午5:12:58
 * @since    SolrClient 1.0
 */
public final class SolrClientFactory {

    /** 默认索引库名称 */
    public static final String DEFAULT_STORE_NAME = "default";
    
    /** 同步锁 */
    private static ReentrantLock lock = new ReentrantLock();
    
    /** Solr客户端持有者对象缓存器  */
    private static Map<String , SolrClientHolder> solrClientHolderCache;

    static{
        solrClientHolderCache = new ConcurrentHashMap<String , SolrClientHolder>();
        registerSolrClient();
    }
    
    /**
     * 根据索引库名定位Solr客户端对象持有者
     * 
     * 由于官方建议，故每个索引库对应的Solr客户端对象采用单例模式。官方说明详细信息如下：<p>
     *   
     * HttpSolrServer is thread-safe and if you are using the following constructor,
     * you *MUST* re-use the same instance for all requests.  If instances are created on
     * the fly, it can cause a connection leak. The recommended practice is to keep a
     * static instance of HttpSolrServer per solr server url and share it for all requests.
     * See https://issues.apache.org/jira/browse/SOLR-861 for more details
     * 
     * @param storeName     待定位的索引库名称
     * @return              Solr客户端对象持有者
     */
    public static SolrClientHolder getSolrClientHolderByStoreName(String storeName){
        if(!solrClientHolderCache.containsKey(storeName)){
            throw new IllegalArgumentException("Has not yet registered." + storeName);
        }
        
        SolrClientHolder solrClientHolder = solrClientHolderCache.get(storeName);
        if(null == solrClientHolder.getSolrClient()){
            lock.lock();
            try{
                if(null == solrClientHolder.getSolrClient()){
                    HttpSolrServer solrClient = new HttpSolrServer(solrClientHolder.getSolrUrl());
                    initSolrClient(solrClient);
                    solrClientHolder.setSolrClient(solrClient);
                }
            }finally{
                lock.unlock();
            }
        }

        return solrClientHolder;
    }
    
    /***
     * 功能描述：初始化Solr客户端参数
     *
     * @see   HttpSolrServer
     * @param solrClient    Solr客户端对象
     */
    private static void initSolrClient(HttpSolrServer solrClient){
        solrClient.setMaxRetries(1); // defaults to 0.  > 1 not recommended.
        solrClient.setConnectionTimeout(5000); // 5 seconds to establish TCP
        // Setting the XML response parser is only required for cross
        // version compatibility and only when one side is 1.4.1 or
        // earlier and the other side is 3.1 or later.
        //solrClient.setParser(new XMLResponseParser()); // binary parser is used by default
        // The following settings are provided here for completeness.
        // They will not normally be required, and should only be used 
        // after consulting javadocs to know whether they are truly required.
        solrClient.setSoTimeout(1000);  // socket read timeout
        solrClient.setMaxTotalConnections(512); // defaults to 128
        solrClient.setDefaultMaxConnectionsPerHost(64); // defaults to 32
        solrClient.setFollowRedirects(false);  // defaults to false
        // allowCompression defaults to false.
        // Server side must support gzip or deflate for this to have any effect.
        solrClient.setAllowCompression(true);
    }
    
    
    /**
     * 注册所有资源入库对象<p>
     * 
     * 初始化时，注册所有入库类型
     */
    private static void registerSolrClient(){
        ConfigurationReader reader = ConfigurationReader.getInstance();
        String solrUrl = reader.getString(ConfigurationKey.SOLR_BASE_URL);
        
        // 配置默认索引库客户端
        SolrClientHolder solrClientHolder = new SolrClientHolder(solrUrl);
        solrClientHolderCache.put(DEFAULT_STORE_NAME, solrClientHolder);
        
        // 配置其他索引库客户端
        String indexStore = reader.getString(ConfigurationKey.SOLR_OTHERS_INDEX_STORE_LIST);
        if(null != indexStore && !"".equals(indexStore)){
            String[] storeList = indexStore.split("\\,");
            for (String storeName : storeList) {
                SolrClientHolder otherSolrClientHolder = new SolrClientHolder(solrUrl + storeName);
                solrClientHolderCache.put(storeName, otherSolrClientHolder);
            }
        }

    }
    
    public static void main(String[] args){
        for (int i = 0; i < 10; i++) {
            new Thread(){
                public void run() {
                    SolrClientHolder solrClientHolder = SolrClientFactory.getSolrClientHolderByStoreName("core0");
                    System.out.println("core0 【当前线程" + Thread.currentThread().getName() + "】 " + solrClientHolder.getSolrClient());
                    
                    solrClientHolder = SolrClientFactory.getSolrClientHolderByStoreName("core1");
                    System.out.println("core1 【当前线程" + Thread.currentThread().getName() + "】 " + solrClientHolder.getSolrClient());
                };
            }.start();
        }
    }
}
