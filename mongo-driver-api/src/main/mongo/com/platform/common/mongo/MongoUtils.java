package com.platform.common.mongo;

import org.apache.commons.lang.StringUtils;

import com.mongodb.DBRef;
import com.platform.common.domain.DomainEntityAware;

/**
 * 功能描述：MongoDB客户端常用工具类。</p>
 *
 * @author   andy.zheng
 * @version  0.1.0, 2015年6月16日 下午2:30:31
 * @since    QN-War/Mongo Client
 */
public final class MongoUtils {

    /**
     * 功能描述：根据实体类注解配置获取集合名称。</p>
     * 缺省为实体名称。
     * 
     * @param entity 实体类。
     * @return       集合名称。
     */
    public static <E extends DomainEntityAware> String getCollectionName(Class<E> entity) {
        MongoCollectionAware annotation = entity.getAnnotation(MongoCollectionAware.class);
        String collectionName = StringUtils.isBlank(annotation.collectionName()) ? entity.getSimpleName() : annotation.collectionName();

        return collectionName;
    }
    
    /**
     * 功能描述：创建文档引用实例。
     * 
     * @param collectionName    引用集合名称。
     * @param objId             引用文档ID。
     * @return                  文档引用实例对象。
     */
    public static DBRef newDBRef(String collectionName, String objId) {
        return new DBRef(collectionName, objId);
    }
}
