package com.platform.common.mongo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.bson.BsonReader;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;

/**
 * 功能描述：基于{@link Document}</p>定制领域模型类编解码器。
 *
 * @author andy.zheng
 * @version 0.1.0, 2015年6月11日 下午12:33:13
 * @since QN-War/Document Codec V0.1.0
 */
public class CustomDocumentCodec<T extends Document> implements CollectibleCodec<T> {

    private DocumentCodec documentCodec;

    private Class<T> documentClass;

    public CustomDocumentCodec(Class<T> documentClass, DocumentCodec documentCodec) {
        this.documentCodec = documentCodec;
        this.documentClass = documentClass;
    }

    @Override
    public Class<T> getEncoderClass() {
        return documentClass;
    }

    @Override
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        this.documentCodec.encode(writer, value, encoderContext);
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = this.documentCodec.decode(reader, decoderContext);
        T result = convertToEntity(document);
        return result;
    }

    @Override
    public T generateIdIfAbsentFromDocument(T entity) {
        this.documentCodec.generateIdIfAbsentFromDocument(entity);
        return entity;
    }

    @Override
    public boolean documentHasId(T document) {
        return this.documentCodec.documentHasId(document);
    }

    @Override
    public BsonValue getDocumentId(T document) {
        return this.documentCodec.getDocumentId(document);
    }

    private T convertToEntity(Document document) {
        T result = null;
        
        try {
            Constructor<T> declaredConstructor = documentClass.getDeclaredConstructor(Map.class);
            result = declaredConstructor.newInstance(document);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        
        return result;
    }
}
