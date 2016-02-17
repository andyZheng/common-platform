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
package com.platform.common.solr.client.search.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.common.solr.client.config.ConfigurationKey;
import com.platform.common.solr.client.config.ConfigurationReader;
import com.platform.common.solr.client.factory.SolrClientFactory;
import com.platform.common.solr.client.factory.SolrClientHolder;
import com.platform.common.solr.client.search.SearchService;

/**
 * 功能描述：<code>SearchServiceImpl</code>为Solr客户端搜索引擎服务实现类。<p>
 *
 * @author   andy.zheng zhengwei09
 * @version  1.0, 2013-3-28 下午5:17:12
 * @since    SolrClient 1.0
 */
public class SearchServiceImpl implements SearchService {
    
    /** 多少毫秒之内提交  */
    private static final int COMMIT_WITHIN_MS = 500;

    /** 日志对象 */
    private static Logger logger = LoggerFactory.getLogger(SearchService.class);
    
    /** Solr索引库名称 */
    private String storeName = ConfigurationReader.getInstance().getString(ConfigurationKey.SOLR_CLIENT_REQUEST_STORE);
    
     
    /* 
     * @see com.apabi.solr.client.search.SearchService#deleteById(java.util.List)
     */
    @Override
    public boolean deleteById(List<String> ids) {
        if(null == ids || ids.isEmpty()){
            logger.debug("The ids is empty!");
            throw new IllegalArgumentException("The ids is empty!");
        }
        
        boolean isSuccess = false;
        try {
            SolrServer solrClient = this.getSolrClient();
            solrClient.deleteById(ids, COMMIT_WITHIN_MS);
            isSuccess = true;
        } catch (SolrServerException e) {
            logger.error("Delete indexs by ids fail!", e);
        } catch (IOException e) {
            logger.error("Delete indexs by ids fail!", e);
        }
        
        
        return isSuccess;
    }

    /* 
     * @see com.apabi.solr.client.search.SearchService#deleteById(java.lang.String)
     */
    @Override
    public boolean deleteById(String id) {
        if(null == id || "".equals(id)){
            logger.debug("The id is empty!");
            throw new IllegalArgumentException("The id is empty!");
        }
        
        boolean isSuccess = false;
        try {
            SolrServer solrClient = this.getSolrClient();
            solrClient.deleteById(id, COMMIT_WITHIN_MS);
            isSuccess = true;
        } catch (SolrServerException e) {
            logger.error("Delete index by id fail!", e);
        } catch (IOException e) {
            logger.error("Delete indexs by id fail!", e);
        }
        
        
        return isSuccess;
    }

    /* 
     * @see com.apabi.solr.client.search.SearchService#deleteByQuery(java.lang.String)
     */
    @Override
    public boolean deleteByQuery(String queryString) {
        if(null == queryString || "".equals(queryString)){
            logger.debug("The queryString is empty!");
            throw new IllegalArgumentException("The queryString is empty!");
        }
        
        boolean isSuccess = false;
        try {
            SolrServer solrClient = this.getSolrClient();
            solrClient.deleteByQuery(queryString, COMMIT_WITHIN_MS);
            isSuccess = true;
        } catch (SolrServerException e) {
            logger.error("Delete indexs by queryString fail!", e);
        } catch (IOException e) {
            logger.error("Delete indexs by queryString fail!", e);
        }
        
        
        return isSuccess;
    }

    /* 
     * @see com.apabi.solr.client.search.SearchService#getSolrClient()
     */
    @Override
    public SolrServer getSolrClient() {
        SolrClientHolder solrClientHolder = SolrClientFactory.getSolrClientHolderByStoreName(storeName);
        if(null == solrClientHolder || null == solrClientHolder.getSolrClient() ){
            solrClientHolder = SolrClientFactory.getSolrClientHolderByStoreName(SolrClientFactory.DEFAULT_STORE_NAME);
        }
        
        return solrClientHolder.getSolrClient();
    }

    /* 
     * @see com.apabi.solr.client.search.SearchService#query(org.apache.solr.common.params.SolrParams)
     */
    @Override
    public QueryResponse query(SolrParams solrParams) {
        if(null == solrParams){
            logger.debug("The solr params is empty!");
            throw new IllegalArgumentException("The solr params is empty!");
        }
        
        QueryResponse  response = null;
        try {
            SolrServer solrClient = this.getSolrClient();
            response = solrClient.query(solrParams);
            logger.info("Query index info success.");
        } catch (SolrServerException e) {
            logger.error("Query index info fail!", e);
        }
        
        return response;
    }

    /* 
     * @see com.apabi.solr.client.search.SearchService#query(java.lang.String)
     */
    @Override
    public QueryResponse query(String queryString) {
        QueryResponse  response = null;
        if(null == queryString || "".equals(queryString)){
            logger.debug("The solr params is empty!");
            throw new IllegalArgumentException("The solr params is empty!");
        }
        
        SolrParams params = new SolrQuery(queryString);
        response = this.query(params);

        return response;
    }

    /* 
     * @see com.apabi.solr.client.search.SearchService#saveOrUpdate(java.util.List)
     */
    @Override
    public boolean saveOrUpdateBeans(List<Serializable> indexBeans) {
        boolean isSuccess = false;
        try {
            SolrServer solrServer = this.getSolrClient();
            solrServer.addBeans(indexBeans);
            solrServer.optimize();
            solrServer.commit();
            logger.info("Add or Update index beans request commit success! " + indexBeans);
            isSuccess = true;
        } catch (SolrServerException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        }
        
        return isSuccess;
    }

    /* 
     * @see com.apabi.solr.client.search.SearchService#saveOrUpdate(java.util.Map)
     */
    @Override
    public boolean saveOrUpdate(List<Map<String, Object>> indexDocuments) {
        boolean isSuccess = false;
        if(null == indexDocuments || indexDocuments.isEmpty()){
            logger.debug("The index document is empty!");
            throw new IllegalArgumentException("The index document is empty!");
        }
        
        try {
            SolrServer solrServer = this.getSolrClient();
            for (int i = 0; i < indexDocuments.size(); i++) {
                Map<String, Object> indexDocument = indexDocuments.get(i);
                SolrInputDocument solrInputDocument = new SolrInputDocument();
                for (Object obj : indexDocument.keySet()) {
                    String key = String.valueOf(obj);
                    // 建立solr文档
                    Object value = indexDocument.get(key);
                    solrInputDocument.addField(key, value);
                }
                solrServer.add(solrInputDocument);
            }
            
            solrServer.optimize();
            solrServer.commit();
            logger.info("Add or Update index request commit success! " + indexDocuments);
            isSuccess = true;
        } catch (SolrServerException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        }
        
        return isSuccess;
    }

    /* 
     * @see com.apabi.solr.client.search.SearchService#setStoreName(java.lang.String)
     */
    @Override
    public void setStoreName(String storeName) {
        if(null != storeName && !"".equals(storeName)){
            this.storeName = storeName;
        }
    }

}
