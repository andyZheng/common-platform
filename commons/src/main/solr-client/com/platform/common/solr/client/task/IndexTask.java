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
package com.platform.common.solr.client.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.platform.common.solr.client.search.SearchService;
import com.platform.common.solr.client.search.impl.SearchServiceImpl;

/**
 * 功能描述：<code>IndexTask</code>提供待索引数据单任务处理。
 * <p>
 * 功能详细描述：<code>IndexTasksService</code>将调度每一个注册的索引单任务，每个单任务处理所分配的索引数据集。
 *
 * @author   andy.zheng zhengwei09@founder.com.cn
 * @see      IndexTasksService
 * @version  1.0, 2013-3-28 下午5:04:51
 * @since    SolrClient 1.0
 */
public class IndexTask implements Runnable {

    /** 索引库名称 */
    private String indexStoreName;
    
    /** 当前任务待索引数据  */
    private List<Map<String, Object>> indexDataList = new ArrayList<Map<String, Object>>();
    
    /** 当前任务索引Bean列表 */
    private List<Serializable> indexBeanList = new ArrayList<Serializable>();
    
    /**
     * 功能描述：缺省构造函数
     *
     */
    public IndexTask() {
        
    }
    
    /**
     * 功能描述：重载构造函数
     * 
     * @param indexStoreName  索引库名称
     *
     */
    public IndexTask(String indexStoreName) {
        this.indexStoreName = indexStoreName;
    }
    
    /**
     * 功能说明：获取待提交的索引数据集
     *
     * @return List<Map<String,Object>> the indexDataList 返回待提交的索引数据集
     */
    public List<Map<String, Object>> getIndexDataList() {
        return indexDataList;
    }

    /**
     * 功能说明：设置待提交的索引数据集
     *
     * @param indexDataList 待提交的索引数据集
     */
    public void setIndexDataList(List<Map<String, Object>> indexDataList) {
        this.indexDataList = indexDataList;
    }

    /**
     * 功能说明：获取待提交的索引Bean数据集
     *
     * @return List<Serializable> the indexBeanList 返回待提交的索引Bean数据集
     */
    public List<Serializable> getIndexBeanList() {
        return indexBeanList;
    }

    /**
     * 功能说明：设置待提交的索引Bean数据集
     *
     * @param indexBeanList 待提交的索引Bean数据集
     */
    public void setIndexBeanList(List<Serializable> indexBeanList) {
        this.indexBeanList = indexBeanList;
    }

    /* 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        SearchService searchService = new SearchServiceImpl();
        searchService.setStoreName(this.indexStoreName);
        
        // 索引MAP数据结构
        if(null != indexDataList && !indexDataList.isEmpty()){
            // 调用搜索引擎接口
            boolean isSuccess = searchService.saveOrUpdate(indexDataList);
            if(isSuccess){
                // 更新已索引资源状态
                IndexTasksRequest taskRequest = new IndexTasksRequest();
                taskRequest.updateIndexedStatus(indexDataList);
            }
        }
        
        // 索引bean数据结构
        if(null != indexBeanList && !indexBeanList.isEmpty()){
            // 调用搜索引擎接口
            boolean isSuccess = searchService.saveOrUpdateBeans(indexBeanList);
            if(isSuccess){
                // 更新已索引资源状态
                IndexTasksRequest taskRequest = new IndexTasksRequest();
                taskRequest.updateBeansIndexedStatus(indexBeanList);
            }
        }
    }
}
