/*
 * 文件名：BaseIbatisDao.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved.
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.dao.ibatis.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.platform.common.config.Constants;
import com.platform.common.dao.ibatis.IbatisDao;

/**
 * 功能描述：<code>BaseIbatisDao</code>是Ibatis数据访问实现类。
 * <p>本类继承自{@link IbatisDao},实现了Ibatis常规数据访问接口。
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2014年6月20日 上午10:14:31
 * @since    Common-Platform/ 1.0
 */
@SuppressWarnings("unchecked")
public class BaseIbatisDao<E extends Serializable, PK extends Serializable> extends SqlMapClientDaoSupport implements IbatisDao<E, PK> {

    /** 日志对象 */
    protected static Logger logger;
    
    /** 当前实体类 */
    private Class<E> entityClass;

    public BaseIbatisDao(){
        logger = LoggerFactory.getLogger(this.getClass());
        this.entityClass = (Class<E>) ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    /* 
     * @see com.platform.common.dao.CommonDao#queryAll()
     */

    @Override
    public List<E> queryAll() {
        return this.getSqlMapClientTemplate().queryForList(this.getQuerySelect());
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryAll(java.lang.String)
     */
    @Override
    public List<E> queryAll(final String sortBy) {
        return this.getSqlMapClientTemplate().queryForList(this.getQuerySelect(), new HashMap<String, String>(){
            /**
             * 
             */
            private static final long serialVersionUID = 7950634558354180395L;

            {
                this.put("sortColumns", sortBy);
            }
        });
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryAll(java.lang.Integer, java.lang.Integer, java.lang.String[])
     */
    @Override
    public List<E> queryAll(final Integer offset, final Integer pageSize, final String... sortBy) {
        Map<String, Object> params = null;
        
        if(null != sortBy && sortBy.length == 1){
            params = new HashMap<String, Object>();
            params.put("sortColumns", sortBy[0]);
        }
        
        return this.getSqlMapClientTemplate().queryForList(this.getQuerySelect(), params , offset, pageSize);
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryByProperty(java.lang.String, java.lang.Object, java.lang.String[])
     */
    @Override
    public List<E> queryByProperty(final String property,final  Object value, final String... sortBy) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(property, value);
        
        if(null != sortBy && sortBy.length == 1){
            params.put("sortColumns", sortBy[0]);
        }
        
        return this.getSqlMapClientTemplate().queryForList(this.getQuerySelect(), params);
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryByProperty(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Object, java.lang.String[])
     */
    @Override
    public List<E> queryByProperty(Integer offset, Integer pageSize, String property, Object value, String... sortBy) {
        Map<String, Object> params = new HashMap<String, Object>();
        
        if(null != sortBy && sortBy.length == 1){
            params.put("sortColumns", sortBy[0]);
        }
        
        return this.getSqlMapClientTemplate().queryForList(this.getQuerySelect(), params , offset, pageSize);
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryByConditions(java.util.Map, java.lang.String[])
     */
    @Override
    public List<E> queryByConditions(Map<String, Object> conditions, String... sortBy) {
        if(null == conditions){
            return null;
        }
        
        if(null != sortBy && sortBy.length == 1){
            conditions.put("sortColumns", sortBy[0]);
        }
        
        return this.getSqlMapClientTemplate().queryForList(this.getQuerySelect(), conditions);
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryByConditions(java.lang.Integer, java.lang.Integer, java.util.Map, java.lang.String[])
     */
    @Override
    public List<E> queryByConditions(Integer offset, Integer pageSize, Map<String, Object> conditions, String... sortBy) {
        if(null == conditions){
            return null;
        }
        
        if(null != sortBy && sortBy.length == 1){
            conditions.put("sortColumns", sortBy[0]);
        }
        
        return this.getSqlMapClientTemplate().queryForList(this.getQuerySelect(), conditions, offset, pageSize);
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryByUniqueProperty(java.lang.String, java.lang.Object)
     */
    @Override
    public E queryByUniqueProperty(final String property, final Object value) {
        return (E) this.getSqlMapClientTemplate().queryForObject(this.getCountQuery(), new HashMap<String, Object>(){
            /**
             * 
             */
            private static final long serialVersionUID = -157960817217221880L;

            {
                this.put(property, value);
            }
        });
    }

    /* 
     * @see com.platform.common.dao.CommonDao#queryUniqueByConditions(java.util.Map)
     */
    @Override
    public E queryUniqueByConditions(Map<String, Object> conditions) {
        if(null == conditions){
            return null;
        }
        
        return (E) this.getSqlMapClientTemplate().queryForObject(this.getQuerySelect(), conditions);
    }

    /* 
     * @see com.platform.common.dao.CommonDao#getById(java.io.Serializable)
     */
    @Override
    public E getById(PK id) {
        return (E) this.getSqlMapClientTemplate().queryForObject(this.getPrimaryKeyQuery(), id);
    }

    /* 
     * @see com.platform.common.dao.CommonDao#countAll()
     */
    @Override
    public Long countAll() {
        return (Long) this.getSqlMapClientTemplate().queryForObject(this.getCountQuery());
    }

    /* 
     * @see com.platform.common.dao.CommonDao#countByConditions(java.util.Map)
     */
    @Override
    public Long countByConditions(Map<String, Object> conditions) {
        if(null == conditions){
            return null;
        }
        
        Object obj = this.getSqlMapClientTemplate().queryForObject(this.getCountQuery(), conditions);
        return (Long)obj;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#isExistByProperty(java.lang.String, java.lang.Object)
     */
    @Override
    public Boolean isExistByProperty(String property, Object value) {
        return !this.queryByProperty(property, value).isEmpty();
    }

    /* 
     * @see com.platform.common.dao.CommonDao#isExistByConditions(java.util.Map)
     */
    @Override
    public Boolean isExistByConditions(Map<String, Object> conditions) {
        if(null == conditions) {
            return false;
        }
        
        return this.queryByConditions(conditions).isEmpty();
    }

    /* 
     * @see com.platform.common.dao.CommonDao#save(java.lang.Object)
     */
    @Override
    public E save(E entity) {
        return (E) this.getSqlMapClientTemplate().insert(this.getInsertQuery(), entity);
    }
    
    /* 
     * @see com.platform.common.dao.ibatis.IbatisDao#batchSave(java.util.List)
     */
    public void batchSave(final List<E> entitys) throws SQLException {
        if(null == entitys || entitys.isEmpty()){
            return;
        }
        
        String insertQuery = this.getInsertQuery();
        this.batchSave(insertQuery, entitys);
    }

    /* 
     * @see com.platform.common.dao.CommonDao#update(java.lang.Object)
     */
    @Override
    public Boolean update(E entity) {
        return this.getSqlMapClientTemplate().update(this.getUpdateQuery(), entity) > 0 ? true : false;
    }
    
    
    /* 
     * @see com.platform.common.dao.ibatis.IbatisDao#batchUpdate(java.util.List)
     */
    public void batchUpdate(final List<E> entitys) throws SQLException {
        if(null == entitys || entitys.isEmpty()){
            return;
        }
        
        
        String updateQuery = this.getUpdateQuery();
        this.batchUpdate(updateQuery, entitys);
    }

    /* 
     * @see com.platform.common.dao.CommonDao#updateByConditions(java.util.Map, java.util.Map)
     */
    @Override
    public Boolean updateByConditions(Map<String, Object> updateProperties, Map<String, Object> updateConditions) {
        throw new UnsupportedOperationException("The method has not implemented!");
    }

    /* 
     * @see com.platform.common.dao.CommonDao#updateByConditions(java.util.Map, java.lang.String, java.lang.Object)
     */
    @Override
    public Boolean updateByConditions(Map<String, Object> updateProperties, String property, Object value) {
        throw new UnsupportedOperationException("The method has not implemented!");
    }

    /* 
     * @see com.platform.common.dao.CommonDao#removeById(java.io.Serializable)
     */
    @Override
    public Boolean removeById(final PK id) {
        return this.getSqlMapClientTemplate().delete(this.getDeleteQuery(), new HashMap<String, Object>(){
            /**
             * 
             */
            private static final long serialVersionUID = -7028151122881293599L;

            {
                this.put("id", id);
            }
        }) > 0 ? true : false;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#removeByProperty(java.lang.String, java.lang.String)
     */
    @Override
    public Boolean removeByProperty(final String property, final String value) {
        return this.getSqlMapClientTemplate().delete(this.getDeleteQuery(), new HashMap<String, Object>(){
            /**
             * 
             */
            private static final long serialVersionUID = -501856465368566433L;

            {
                this.put(property, value);
            }
        }) > 0 ? true : false;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#removeByConditions(java.util.Map)
     */
    @Override
    public Boolean removeByConditions(Map<String, Object> conditions) {
        if(null == conditions){
            return null;
        }
        
        return this.getSqlMapClientTemplate().delete(this.getDeleteQuery(), conditions) > 0 ? true : false;
    }

    /* 
     * @see com.platform.common.dao.CommonDao#remove(java.lang.Object)
     */
    @Override
    public Boolean remove(E entity) {
        throw new UnsupportedOperationException("The method has not implemented!");
    }
    
    /**
     * 功能描述：获取当前实体类型。
     *
     * @return  当前实体类型。
     */
    protected Class<?> getEntityClass(){
        return this.entityClass;
    }
    
    /**
     * 功能描述：获取主键查询语句名称。
     *
     * @return  主键查询语句名称。
     */
    protected String getPrimaryKeyQuery() {
        return getEntityClass().getSimpleName() + ".getById";
    }
    
    /**
     * 功能描述：获取插入功能查询语句名称。
     *
     * @param clazz 待插入的数据类型。
     * @return      插入语句名称。
     */
    protected String getInsertQuery() {
        return getInsertQuery(getEntityClass());
    }

    /**
     * 功能描述：获取插入功能查询语句名称。
     * <p>支持自定义类型。
     *
     * @param clazz 待插入的数据类型。
     * @return      插入语句名称。
     */
    protected String getInsertQuery(final Class<?> clazz) {
        return clazz.getSimpleName() + ".insert";
    }
    
    /**
     * 功能描述：获取插入功能查询语句名称。
     * <p>支持自定义插入语句。
     *
     * @param clazz 待插入的数据类型。
     * @return      插入语句名称。
     */
    protected String getInsertQuery(final String... query) {
        String selectId = ".insert";

        if (query != null && query.length == 1) {

            selectId = "." + query[0];
        }
        return getEntityClass().getSimpleName() + selectId;
    }

    /**
     * 功能描述：获取更新功能查询语句名称。
     *
     * @param clazz 待更新的数据类型。
     * @return      更新语句名称。
     */
    protected String getUpdateQuery() {
        return getUpdateQuery(getEntityClass());
    }

    /**
     * 功能描述：获取更新功能查询语句名称。
     * <p>支持自定义类型。
     *
     * @param clazz 待更新的数据类型。
     * @return      更新语句名称。
     */
    protected String getUpdateQuery(final Class<?> clazz) {
        return clazz.getSimpleName() + ".update";
    }
    
    /**
     * 功能描述：获取更新功能查询语句名称。
     * <p>支持自定义更新语句。
     *
     * @param clazz 待更新的数据类型。
     * @return      更新语句名称。
     */
    protected String getUpdateQuery(final String... query) {
        String selectId = ".update";

        if (query != null && query.length == 1) {

            selectId = "." + query[0];
        }
        return getEntityClass().getSimpleName() + selectId;
    }

    /**
     * 功能描述：获取删除功能查询语句名称。
     *
     * @return  删除语句名称。
     */
    protected String getDeleteQuery() {
        return getDeleteQuery(getEntityClass());
    }

    /**
     * 功能描述：获取删除功能查询语句名称。
     * <p>支持自定义类型。
     * 
     * @param clazz     待删除数据类型。
     * @return          删除语句名称。
     */
    protected String getDeleteQuery(final Class<?> clazz) {
        return clazz.getSimpleName() + ".delete";
    }
    
    /**
     * 功能描述：获取删除功能查询语句名称。
     * <p>支持自定义删除语句。
     * 
     * @param query     自定义删除语句名。
     * @return          删除语句名称。
     */
    protected String getDeleteQuery(final String... query) {
        String selectId = ".delete";

        if (query != null && query.length == 1) {

            selectId = "." + query[0];
        }
        return getEntityClass().getSimpleName() + selectId;
    }

    /**
     * 功能描述：获取统计查询语句名称。
     *
     * @param query 自定义查询语句名。
     * @return      统计查询语句名称。
     */
    protected String getCountQuery(final String... query) {

        String selectId = ".count";

        if (query != null && query.length == 1) {

            selectId = "." + query[0];
        }
        return getEntityClass().getSimpleName() + selectId;
    }

    /**
     * 功能描述：获取查询语句名称。<p>
     * 缺省配置为<实体名称>.pageSelect
     *
     * @param query 自定义查询语句名。
     * @return      查询语句名称。  
     */
    protected String getQuerySelect(final String... query) {
        String selectId = ".pageSelect";

        if (query != null && query.length == 1) {

            selectId = "." + query[0];
        }

        return getEntityClass().getSimpleName() + selectId;
    }
    
    /**
     * 功能描述：获取数据访问模板。
     *
     * @return  数据访问模板对象。
     */
    protected SqlMapClientTemplate getDaoTemplate(){
        return this.getSqlMapClientTemplate();
    }
    
    /**
     * 功能描述：批量保存数据。
     * <p>支持自定义查询语句。
     *
     * @param insertQuery  插入语句名。
     * @param entitys      待插入的实体数据。
     * @throws DataAccessException
     */
    protected void batchSave(final String insertQuery,  final List<E> entitys) throws DataAccessException {
        if(null == entitys || entitys.isEmpty()){
            return;
        }
        
        // 批处理回调
        this.getSqlMapClientTemplate().execute(new SqlMapClientCallback<Void>() {

            @Override
            public Void doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                // 开始批处理
                executor.startBatch();
                
                for (int i = 0; i < entitys.size(); i++) {
                    executor.insert(insertQuery, entitys.get(i));
                    if(i > 0 && i % Constants.MYSQL_BATCH_PROCESS_MAX_SIZE == 0){ // 分批执行批量操作
                        executor.executeBatch();
                    }
                }
                
                // 执行批处理
                executor.executeBatch();
                
                return null;
            }
        });
    }
    
    /**
     * 功能描述：批量更新数据。
     * <p>支持自定义更新语句。
     *
     * @param updateQuery   待更新的语句名。
     * @param entitys       待更新的实体数据。
     * @throws DataAccessException
     */
    protected void batchUpdate(final String updateQuery, final List<E> entitys) throws DataAccessException {
        if(null == entitys || entitys.isEmpty()){
            return;
        }
        
        // 批处理回调
        this.getSqlMapClientTemplate().execute(new SqlMapClientCallback<Void>() {

            @Override
            public Void doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                // 开始批处理
                executor.startBatch();
                
                for (int i = 0; i < entitys.size(); i++) {
                    executor.update(updateQuery, entitys.get(i));
                    if(i > 0 && i % Constants.MYSQL_BATCH_PROCESS_MAX_SIZE == 0){ // 分批执行批量操作
                        executor.executeBatch();
                    }
                }
                
                // 执行批处理
                executor.executeBatch();
                
                return null;
            }
        });
    }
}
