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

import java.io.Serializable;

import org.apache.solr.client.solrj.SolrServer;

/**
 * 功能描述：<code>SolrClientHolder</code>类为Solr客户端对象持有者。
 * <p>
 * 功能详细描述：缓存Solr客户端信息以及对象。
 *
 * @author   andy.zheng zhengwei09@founder.com.cn
 * @see      SolrClientFactory
 * @version  1.0, 2013-3-28 下午5:15:11
 * @since    SolrClient 1.0
 */
public class SolrClientHolder implements Serializable {

    /** 序列号 */
    private static final long serialVersionUID = -7030014020344263236L;
    
    /** Solr服务器地址 */
    private String solrUrl;
    
    /** solr客户端对象 */
    private  SolrServer solrClient;
    
    /**
     * 默认构造函数
     */
    public SolrClientHolder(){
        
    }
    
    /**
     * 重载构造函数
     * @param solrUrl         Solr服务器地址
     */
    public SolrClientHolder(String solrUrl){
        this.solrUrl = solrUrl;
    }
    
    /**
     * 重载构造函数
     * @param solrUrl         Solr服务器地址
     * @param solrClient      Solr客户端对象
     */
    public SolrClientHolder(String solrUrl , SolrServer solrClient){
        this.solrUrl = solrUrl;
        this.solrClient = solrClient;
    }
    
    /**
     * 获取Solr服务器地址
     * 
     * 
     * @return  Solr服务器地址
     */
    public String getSolrUrl(){
        return this.solrUrl;
    }
    
    /**
     * 设置Solr服务器地址
     * @param solrUrl Solr服务器地址
     */
    public void setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
    }

    /**
     * 设置Solr客户端对象
     * @param solrClient   设置Solr客户端对象
     */
    public void setSolrClient(SolrServer solrClient) {
        this.solrClient = solrClient;
    }

    /**
     * 获取Solr客户端对象
     * @return  Solr客户端对象
     */
    public SolrServer getSolrClient(){
        return this.solrClient;
    }
}
