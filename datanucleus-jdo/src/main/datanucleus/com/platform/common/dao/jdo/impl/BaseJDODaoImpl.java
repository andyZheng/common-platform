/*
 * 文件名：BaseJdoDaoImpl.java
 * 版权：Copyright 2012-2015 SOHO studio. All Rights Reserved.
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.dao.jdo.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.common.dao.jdo.JDODao;

/**
 * 功能描述：<code>BaseJdoDaoImpl</code>是JDO数据访问实现类。
 * <p>
 * 本类继承自{@link JDODao},实现了JDO常规数据访问接口。
 *
 * @author andy.zheng0807@gmail.com
 * @version 1.0, 2015年6月19日 上午10:14:31
 * @since Common-Platform/JDO DAO 1.0
 */
@SuppressWarnings({ "unchecked", "serial" })
public abstract class BaseJDODaoImpl<E extends Serializable, PK extends Serializable> implements JDODao<E, PK> {

    /** 日志对象 */
    protected static Logger logger;

    /** 更新参数前缀 */
    private static final String PREFIX_FOR_UPDATE = "set_";

    /** 当前实体缺省JDOQL查询从句 */
    protected String defaultQueryStatement;

    /** 当前实体缺省JDOQL统计从句 */
    protected String defaultCountStatement;

    /** 当前实体默认更新从句 */
    protected String defaultUpdateStatement;

    /** 当前实体默认删除从句 */
    protected String defaultDeleteStatement;

    /** 由Spring容器注入的持久化管理工程对象 */
    @Autowired
    private PersistenceManagerFactory persistenceManagerFactory;

    /** 当前实体类 */
    private Class<E> entityClass;

    public BaseJDODaoImpl() {
        logger = LoggerFactory.getLogger(this.getClass());

        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        this.entityClass = (Class<E>) actualTypeArguments[0];

        defaultQueryStatement = "SELECT FROM " + this.entityClass.getName() + " e ";
        defaultCountStatement = "SELECT COUNT(this) FROM " + this.entityClass.getName() + " e ";
        defaultUpdateStatement = "UPDATE " + this.entityClass.getName() + " e SET %s ";
        defaultDeleteStatement = "DELETE FROM " + this.entityClass.getName() + " e ";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.platform.common.jdo.dao.JDODao#getPersistenceManager()
     */
    @Override
    public PersistenceManager getPersistenceManager() {
        return persistenceManagerFactory.getPersistenceManager();
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryAll()
     */

    public List<E> queryAll() {
        Query query = this.getPersistenceManager().newQuery(this.entityClass);

        return (List<E>) query.execute();
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryAll(java.lang.String)
     */
    public List<E> queryAll(final String sortBy) {
        Query query = this.getPersistenceManager().newQuery();

        return (List<E>) query.execute();
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryAll(java.lang.Integer,
     * java.lang.Integer, java.lang.String[])
     */
    public List<E> queryAll(final Integer offset, final Integer pageSize, final String... sortBy) {
        Query query = this.getPersistenceManager().newQuery(this.entityClass);
        if (sortBy.length == 1) {
            query.setOrdering(sortBy[0]);
        }
        query.setRange(offset, pageSize + pageSize);

        return (List<E>) query.execute();
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryByProperty(java.lang.String,
     * java.lang.Object, java.lang.String[])
     */
    public List<E> queryByProperty(final String property, final Object value, final String... sortBy) {
        Query query = this.getPersistenceManager().newQuery(this.entityClass);
        query.setFilter(property + " == :value");
        if (sortBy.length == 1) {
            query.setOrdering(sortBy[0]);
        }

        return (List<E>) query.execute(value);
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryByProperty(java.lang.Integer,
     * java.lang.Integer, java.lang.String, java.lang.Object,
     * java.lang.String[])
     */
    public List<E> queryByProperty(final Integer offset, final Integer pageSize, final String property,
            final Object value, final String... sortBy) {
        Query query = this.getPersistenceManager().newQuery(this.entityClass);
        query.setFilter(property + " == :value");
        if (sortBy.length == 1) {
            query.setOrdering(sortBy[0]);
        }
        query.setRange(offset, pageSize + pageSize);

        return (List<E>) query.execute(value);
    }

    /*
     * @see com.platform.common.dao.CommonDao#queryByConditions(java.util.Map,
     * java.lang.String[])
     */
    public List<E> queryByConditions(final Map<String, Object> conditions, final String... sortBy) {
        Query query = this.getPersistenceManager().newQuery(this.entityClass);
        this.buildFilters(query, conditions);
        if (sortBy.length == 1) {
            query.setOrdering(sortBy[0]);
        }

        // 获取参数值列表
        Collection<Object> values = conditions.values();
        Object[] parameters = new Object[values.size()];
        values.toArray(parameters);

        return (List<E>) query.executeWithArray(parameters);
    }

    /*
     * @see
     * com.platform.common.dao.CommonDao#queryByConditions(java.lang.Integer,
     * java.lang.Integer, java.util.Map, java.lang.String[])
     */
    public List<E> queryByConditions(final Integer offset, final Integer pageSize,
            final Map<String, Object> conditions, final String... sortBy) {
        Query query = this.getPersistenceManager().newQuery(this.entityClass);
        this.buildFilters(query, conditions);
        if (sortBy.length == 1) {
            query.setOrdering(sortBy[0]);
        }
        query.setRange(offset, pageSize + pageSize);

        // 获取参数值列表
        Collection<Object> values = conditions.values();
        Object[] parameters = new Object[values.size()];
        values.toArray(parameters);

        return (List<E>) query.executeWithArray(parameters);
    }

    /*
     * @see
     * com.platform.common.dao.CommonDao#queryByUniqueProperty(java.lang.String,
     * java.lang.Object)
     */
    public E queryByUniqueProperty(final String property, final Object value) {
        Query query = this.getPersistenceManager().newQuery(this.entityClass);
        query.setFilter(property + " == :value");
        query.setUnique(true);

        return (E) query.execute(value);
    }

    /*
     * @see
     * com.platform.common.dao.CommonDao#queryUniqueByConditions(java.util.Map)
     */
    public E queryUniqueByConditions(final Map<String, Object> conditions) {
        Query query = this.getPersistenceManager().newQuery(this.entityClass);
        this.buildFilters(query, conditions);
        query.setUnique(true);

        // 获取参数值列表
        Collection<Object> values = conditions.values();
        Object[] parameters = new Object[values.size()];
        values.toArray(parameters);

        return (E) query.executeWithArray(parameters);
    }

    /*
     * @see com.platform.common.dao.CommonDao#getById(java.io.Serializable)
     */
    public E getById(final PK id) {
        return this.getPersistenceManager().getObjectById(this.entityClass, id);
    }

    /*
     * @see com.platform.common.dao.CommonDao#countAll()
     */
    public Long countAll() {
        Query query = this.getPersistenceManager().newQuery(this.entityClass);
        query.setResult("COUNT(this)");
        query.setResultClass(Long.class);

        return (Long) query.execute();
    }

    /*
     * @see com.platform.common.dao.CommonDao#countByConditions(java.util.Map)
     */
    public Long countByConditions(final Map<String, Object> conditions) {
        Query query = this.getPersistenceManager().newQuery(this.entityClass);
        this.buildFilters(query, conditions);
        query.setResult("COUNT(this)");
        query.setResultClass(Long.class);

        // 获取参数值列表
        Collection<Object> values = conditions.values();
        Object[] parameters = new Object[values.size()];
        values.toArray(parameters);

        return (Long) query.executeWithArray(parameters);
    }

    /*
     * @see
     * com.platform.common.dao.CommonDao#isExistByProperty(java.lang.String,
     * java.lang.Object)
     */
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
    public Boolean isExistByConditions(final Map<String, Object> conditions) {
        return this.countByConditions(conditions) > 0 ? true : false;
    }

    /*
     * @see com.platform.common.dao.CommonDao#save(java.io.Serializable)
     */
    public E save(final E entity) {
        E newObj = this.getPersistenceManager().makePersistent(entity);
        return newObj;
    }

    /*
     * @see com.platform.common.dao.CommonDao#update(java.io.Serializable)
     */
    public Boolean update(final E entity) {
        this.getPersistenceManager().makePersistent(entity);
        return JDOHelper.isDirty(entity);
    }

    /*
     * @see com.platform.common.dao.CommonDao#updateByConditions(java.util.Map,
     * java.util.Map)
     */
    public Boolean updateByConditions(final Map<String, Object> updateProperties, final Map<String, Object> updateConditions) {
        String updateString = this.buildUpdateParameters(this.defaultUpdateStatement, updateProperties);
        Query query = this.getPersistenceManager().newQuery(updateString);
        this.buildFilters(query, updateConditions);

        // 获取参数值列表
        Object[] parameters = this.buildUpdateParameters(updateProperties, updateConditions);
        return (Long) query.executeWithArray(parameters) > 0 ? true : false;
    }

    /*
     * @see com.platform.common.dao.CommonDao#updateByConditions(java.util.Map,
     * java.lang.String, java.lang.String)
     */
    public Boolean updateByConditions(final Map<String, Object> updateProperties, final String property, final Object value) {
        String updateString = this.buildUpdateParameters(this.defaultUpdateStatement, updateProperties);
        Query query = this.getPersistenceManager().newQuery(updateString);
        Map<String, Object> updateConditions = new HashMap<>();
        updateConditions.put(property, value);
        this.buildFilters(query, updateConditions);

        // 获取参数值列表
        Object[] parameters = this.buildUpdateParameters(updateProperties, updateConditions);
        return (Long) query.executeWithArray(parameters) > 0 ? true : false;
    }

    /*
     * @see com.platform.common.dao.CommonDao#removeById(java.io.Serializable)
     */
    public Boolean removeById(final PK id) {
        E persistenceObj = this.getById(id);
        this.getPersistenceManager().deletePersistent(persistenceObj);
        return JDOHelper.isDeleted(persistenceObj);
    }

    /*
     * @see com.platform.common.dao.CommonDao#removeByProperty(java.lang.String,
     * java.lang.String)
     */
    public Boolean removeByProperty(final String property, final String value) {
        Query query = this.getPersistenceManager().newQuery(this.defaultDeleteStatement);
        this.buildFilters(query, new HashMap<String, Object>() {
            {
                this.put(property, value);
            }
        });

        return (Long) query.execute(value) > 0 ? true : false;
    }

    /*
     * @see com.platform.common.dao.CommonDao#removeByConditions(java.util.Map)
     */
    public Boolean removeByConditions(final Map<String, Object> conditions) {
        Query query = this.getPersistenceManager().newQuery(this.defaultDeleteStatement);
        this.buildFilters(query, conditions);

        // 获取参数值列表
        Collection<Object> values = conditions.values();
        Object[] parameters = new Object[values.size()];
        values.toArray(parameters);

        return (Long) query.executeWithArray(parameters) > 0 ? true : false;
    }

    /*
     * @see com.platform.common.dao.CommonDao#remove(java.io.Serializable)
     */
    public Boolean remove(final E entity) {
        this.getPersistenceManager().deletePersistent(entity);
        return JDOHelper.isDeleted(entity);
    }

    @Override
    public List<?> nativeSQLQuery(final String sql, final Object... parameters) {
        Query query = this.getPersistenceManager().newQuery(Query.SQL, sql);

        return (List<?>) query.executeWithArray(parameters);
    }

    private void buildFilters(Query query, Map<String, Object> conditions) {
        if (null == conditions) {
            conditions = new HashMap<String, Object>();
        }

        StringBuilder filterBuilder = new StringBuilder();
        Set<String> keySet = conditions.keySet();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
            String param = (String) iterator.next();
            if (filterBuilder.length() > 0) {
                filterBuilder.append(" AND ");
            }
            filterBuilder.append(param).append(" == ").append(":").append(param);
        }
        query.setFilter(filterBuilder.toString());
    }

    private String buildUpdateParameters(String prefixUpdateString, Map<String, Object> updateParams) {
        // 封装更新参数
        StringBuilder updateParamsBuilder = new StringBuilder();
        int index = 1;
        for (String name : updateParams.keySet()) {
            // 非首位参数后添加逗号分隔符
            if (index > 1) {
                updateParamsBuilder.append(", ");
            }

            // 添加参数
            Object value = updateParams.get(name);
            updateParamsBuilder.append(name).append(" == ");
            updateParamsBuilder.append(":").append(PREFIX_FOR_UPDATE).append(value);

            index++;
        }

        String updateString = String.format(prefixUpdateString, updateParamsBuilder.toString());
        return updateString;
    }

    private Object[] buildUpdateParameters(Map<String, Object> updateProperties, Map<String, Object> updateConditions) {
        Collection<Object> properties = updateProperties.values();
        Collection<Object> values = updateConditions.values();

        Object[] parameters = new Object[properties.size() + values.size()];
        properties.toArray(parameters);

        Object[] conditions = new Object[values.size()];
        values.toArray(conditions);

        System.arraycopy(conditions, 0, parameters, parameters.length, conditions.length);

        return parameters;
    }
}
