/*
 * 文件名：IndexTasksHandler.java
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
import java.util.List;

import com.platform.common.solr.client.config.ConfigurationKey;
import com.platform.common.solr.client.config.ConfigurationReader;

/**
 * 功能描述：<code>IndexTasksHandler</code>为待索引数据处理器。
 * <p>
 * 功能详细描述：<p>本类处理待索引数据步骤
 * <ul>
 *      <li>向需要提供全文索引系统发送查询待索引数据的请求；</li>
 *      <li>如果上一步返回的结果待索引数据量较大，细分为多个单任务；</li>
 *      <li>将上一步分配的任务注册到<code>IndexTasksService</code>；</li>
 * </ul>
 *
 * @author   andy.zheng zhengwei09@founder.com.cn
 * @see      IndexTasksScheduleService
 * @version  1.0, 2013-3-28 下午5:08:25
 * @since    SolrClient 1.0
 */
public class IndexTasksHandler implements Runnable {

    /** 配置文件读取器 */
    private static ConfigurationReader reader = ConfigurationReader.getInstance();
    
    /* 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        // 获取Solr服务器中自定义索引库列表
        String indexStore = reader.getString(ConfigurationKey.SOLR_OTHERS_INDEX_STORE_LIST);
        if(null != indexStore && !"".equals(indexStore)){
            String[] storeList = indexStore.split("\\,");
            // 请求待索引的数据
            final IndexTasksRequest taskRequest = new IndexTasksRequest();
            for (final String storeName : storeList) {
                IndexTasksService.execute(new Runnable() {
                    
                    @Override
                    public void run() {
                        List<Serializable> indexDatas = taskRequest.obtainIndexBeans(storeName);
                        if(null != indexDatas && !indexDatas.isEmpty()){
                            processBatchTasks(storeName, indexDatas);
                        }
                    }
                });
            }
        }
    }

    /**
     * 功能描述：将待索引bean数据建立分批处理任务
     *
     * @param storeName    索引库名称
     * @param indexDatas   当前待处理索引bean列表
     */
    private void processBatchTasks(String storeName, List<Serializable> indexDatas) {
        int size = indexDatas.size();
        
        // 索引数据分批临界值
        int step = reader.getInt(ConfigurationKey.SOLR_CLIENT_INDEX_DATA_STEP);
        
        // 计算索引任务数
        int taskAmount = size / step;
        if(0 != size % step){
            taskAmount += 1;
        }
        
        // 将待索引数据分配任务
        for (int i = 0; i < taskAmount; i++) {
            int start = i * step;
            int end = (i + 1) * step;
            if(size < end){
                end = size;
            }
            
            // 创建索引任务对象并注册到索引多任务服务中
            List<Serializable> currentTaskDataList = indexDatas.subList(start, end);
            IndexTask indexTask = new IndexTask(storeName);
            indexTask.setIndexBeanList(currentTaskDataList);
            IndexTasksService.registerTask(indexTask);
        }
        
        // 调度索引多任务服务
        IndexTasksService.executeAllTasks();
    }
}
