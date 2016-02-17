/*
 * 文件名：IndexTasksRequest.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 全文检索(Solr)客户端程序
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.solr.client.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.noggit.ObjectBuilder;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.common.solr.client.config.ConfigurationKey;
import com.platform.common.solr.client.config.ConfigurationReader;
import com.platform.common.solr.client.search.SearchService;

/**
 * 功能描述：<code>IndexTasksRequest</code>为索引数据请求器。
 * <p>
 * 功能详细描述：请求器向需要提供全文索引系统发送查询待索引数据请求。
 *
 * @author   andy.zheng zhengwei09@founder.com.cn
 * @version  1.0, 2013-3-28 下午5:09:51
 * @since    SolrClient 1.0
 */
public class IndexTasksRequest {
    
    /** 配置读取器 */
    private static ConfigurationReader reader = ConfigurationReader.getInstance();
    
    /** 日志对象 */
    private static Logger logger = LoggerFactory.getLogger(SearchService.class);
    
    /** Http Client对象 */
    private HttpClient httpClient;
    
    
    /**
     * 功能描述：获取待索引数据
     * <p>
     * 功能详细描述：<ul>
     * <li>向第三方系统发送查询待索引资源请求；</li>
     * <li>解析响应结果并反序列化索引Bean。</li>
     * </ul>
     * 
     * @param  indexStoreName   索引库名称   
     * @return                  待索引Bean列表
     */
    @SuppressWarnings("unchecked")
    public List<Serializable> obtainIndexBeans(String indexStoreName){
        List<Serializable> indexBeans = null;
        // 获取待索引资源请求地址
        String requestURL = reader.getString(ConfigurationKey.INDEX_RES_REQUEST_URL);
        
        InputStream inputStream = null;
        ObjectInputStream ois = null;
        try {
            logger.info("The request index is starting...");
            HttpClient httpClient = this.createClient();
            // 组装post请求
            HttpPost post = new HttpPost(requestURL);
            boolean isRedirects = httpClient.getParams().getBooleanParameter("http.protocol.handle-redirects", false);
            post.getParams().setBooleanParameter("http.protocol.handle-redirects", isRedirects);
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("indexStoreName", indexStoreName));
            post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
            
            // 发送请求
            HttpResponse httpResponse = httpClient.execute(post);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            inputStream = httpResponse.getEntity().getContent();
            String message = "The request index data success!";
            switch(statusCode){
                case 301: 
                case 302: 
                      if(!isRedirects){
                          message = "Server at " + requestURL + " sent back a redirect (" + statusCode + ").";
                          throw new IllegalStateException(message);
                      }
                      break;
                case 200: 
                case 400: 
                case 409: 
                    break;
                default:
                    message = "Server at " + requestURL + "  appear (" + statusCode + ").";
                    throw new IllegalStateException(message);
            }
            
            ois = new ObjectInputStream(inputStream);
            indexBeans = (List<Serializable>)ois.readObject();
            logger.info(message + " Response index data:" + indexBeans);
        } catch (ClientProtocolException e) {
            logger.info("The request index data fail!", e);
        } catch (IOException e) {
            logger.info("The request index data fail!", e);
        } catch (ClassNotFoundException e) {
            logger.info("The request index data fail!", e);
        }finally{
            try {
                if(null != ois){
                    ois.close();
                }
                if(null != inputStream){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
        return indexBeans;

    }
    
    /**
     * 功能描述：获取待索引数据
     * <p>
     * 功能详细描述：<ul>
     * <li>向第三方系统发送查询待索引资源请求；</li>
     * <li>解析响应结果并返回Map结构。</li>
     * </ul>
     *
     * @param  indexStoreName   索引库名称   
     * @return                  待索引数据集合
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> obtainIndexMap(String indexStoreName){
        List<Map<String, Object>>  indexDataList = new ArrayList<Map<String, Object>>();
        try {
            indexDataList.add((Map<String, Object>)ObjectBuilder.fromJSON("{\"id\": \"001\", \"name\": \"andy\", \"content\": \"本人其实就是一个写程序的而已!@@@@@@@@@@@@@@@\"}"));
            indexDataList.add((Map<String, Object>)ObjectBuilder.fromJSON("{\"id\": \"002\", \"name\": \"大牛\", \"content\" : \"本人名字大牛是也，I Miss you~\"}"));
            indexDataList.add((Map<String, Object>)ObjectBuilder.fromJSON("{\"id\": \"003\", \"name\": \"大牛\", \"content\" : \"What are you name? My name is 大牛~\"}"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return indexDataList;

    }
    
    /**
     * 功能描述：更新索引Bean索引状态。
     *
     * @param indexBeans   待更新资源索引状态的Bean列表
     */
    public void updateBeansIndexedStatus(List<Serializable> indexBeans){
        if(null == indexBeans || indexBeans.isEmpty()){
            return;
        }
        
        try {
            // 获取更新资源索引状态请求地址
            String requestURL = reader.getString(ConfigurationKey.UPDATE_INDEX_STATUS_REQUEST_URL);
            
            // 组装post请求
            logger.info("Update index bean status is starting...");
            HttpClient httpClient = this.createClient();
            HttpPost post = new HttpPost(requestURL);
            boolean isRedirects = httpClient.getParams().getBooleanParameter("http.protocol.handle-redirects", false);
            post.getParams().setBooleanParameter("http.protocol.handle-redirects", isRedirects);
            // 封装请求参数
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Serializable bean : indexBeans) {
                params.add(new BasicNameValuePair("ids", bean.toString()));
            }
            post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
            
            // 发送请求
            httpClient.execute(post);
            logger.info("Update index bean status success!");
        } catch (ClientProtocolException e) {
            logger.info("Update index bean status fail!", e);
        } catch (IOException e) {
            logger.info("Update index bean status fail!", e);
        }
    }
    
    /**
     * 功能描述：更新已索引数据索引状态。
     *
     * @param indexDataList   待更新资源索引状态的索引数据列表
     */
    public void updateIndexedStatus(List<Map<String, Object>> indexDataList){
        if(null == indexDataList || indexDataList.isEmpty()){
            return;
        }
        
        try {
            // 获取更新资源索引状态请求地址
            String requestURL = reader.getString(ConfigurationKey.UPDATE_INDEX_STATUS_REQUEST_URL);
            
            // 组装post请求
            logger.info("Update index datas status is starting...");
            HttpClient httpClient = this.createClient();
            HttpPost post = new HttpPost(requestURL);
            boolean isRedirects = httpClient.getParams().getBooleanParameter("http.protocol.handle-redirects", false);
            post.getParams().setBooleanParameter("http.protocol.handle-redirects", isRedirects);
            // 封装请求参数
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map<String, Object> indexMap : indexDataList) {
                params.add(new BasicNameValuePair("ids", String.valueOf(indexMap.get("id"))));
            }
            post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
            
            // 发送请求
            httpClient.execute(post);
            logger.info("Update index data status success!");
        } catch (ClientProtocolException e) {
            logger.info("Update index data status fail!", e);
        } catch (IOException e) {
            logger.info("Update index data status fail!", e);
        }
    }
    
    
    
    
    /**
     * 功能描述：创建HttpClient对象
     *
     * @see     org.apache.solr.client.solrj.impl.HttpClientUtil#createClient(org.apache.solr.common.params.SolrParams)
     * @return  HttpClient对象
     */
    private synchronized HttpClient createClient(){
        if(null == httpClient){
            ModifiableSolrParams params = new ModifiableSolrParams();
            params.set("maxConnections", 128);
            params.set("maxConnectionsPerHost", 32);
            params.set("followRedirects", false);
            httpClient = HttpClientUtil.createClient(params);
        }
        
        return httpClient;
    }
}
