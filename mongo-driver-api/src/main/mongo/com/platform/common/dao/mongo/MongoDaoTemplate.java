package com.platform.common.dao.mongo;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.Transformer;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.DBRef;
import com.mongodb.DBRefCodecProvider;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.platform.common.mongo.CustomDocumentCodecProvider;

/**
 * 功能描述：MongoDB数据访问模板类。</p>
 *
 * @author andy.zheng
 * @version 0.1.0, 2015年6月12日 上午9:51:44
 * @since QN-War/Mongo DAO API
 */
@Component
public class MongoDaoTemplate {

    /** 默认数据库名称 */
    private static final String DEFAULT_DATABASE_NAME = "test";

    /** MongoDB客户端对象 */
    @Autowired
    private MongoClient mongoClient;

    /** MongoDB客户端URI配置对象 */
    @Autowired
    private MongoClientURI mongoClientURI;

    /** MongoDB数据库对象 */
    private volatile static MongoDatabase db;
    
    /**
     * 功能描述：获取待访问的集合对象。
     * 
     * @return
     */
    public <E> MongoCollection<E> getCollection(String collectionName, Class<E> resultClass) {
        if (null == db) {
            // 获取待访问的数据库
            String customDatabaseName = mongoClientURI.getDatabase();
            customDatabaseName = customDatabaseName == null ? DEFAULT_DATABASE_NAME : customDatabaseName;

            // 获取数据库实例并注册定制化编解码列表
            List<CodecProvider> providerList = Arrays.asList(new ValueCodecProvider(), new DBRefCodecProvider(),
                    new DocumentCodecProvider(new DocumentToDBRefTransformer()), new CustomDocumentCodecProvider()
            // new DBObjectCodecProvider(),
            // new BsonValueCodecProvider()
            );
            CodecRegistry codecRegistry = CodecRegistries.fromProviders(providerList);
            db = mongoClient.getDatabase(customDatabaseName).withCodecRegistry(codecRegistry);

            String customCollectionName = mongoClientURI.getCollection();
            if (null == collectionName && null != customCollectionName) {
                collectionName = customCollectionName;
            }
        }

        return db.getCollection(collectionName, resultClass);
    }
    
    public void setDatabaseName(String databaseName) {
        if (null != databaseName) {
            db = mongoClient.getDatabase(databaseName);
        }
    }

    static class DocumentToDBRefTransformer implements Transformer {
        @Override
        public Object transform(final Object value) {
            if (value instanceof Document) {
                Document document = (Document) value;
                if (document.containsKey("$id") && document.containsKey("$ref")) {
                    return new DBRef((String) document.get("$ref"), document.get("$id"));
                }
            }
            return value;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
}
