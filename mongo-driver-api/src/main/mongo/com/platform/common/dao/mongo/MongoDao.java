/*
 * 文件名：MongoDao.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.dao.mongo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.bson.conversions.Bson;

import com.mongodb.client.model.IndexModel;
import com.platform.common.dao.CommonDao;


/**
 * 功能描述：<code>BaseMongoDao</code>是针对MongoDB DAO层基类接口定义。
 * <p>本类根据MongoDB的数据访问特性定义个性化数据访问接口。
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2015年6月10日 下午4:04:16
 * @since    Common-Platform/MongoDB DAO 1.0
 */
public interface MongoDao<E extends Bson, PK extends Serializable> extends CommonDao<E, PK>{

    public void mapReduce(final String mapFunction, final String reduceFunction);
    
    public void createIndex(Bson keys);
    
    public void createIndex(List<IndexModel> indexModels);
    
    public void dropIndex(Bson keys);
    
    public void save(List<E> entitys);
    
    public Boolean updateOneByConditions(Map<String, Object> updateProperties, Map<String, Object> updateConditions);
    
    public Boolean updateOneByConditions(Map<String, Object> updateProperties, String property, Object value);
}