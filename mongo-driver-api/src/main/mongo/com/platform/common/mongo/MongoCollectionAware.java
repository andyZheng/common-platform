package com.platform.common.mongo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能描述：定制化Mongo集合标记注解类。</p>
 * 领域模型类必须标记本注解，才能与MongoDB中的集合名称为{@link MongoCollectionAware#collectionName}的Collection相对应，
 * 并且在访问集合时实现相应的编解码操作。
 *
 * @author   andy.zheng
 * @version  0.1.0, 2015年6月11日 下午2:46:03
 * @since    QN-War/Annotaion V0.1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoCollectionAware {
    
    /**
     * 功能描述：设置集合名称。
     * 
     * @return 当前集合名称。
     */
    public String collectionName() default "";
}
