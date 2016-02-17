/*
 * 文件名：JPADao.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved.
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.dao.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import com.platform.common.dao.CommonDao;

/**
 * 功能描述：<code>JPADao</code>基于DataNucleus JPA定义Dao层数据访问接口。
 * <p>本类扩展自{@link CommonDao},将根据JPA的数据访问特性定义个性化数据访问接口。
 *
 * @param <E>   操作的实体对象。
 * @param <PK>  操作的实体对象的唯一标识符。
 * @author      andy.zheng0807@gmail.com
 * @version     1.0, 2014年6月20日 上午10:07:37
 * @since       Common-Platform/Ibatis DAO 1.0
 */
public interface JPADao<E extends Serializable, PK extends Serializable> extends CommonDao<E, PK> {

    /**
     * 获取实体管理器。
     * @return  当前实体管理器。
     */
    public EntityManager getEntityManager();
}
