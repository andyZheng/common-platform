/*
 * 文件名：BaseJPADaoImpl.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved.
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.dao.jpa.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.common.dao.jpa.JPADao;

/**
 * 功能描述：<code>BaseJPADaoImpl</code>是JPA数据访问实现类。
 * <p>
 * 本类继承自{@link JPADao},实现了JPA常规数据访问接口。
 *
 * @author andy.zheng0807@gmail.com
 * @version 1.0, 2014年6月20日 上午10:14:31
 * @since Common-Platform/ 1.0
 */
@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
public abstract class BaseJPADaoImpl<E extends Serializable, PK extends Serializable> implements JPADao<E, PK> {

    /** 日志对象 */
    protected static Logger logger;

    /** 注入实体管理对象 */
    @PersistenceContext
    protected EntityManager entityManager;

    /** 当前访问实体类 */
    protected Class<E> entityClass;

    /** 当前实体默认查询从句 */
    protected String defaultQueryStatement;

    /** 当前实体默认统计从句 */
    protected String defaultCountStatement;
    
    /** 当前实体默认更新从句 */
    protected String defaultUpdateStatement;
    
    /** 当前实体默认删除从句 */
    protected String defaultDeleteStatement;

    public BaseJPADaoImpl() {
        logger = LoggerFactory.getLogger(this.getClass());

        // 动态获取当前数据访问的领域实体
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        this.entityClass = (Class<E>) actualTypeArguments[0];

        // 构造默认从句
        defaultQueryStatement = "SELECT e FROM " + this.entityClass.getSimpleName() + " e ";
        defaultCountStatement = "SELECT COUNT(1) FROM " + this.entityClass.getSimpleName() + " e ";
        defaultUpdateStatement = "UPDATE " + this.entityClass.getSimpleName() + " e SET %s ";
        defaultDeleteStatement = "DELETE FROM " + this.entityClass.getSimpleName() + " e  ";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.platform.common.dao.jpa.JPADao#getEntityManager()
     */
    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryAll()
     */
    @Override
    public List<E> queryAll() {
        TypedQuery<E> query = this.entityManager.createQuery(defaultQueryStatement, this.entityClass);
        return query.getResultList();
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryAll(java.lang.String)
     */
    @Override
    public List<E> queryAll(final String sortBy) {
        StringBuilder queryStringBuilder = new StringBuilder(this.defaultQueryStatement);
        if (StringUtils.isNotBlank(sortBy)) {
            queryStringBuilder.append(" ORDER BY ").append(sortBy);
        }

        TypedQuery<E> query = this.entityManager.createQuery(queryStringBuilder.toString(), this.entityClass);
        return query.getResultList();
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryAll(java.lang.Integer,
     * java.lang.Integer, java.lang.String[])
     */
    @Override
    public List<E> queryAll(final Integer offset, final Integer pageSize, final String... sortBy) {
        StringBuilder queryStringBuilder = new StringBuilder(this.defaultQueryStatement);
        if (sortBy.length == 1) {
            queryStringBuilder.append(" ORDER BY ").append(sortBy[0]);
        }

        TypedQuery<E> query = this.entityManager.createQuery(queryStringBuilder.toString(), this.entityClass);
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryByProperty(java.lang.String,
     * java.lang.Object, java.lang.String[])
     */
    @Override
    public List<E> queryByProperty(final String property, final Object value, final String... sortBy) {
        TypedQuery<E> query = this.buildConditionsQuery(this.defaultQueryStatement, new HashMap<String, Object>() {
            {
                this.put(property, value);
            }
        }, sortBy);

        return query.getResultList();
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryByProperty(java.lang.Integer,
     * java.lang.Integer, java.lang.String, java.lang.Object,
     * java.lang.String[])
     */
    @Override
    public List<E> queryByProperty(final Integer offset, final Integer pageSize, final String property,
            final Object value, String... sortBy) {
        TypedQuery<E> query = this.buildConditionsQuery(this.defaultQueryStatement, new HashMap<String, Object>() {
            {
                this.put(property, value);
            }
        }, sortBy);

        // 设置分页相关参数
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryByConditions(java.util.Map,
     * java.lang.String[])
     */
    @Override
    public List<E> queryByConditions(final Map<String, Object> conditions, final String... sortBy) {
        TypedQuery<E> query = this.buildConditionsQuery(this.defaultQueryStatement, conditions, sortBy);

        return query.getResultList();
    }

    /*
     * @see
     * com.platform.common.dao.CommonDao#queryByConditions(java.lang.Integer,
     * java.lang.Integer, java.util.Map, java.lang.String[])
     */
    @Override
    public List<E> queryByConditions(final Integer offset, final Integer pageSize,
            final Map<String, Object> conditions, final String... sortBy) {
        TypedQuery<E> query = this.buildConditionsQuery(this.defaultQueryStatement, conditions, sortBy);
        // 设置分页相关参数
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    /*
     * @see
     * com.platform.common.dao.CommonDao#queryByUniqueProperty(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public E queryByUniqueProperty(final String property, final Object value) {
        TypedQuery<E> query = this.buildConditionsQuery(this.defaultQueryStatement, new HashMap<String, Object>() {
            {
                this.put(property, value);
            }
        });

        return query.getSingleResult();
    }

    /*
     * @see
     * com.platform.common.dao.CommonDao#queryUniqueByConditions(java.util.Map)
     */
    @Override
    public E queryUniqueByConditions(final Map<String, Object> conditions) {
        TypedQuery<E> query = this.buildConditionsQuery(this.defaultQueryStatement, conditions);

        return query.getSingleResult();
    }

    /*
     * @see com.platform.common.dao.CommonDao#getById(java.io.Serializable)
     */
    @Override
    public E getById(PK id) {
        return this.entityManager.find(this.entityClass, id);
    }

    /*
     * @see com.platform.common.dao.CommonDao#countAll()
     */
    @Override
    public Long countAll() {
        return this.entityManager.createQuery(this.defaultCountStatement, Long.class).getSingleResult();
    }

    /*
     * @see com.platform.common.dao.CommonDao#countByConditions(java.util.Map)
     */
    @Override
    public Long countByConditions(final Map<String, Object> conditions) {
        TypedQuery<Long> query = this.buildConditionsQuery(this.defaultCountStatement, conditions, Long.class);
        return query.getSingleResult();
    }

    /*
     * @see
     * com.platform.common.dao.CommonDao#isExistByProperty(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public Boolean isExistByProperty(final String property, final Object value) {
        return this.countByConditions(new HashMap<String, Object>() {
            {
                this.put(property, value);
            }
        }) > 0 ? true : false;
    }

    /*
     * @see com.platform.common.dao.CommonDao#isExistByConditions(java.util.Map)
     */
    @Override
    public Boolean isExistByConditions(Map<String, Object> conditions) {
        return this.countByConditions(conditions) > 0 ? true : false;
    }

    /*
     * @see com.platform.common.dao.CommonDao#save(java.lang.Object)
     */
    @Override
    public E save(E entity) {
        this.entityManager.persist(entity);
        return entity;
    }

    /*
     * @see com.platform.common.dao.CommonDao#update(java.lang.Object)
     */
    @Override
    public Boolean update(E entity) {
        try {
            this.entityManager.merge(entity);
        } catch (Exception e) {
            logger.error("Update " + entity + " fail!");
            return false;
        }
        
        return true;
    }

    /*
     * @see com.platform.common.dao.CommonDao#updateByConditions(java.util.Map,
     * java.util.Map)
     */
    @Override
    public Boolean updateByConditions(final Map<String, Object> updateProperties, final Map<String, Object> updateConditions) {
        Query query = this.buildConditionsUpdate(this.defaultUpdateStatement, updateProperties, updateConditions);
        
        return query.executeUpdate() > 0 ? true : false;
    }

    /*
     * @see com.platform.common.dao.CommonDao#updateByConditions(java.util.Map,
     * java.lang.String, java.lang.String)
     */
    @Override
    public Boolean updateByConditions(final Map<String, Object> updateProperties, final String property, final Object value) {
        Query query = this.buildConditionsUpdate(this.defaultUpdateStatement, updateProperties, new HashMap<String, Object>(){
            {
                this.put(property, value);
            }
        });
        
        return query.executeUpdate() > 0 ? true : false;
    }

    /*
     * @see com.platform.common.dao.CommonDao#removeById(java.io.Serializable)
     */
    @Override
    public Boolean removeById(final PK id) {
        E entity = this.getById(id);
        
        return this.remove(entity);
    }

    /*
     * @see com.platform.common.dao.CommonDao#removeByProperty(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Boolean removeByProperty(final String property, final String value) {
        Query query = this.buildConditionsQuery(this.defaultDeleteStatement, new HashMap<String, Object>() {
            {
                this.put(property, value);
            }
        }, Integer.class);
        
        return query.executeUpdate() > 0 ? true : false;
    }

    /*
     * @see com.platform.common.dao.CommonDao#removeByConditions(java.util.Map)
     */
    @Override
    public Boolean removeByConditions(final Map<String, Object> conditions) {
        Query query = this.buildConditionsQuery(this.defaultDeleteStatement, conditions, Integer.class);
        return query.executeUpdate() > 0 ? true : false;
    }

    /*
     * @see com.platform.common.dao.CommonDao#remove(java.lang.Object)
     */
    @Override
    public Boolean remove(final E entity) {
        try {
            this.entityManager.remove(entity);
        } catch (Exception e) {
            logger.error("Delete " + entity + "fail");
            return false;
        }
        
        return true;
    }
    
    /**
     * 封装条件更新。
     * 
     * @param prefixUpdateString
     *            更新字符串前缀。
     * @param updateParams
     *            当前更新参数列表。
     * @param updateConditions
     *            当前更新条件列表。
     * @return 封装的更新对象。
     */
    private Query buildConditionsUpdate(String prefixUpdateString, Map<String, Object> updateParams, 
            Map<String, Object> updateConditions) {
        StringBuilder updateParamsBuilder = new StringBuilder(); 
        
        // 封装更新参数
        int index = 1;
        if (null != updateParams) {
            for (String name : updateParams.keySet()) {
                // 非首位参数后添加逗号分隔符
                if (index > 1) {
                    updateParamsBuilder.append(", ");
                }
                
                // 添加参数
                updateParamsBuilder.append(name);
                if (!name.matches(".*=\\s*$")) {
                    updateParamsBuilder.append(" = ");
                }
                updateParamsBuilder.append("?").append(index);
                
                index++;
            }
        }
        
        String updateString = String.format(prefixUpdateString, updateParamsBuilder.toString());
        
        // 封装更新条件
        TypedQuery<Integer> query = this.buildConditionsQuery(updateString, updateConditions, Integer.class);
        index = 1;
        for (Object value : updateParams.values()) {
            this.setQueryParameterByIndex(query, index, value);
            index ++;
        }

        return query;
    }

    /**
     * 封装条件查询。
     * 
     * @param prefixQueryString
     *            查询字符串前缀。
     * @param queryConditions
     *            当前查询条件列表。
     * @param sortBy
     *            排序字符串。eg price desc
     * @return 封装的查询对象。
     */
    private TypedQuery<E> buildConditionsQuery(String prefixQueryString, Map<String, Object> queryConditions,
            String... sortBy) {
        StringBuilder queryBuilder = new StringBuilder(prefixQueryString);
        
        // 封装查询参数信息
        if (null != queryConditions) {
            queryBuilder.append(" WHERE ");
            Set<String> paramSets = queryConditions.keySet();
            boolean preAdd = false;
            for (Iterator<String> iterator = paramSets.iterator(); iterator.hasNext();) {
                String param = (String) iterator.next();
                if (preAdd) {
                    queryBuilder.append(" AND ");
                } else {
                    preAdd = true;
                }
                queryBuilder.append(param);
                if (!param.matches(".*=\\s*$")) {
                    queryBuilder.append(" = ");
                }
                queryBuilder.append(":").append(param);
            }
        }

        // 封装排序信息
        if (sortBy.length == 1) {
            queryBuilder.append(" ORDER BY ").append(sortBy[0]);
        }

        TypedQuery<E> query = this.entityManager.createQuery(queryBuilder.toString(), this.entityClass);
        // 封装参数值
        if (null != queryConditions) {
            for (String param : queryConditions.keySet()) {
                Object value = queryConditions.get(param);
                this.setQueryParameterByName(query, param, value);
            }
        }

        return query;
    }

    /**
     * 封装条件查询。
     * 
     * @param prefixQueryString
     *            查询字符串前缀。
     * @param queryConditions
     *            当前查询条件列表。
     * @param resultClass
     *            查询结果类型。
     * @return 封装的查询对象。
     */
    private <T> TypedQuery<T> buildConditionsQuery(String prefixQueryString, Map<String, Object> queryConditions,
            Class<T> resultClass) {
        StringBuilder queryBuilder = new StringBuilder(prefixQueryString);

        // 封装查询参数
        if (null != queryConditions) {
            queryBuilder.append(" WHERE ");
            Set<String> paramSets = queryConditions.keySet();
            boolean preAdd = false;
            for (Iterator<String> iterator = paramSets.iterator(); iterator.hasNext();) {
                String param = (String) iterator.next();
                if (preAdd) {
                    queryBuilder.append(" AND ");
                } else {
                    preAdd = true;
                }
                queryBuilder.append(param);
                if (!param.matches(".*=\\s*$")) {
                    queryBuilder.append(" = ");
                }
                queryBuilder.append(":").append(param);
            }
        }

        TypedQuery<T> query = this.entityManager.createQuery(prefixQueryString, resultClass);
        // 封装参数值
        if (null != queryConditions) {
            for (String param : queryConditions.keySet()) {
                Object value = queryConditions.get(param);
                this.setQueryParameterByName(query, param, value);
            }
        }

        return query;
    }

    private void setQueryParameterByName(Query query, String name, Object value) {
        Class type = value.getClass();
        if (type == java.sql.Timestamp.class) {
            query.setParameter(name, (Timestamp) value, TemporalType.TIMESTAMP);
        } else if (type == java.sql.Date.class || type == java.util.Date.class) {
            query.setParameter(name, (java.util.Date) value, TemporalType.DATE);
        } else {
            query.setParameter(name, value);
        }
    }
    
    private void setQueryParameterByIndex(Query query, int index, Object value) {
        Class type = value.getClass();
        if (type == java.sql.Timestamp.class) {
            query.setParameter(index, (Timestamp) value, TemporalType.TIMESTAMP);
        } else if (type == java.sql.Date.class || type == java.util.Date.class) {
            query.setParameter(index, (java.util.Date) value, TemporalType.DATE);
        } else {
            query.setParameter(index, value);
        }
    }
}
