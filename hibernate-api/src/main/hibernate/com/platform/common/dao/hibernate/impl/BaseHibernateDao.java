/*
 * 文件名：BaseHibernateDao.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved.
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.dao.hibernate.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.platform.common.dao.hibernate.HibernateDao;

/**
 * 功能描述：<code>BaseHibernateDao</code>是Hibernate实现类。 
 * <p>本类继承自{@link HibernateDao},实现了Hibernate常规数据访问接口。
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2014年6月20日 上午10:33:15
 * @since    Common-Platform/ 1.0
 */
public class BaseHibernateDao<E extends Serializable, PK extends Serializable> implements HibernateDao<E, PK> {

    /* 
     * @see com.platform.common.dao.CommonDao#queryAll()
     */
    @Override
    public List<E> queryAll() {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryAll(java.lang.String)
     */
    @Override
    public List<E> queryAll(String sortBy) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryAll(java.lang.Integer, java.lang.Integer, java.lang.String[])
     */
    @Override
    public List<E> queryAll(Integer offset, Integer pageSize, String... sortBy) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryByProperty(java.lang.String, java.lang.Object, java.lang.String[])
     */
    @Override
    public List<E> queryByProperty(String property, Object value, String... sortBy) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryByProperty(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Object, java.lang.String[])
     */
    @Override
    public List<E> queryByProperty(Integer offset, Integer pageSize, String property, Object value, String... sortBy) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryByConditions(java.util.Map, java.lang.String[])
     */
    @Override
    public List<E> queryByConditions(Map<String, Object> conditions, String... sortBy) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryByConditions(java.lang.Integer, java.lang.Integer, java.util.Map, java.lang.String[])
     */
    @Override
    public List<E> queryByConditions(Integer offset, Integer pageSize, Map<String, Object> conditions, String... sortBy) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryByUniqueProperty(java.lang.String, java.lang.Object)
     */
    @Override
    public E queryByUniqueProperty(String property, Object value) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryUniqueByConditions(java.util.Map)
     */
    @Override
    public E queryUniqueByConditions(Map<String, Object> conditions) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#getById(java.io.Serializable)
     */
    @Override
    public E getById(PK id) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#countAll()
     */
    @Override
    public Long countAll() {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#countByConditions(java.util.Map)
     */
    @Override
    public Long countByConditions(Map<String, Object> conditions) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#isExistByProperty(java.lang.String, java.lang.Object)
     */
    @Override
    public Boolean isExistByProperty(String property, Object value) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#isExistByConditions(java.util.Map)
     */
    @Override
    public Boolean isExistByConditions(Map<String, Object> conditions) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#save(java.lang.Object)
     */
    @Override
    public E save(E entity) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#update(java.lang.Object)
     */
    @Override
    public Boolean update(E entity) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#updateByConditions(java.util.Map, java.util.Map)
     */
    @Override
    public Boolean updateByConditions(Map<String, Object> updateProperties, Map<String, Object> updateConditions) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#updateByConditions(java.util.Map, java.lang.String, java.lang.String)
     */
    @Override
    public Boolean updateByConditions(Map<String, Object> updateProperties, String property, Object value) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#removeById(java.io.Serializable)
     */
    @Override
    public Boolean removeById(PK id) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#removeByProperty(java.lang.String, java.lang.String)
     */
    @Override
    public Boolean removeByProperty(String property, String value) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#removeByConditions(java.util.Map)
     */
    @Override
    public Boolean removeByConditions(Map<String, Object> conditions) {
        return null;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#remove(java.lang.Object)
     */
    @Override
    public Boolean remove(E entity) {
        return null;
    }

}
