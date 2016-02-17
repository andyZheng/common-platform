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
package com.platform.common.solr.client.search;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;

/**
 * 功能描述：<code>SearchService</code>为Solr客户端搜索引擎服务接口。<p>
 * 功能详细描述：主要包括索引的创建、查询、更新、删除（CRUD）以及获取Solr客户端对象接口。
 *
 * @author   andy.zheng zhengwei09@founder.com.cn
 * @version  1.0, 2013-3-28 下午5:14:23
 * @since    SolrClient 1.0
 */
public interface SearchService {
    
    /**
     * 功能描述：根据索引ID删除索引数据。<p>
     * 功能详细描述：支持批量删除操作。
     *
     * @param ids   待删除索引id列表
     * @return      操作结果
     */
    public boolean deleteById(List<String> ids);
    
    /**
     * 功能描述：根据索引ID删除索引数据。<p>
     * 
     * @param id    待删除的索引ID
     * @return      操作结果
     */
    public boolean deleteById(String id);
    
    /**
     * 功能描述：根据查询语句删除索引。<p>
     *
     * @param queryString   待删除索引的查询语句
     * @return              操作结果
     */
    public boolean deleteByQuery(String queryString);
    
    /**
     * 功能描述：获取Solr客户端对象。<p>
     *
     * @return  Solr客户端对象  默认返回Solr服务器默认索引库客户端对象
     */
    public SolrServer getSolrClient();
    
    /**
     * 功能描述：查询索引数据。<p>
     * 功能详细描述：面向对象支持复杂查询。
     *
     * @param solrParams   索引查询参数
     * @return             索引查询结果 
     */
    public QueryResponse query(SolrParams solrParams);
    
    /**
     * 功能描述：查询索引数据。<p>
     * 功能详细描述：仅支持简单查询
     *
     * @param queryString   索引查询语句
     * @return              索引查询结果
     */
    public QueryResponse query(String queryString);
    
    /**
     * 功能描述：批量创建/更新索引bean。<p>
     * 功能详细描述：索引bean必须已基于annotation映射索引字段。
     *
     * @param indexBeans    待创建/更新的索引bean列表
     * @return              操作结果
     */
    public boolean saveOrUpdateBeans(List<Serializable> indexBeans);
    
    /**
     * 功能描述：批量创建/更新索引文档。<p>
     *
     * @param indexDocuments    待创建/更新的索引文档集合
     * @return                  操作结果
     */
    public boolean saveOrUpdate(List<Map<String, Object>> indexDocuments);
    
    /**
     * 功能描述：设置Solr索引库名称 。<p>
     * 功能详细描述：适用于动态切换Solr索引库。
     *
     * @param storeName     待设置的Solr索引库名称 
     */
    public void setStoreName(String storeName);
    
}
