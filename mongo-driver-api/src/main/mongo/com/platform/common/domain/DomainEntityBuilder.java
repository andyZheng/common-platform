package com.platform.common.domain;

import java.util.Map;

/**
 * 功能描述：领域模型类构建器。</p>
 *
 * @author   andy.zheng
 * @version  0.1.0, 2015年6月11日 下午6:02:39
 * @since    QN-War/Domain Components
 */
public interface DomainEntityBuilder<E> {
    
    /**
     * 功能描述：封装待创建的对象。
     * 
     * @return  瞬时态的待创建对象。
     */
    public E buildForCreate();
    
    /**
     * 功能描述：封装待更新的属性集合。
     * 
     * @param   old  更新的目标对象。（持久态对象） 
     * @return  待更新的属性集合。          
     */
    public Map<String, Object> buildForUpdate(E old);
}
