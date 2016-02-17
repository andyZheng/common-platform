package com.platform.common.dao.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.platform.common.domain.DomainEntityAware.FIELD_MONGO_ID;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.platform.common.config.Constants;
import com.platform.common.mongo.MongoCollectionAware;

/**
 * 功能描述：MongoDB数据访问支持。</p>
 *
 * @author andy.zheng
 * @version 0.1.0, 2015年6月10日 下午2:30:52
 * @since QN-War/Mongo DAL
 */
@Repository
public class MongoDaoSupport<E extends Bson, PK extends Serializable> implements MongoDao<E, PK> {

    /** MongoDB集合名称 */
    private String collectionName;

    /** 实体类 */
    private Class<E> entityClass;

    /** 日志对象 */
    protected final Logger logger;

    @Autowired
    private MongoDaoTemplate mongoDaoTemplate;

    @SuppressWarnings("unchecked")
    public MongoDaoSupport() {
        logger = LoggerFactory.getLogger(this.getClass());

        // 获取待访问的集合名称
        try {
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            Type[] actualTypeArguments = type.getActualTypeArguments();
            this.entityClass = (Class<E>) actualTypeArguments[0];
            MongoCollectionAware annotation = this.entityClass.getAnnotation(MongoCollectionAware.class);
            if (null != annotation && !annotation.collectionName().isEmpty()) {
                this.collectionName = annotation.collectionName();
            } else {
                this.collectionName = this.entityClass.getSimpleName();
            }
        } catch (Exception e) {
            this.entityClass = (Class<E>) Document.class;
            this.collectionName = this.entityClass.getSimpleName();
        }
    }

    @Override
    public Long countAll() {
        return this.getCollection().count();
    }

    @Override
    public Long countByConditions(Map<String, Object> conditions) {
        Bson filter = this.buildAndConditions(conditions);

        return this.getCollection().count(filter);
    }

    @Override
    public void createIndex(Bson keys) {
        IndexOptions indexOptions = new IndexOptions();
        this.getCollection().createIndex(keys, indexOptions);
    }

    @Override
    public void createIndex(List<IndexModel> indexModels) {
        this.getCollection().createIndexes(indexModels);
    }

    @Override
    public void dropIndex(Bson keys) {
        this.getCollection().dropIndex(keys);
    }

    @Override
    public E getById(PK id) {
        ObjectId objectId = null;
        if (id instanceof ObjectId) {
            objectId = (ObjectId) id;
        } else {
            objectId = new ObjectId(id.toString());
        }
        Bson filter = eq(FIELD_MONGO_ID, objectId);

        return this.getCollection().find(filter).first();
    }

    /**
     * 功能描述：获取待访问的集合对象。
     * 
     * @return
     */
    protected MongoCollection<E> getCollection() {
        return this.mongoDaoTemplate.getCollection(collectionName, this.entityClass);
    }

    @Override
    public Boolean isExistByConditions(Map<String, Object> conditions) {
        List<E> result = this.queryByConditions(conditions);

        return CollectionUtils.isNotEmpty(result) ? true : false;
    }

    @Override
    public Boolean isExistByProperty(String property, Object value) {
        List<E> result = this.queryByProperty(property, value);

        return CollectionUtils.isNotEmpty(result) ? true : false;
    }

    @Override
    public void mapReduce(String mapFunction, String reduceFunction) {
        this.getCollection().mapReduce(mapFunction, reduceFunction);
    }

    @Override
    public List<E> queryAll() {
        MongoCollection<E> collection = this.getCollection();

        return collection.find().into(new ArrayList<E>());
    }

    @Override
    public List<E> queryAll(Integer offset, Integer pageSize, String... sortBy) {
        MongoCollection<E> collection = this.getCollection();

        // 构造分页查询器
        FindIterable<E> queryBuilder = collection.find().skip(offset).limit(pageSize);

        // 构造查询条件
        Bson sort = this.buildSort(sortBy);
        if (null != sort) {
            queryBuilder.sort(sort);
        }

        return queryBuilder.into(new ArrayList<E>());
    }

    @Override
    public List<E> queryAll(String sortBy) {
        MongoCollection<E> collection = this.getCollection();

        // 创建查询器
        FindIterable<E> queryBuilder = collection.find();

        // 构造查询条件
        Bson sort = this.buildSort(sortBy);
        if (null != sort) {
            queryBuilder.sort(sort);
        }

        return queryBuilder.into(new ArrayList<E>());
    }

    @Override
    public List<E> queryByConditions(Integer offset, Integer pageSize, Map<String, Object> conditions, String... sortBy) {
        MongoCollection<E> collection = this.getCollection();

        // 构造分页查询器
        Bson filter = this.buildAndConditions(conditions);
        FindIterable<E> queryBuilder = collection.find(filter).skip(offset).batchSize(pageSize);

        // 构造查询条件
        Bson sort = this.buildSort(sortBy);
        if (null != sort) {
            queryBuilder.sort(sort);
        }

        return queryBuilder.into(new ArrayList<E>());
    }

    @Override
    public List<E> queryByConditions(Map<String, Object> conditions, String... sortBy) {
        MongoCollection<E> collection = this.getCollection();

        // 构造分页查询器
        Bson filter = this.buildAndConditions(conditions);
        FindIterable<E> queryBuilder = collection.find(filter);

        // 构造查询条件
        Bson sort = this.buildSort(sortBy);
        if (null != sort) {
            queryBuilder.sort(sort);
        }

        return queryBuilder.into(new ArrayList<E>());
    }
    
    @Override
    public List<E> queryByOrConditions(Map<String, Object> conditions, String... sortBy) {
        MongoCollection<E> collection = this.getCollection();

        // 构造分页查询器
        Bson filter = this.buildOrConditions(conditions);
        FindIterable<E> queryBuilder = collection.find(filter);

        // 构造查询条件
        Bson sort = this.buildSort(sortBy);
        if (null != sort) {
            queryBuilder.sort(sort);
        }

        return queryBuilder.into(new ArrayList<E>());
    }

    @Override
    public List<E> queryByProperty(Integer offset, Integer pageSize, String property, Object value, String... sortBy) {
        MongoCollection<E> collection = this.getCollection();

        // 构造分页查询器
        FindIterable<E> queryBuilder = collection.find(eq(property, value)).skip(offset).batchSize(pageSize);

        // 构造查询条件
        Bson sort = this.buildSort(sortBy[0]);
        if (null != sort) {
            queryBuilder.sort(sort);
        }

        return queryBuilder.into(new ArrayList<E>());
    }

    @Override
    public List<E> queryByProperty(String property, Object value, String... sortBy) {
        MongoCollection<E> collection = this.getCollection();

        // 构造查询器
        FindIterable<E> queryBuilder = collection.find(eq(property, value));

        // 构造查询条件
        Bson sort = this.buildSort(sortBy);
        if (null != sort) {
            queryBuilder.sort(sort);
        }

        return queryBuilder.into(new ArrayList<E>());
    }

    @Override
    public E queryByUniqueProperty(String property, Object value) {
        MongoCollection<E> collection = this.getCollection();

        // 构造唯一查询条件
        FindIterable<E> queryBuilder = collection.find(eq(property, value));

        return queryBuilder.first();
    }

    @Override
    public E queryUniqueByConditions(Map<String, Object> conditions) {
        MongoCollection<E> collection = this.getCollection();

        // 构造唯一查询条件
        Bson filter = this.buildAndConditions(conditions);
        FindIterable<E> queryBuilder = collection.find(filter);

        return queryBuilder.first();
    }

    @Override
    @Deprecated
    public Boolean remove(E entity) {
        throw new UnsupportedOperationException("The MongoDB driver has not provided support!");
    }

    @Override
    public Boolean removeByConditions(Map<String, Object> conditions) {
        Bson filter = this.buildAndConditions(conditions);
        DeleteResult deleteResult = this.getCollection().deleteMany(filter);

        return deleteResult.getDeletedCount() > 0 ? true : false;
    }

    @Override
    public Boolean removeById(PK id) {
        ObjectId objectId = null;
        if (id instanceof ObjectId) {
            objectId = (ObjectId) id;
        } else {
            objectId = new ObjectId(id.toString());
        }
        Bson filter = eq(FIELD_MONGO_ID, objectId);
        DeleteResult deleteResult = this.getCollection().deleteOne(filter);

        return deleteResult.getDeletedCount() > 0 ? true : false;
    }

    @Override
    public Boolean removeByProperty(String property, String value) {
        Bson filter = eq(property, value);
        DeleteResult deleteResult = this.getCollection().deleteMany(filter);

        return deleteResult.getDeletedCount() > 0 ? true : false;
    }

    @Override
    public E save(E entity) {
        this.getCollection().insertOne(entity);
        return entity;
    }

    @Override
    public void save(List<E> entitys) {
        this.getCollection().insertMany(entitys);
    }
    
    public void setCollectionName(String collectionName) {
        if (null != collectionName) {
            this.collectionName = collectionName;
        }
    }

    @Override
    @Deprecated
    public Boolean update(E entity) {
        throw new UnsupportedOperationException("The MongoDB driver has not provided support!");
    }

    @Override
    public Boolean updateByConditions(Map<String, Object> updateProperties, Map<String, Object> updateConditions) {
        return this.updateByConditions(updateProperties, updateConditions, true, false);
    }

    @Override
    public Boolean updateByConditions(Map<String, Object> updateProperties, String property, Object value) {
        return this.updateByConditions(updateProperties, property, value, true, false);
    }

    @Override
    public Boolean updateOneByConditions(Map<String, Object> updateProperties, Map<String, Object> updateConditions) {
        return this.updateByConditions(updateProperties, updateConditions, false, false);
    }
    
    

    @Override
    public Boolean updateOneByConditions(Map<String, Object> updateProperties, String property, Object value) {
        return this.updateByConditions(updateProperties, property, value, false, false);
    }

    public Boolean updateByConditions(Map<String, Object> updateProperties, Map<String, Object> updateConditions,
            boolean many, boolean upsert) {
        Bson filter = this.buildAndConditions(updateConditions);
        Bson update = this.buildUpdates(updateProperties);
        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.upsert(upsert);

        UpdateResult updateResult = null;
        if (many) {
            updateResult = this.getCollection().updateMany(filter, update, updateOptions);
        } else {
            updateResult = this.getCollection().updateOne(filter, update, updateOptions);
        }

        return updateResult.getModifiedCount() > 0 ? true : false;
    }

    public Boolean updateByConditions(Map<String, Object> updateProperties, String property, Object value, boolean many, boolean upsert) {
        Bson filter = eq(property, value);
        Bson update = this.buildUpdates(updateProperties);
        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.upsert(upsert);

        UpdateResult updateResult = null;
        if (many) {
            updateResult = this.getCollection().updateMany(filter, update, updateOptions);
        } else {
            updateResult = this.getCollection().updateOne(filter, update, updateOptions);
        }

        return updateResult.getModifiedCount() > 0 ? true : false;
    }

    /**
     * 功能描述：根据查询参数构造AND查询条件。
     * 
     * @param queryConditions
     * @return
     */
    protected Bson buildAndConditions(Map<String, Object> queryConditions) {
        List<Bson> conditions = new ArrayList<>();
        if (null != queryConditions) {
            for (String name : queryConditions.keySet()) {
                Object val = queryConditions.get(name);
                conditions.add(eq(name, val));
            }
        }

        Bson filter = and(conditions);
        return filter;
    }
    
    /**
     * 功能描述：根据查询参数构造OR查询条件。
     * 
     * @param queryConditions
     * @return
     */
    private Bson buildOrConditions(Map<String, Object> queryConditions) {
        List<Bson> conditions = new ArrayList<>();
        if (null != queryConditions) {
            for (String name : queryConditions.keySet()) {
                Object val = queryConditions.get(name);
                conditions.add(eq(name, val));
            }
        }
        
        Bson filter = or(conditions);
        return filter;
    }

    /**
     * 功能描述：根据排序字符串构造排序对象。
     *
     * @param orderBy
     *            当前查询排序字符串。
     * @return 排序对象
     */
    private Bson buildSort(String... orderBy) {
        Bson sort = null;

        if (null != orderBy && orderBy.length == 1) {
            String sortBy = orderBy[0];
            String[] properties = null;

            if (sortBy.endsWith(Constants.ORDER_DIRECTION_DESC)) { // 降序
                int endIndex = sortBy.indexOf(Constants.ORDER_DIRECTION_DESC);
                properties = StringUtils.trim(sortBy.substring(0, endIndex)).split("\\,");
                sort = descending(properties);
            } else { // 升序
                int endIndex = sortBy.indexOf(Constants.ORDER_DIRECTION_ASC);
                if (-1 != endIndex) {
                    properties = StringUtils.trim(sortBy.substring(0, endIndex)).split("\\,");
                } else {
                    properties = sortBy.split("\\,");
                }

                sort = ascending(properties);
            }
        }

        return sort;
    }

    /**
     * 功能描述：根据待更新参数构造更新对象。
     * 
     * @param updateProperties
     *            待更新参数列表。
     * @return 更新对象。
     */
    protected Bson buildUpdates(Map<String, Object> updateProperties) {
        Document update = new Document();
        
        Document setDocument = null;
        Document pushDocument = null;
        if (null != updateProperties) {
            for (String name : updateProperties.keySet()) {
                Object value = updateProperties.get(name);
                if (value instanceof Collection) {
                    if (null == pushDocument) {
                        pushDocument = new Document();
                    }
                    pushDocument.append(name, new Document("$each", value));
                } else {
                    if (null == setDocument) {
                        setDocument = new Document();
                    }
                    setDocument.append(name, value);
                }
            }
        }

        if (null != setDocument) {
            update.append("$set", setDocument);
        }
        
        if (null != pushDocument) {
            update.append("$addToSet", pushDocument);
        }

        return update;
    }
}
