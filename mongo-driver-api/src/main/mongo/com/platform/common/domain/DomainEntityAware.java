package com.platform.common.domain;

import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * 领域实体抽象模型。
 * <p/>
 * Created by andy on 2015/5/20.
 */
public abstract class DomainEntityAware extends Document {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    

    /** MongoDB ID标识名称 */
    public static final String FIELD_MONGO_ID = "_id";

    /** 实体ID标识名称 */
    public final static String FIELD_ID = "id";
    
    // 根据客户端消息机制，定义消息ID
    public final static String FIELD_MESSAGE_ID = "msgId";

    /** 当前请求发送的消息ID参数 */
    protected int currentMessageId;

    public DomainEntityAware() {
        super();
    }

    public DomainEntityAware(Map<String, Object> dataMap) {
        try {
            if (dataMap.containsKey(FIELD_MESSAGE_ID)) {
                this.currentMessageId = Integer.valueOf((String) dataMap.get(FIELD_MESSAGE_ID));
                dataMap.remove(FIELD_MESSAGE_ID);
            }
            
            if (dataMap.containsKey(FIELD_MONGO_ID)) {
                ObjectId objectId = (ObjectId)dataMap.get(FIELD_MONGO_ID);
                dataMap.put(FIELD_ID, objectId.toString());
                dataMap.remove(FIELD_MONGO_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.putAll(dataMap);
        }
    }

    /**
     * 功能描述：获取返回的消息ID。
     * 
     * @return 待返回的消息ID。
     */
    public int getReturnMessageId() {
        return ++this.currentMessageId;
    }
}
