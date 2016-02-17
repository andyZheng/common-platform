package com.platform.common.mongo;

import org.bson.Document;
import org.bson.codecs.CodeWithScopeCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.CodeWithScope;

/**
 * 功能描述：个性化定制文档编解码提供者。</p>
 *
 * @author   andy.zheng
 * @version  0.1.0, 2015年6月11日 下午12:40:31
 * @since    QN-War/Document Codec V0.1.0
 */
public class CustomDocumentCodecProvider implements CodecProvider {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == CodeWithScope.class) {
            return (Codec<T>) new CodeWithScopeCodec(registry.get(Document.class));
        }
        
        try {
            MongoCollectionAware collectionAware = clazz.getAnnotation(MongoCollectionAware.class);
            if (null != collectionAware) {
                return new CustomDocumentCodec(clazz, (DocumentCodec)registry.get(Document.class));
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }
}
