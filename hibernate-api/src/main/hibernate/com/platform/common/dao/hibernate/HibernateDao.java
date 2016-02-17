/*
 * 文件名：HibernateDao.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved.
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.dao.hibernate;

import java.io.Serializable;

import com.platform.common.dao.CommonDao;

/**
 * 功能描述：<code>HibernateDao</code>基于Hibernate定义DAO层高层接口。
 * <p>本类扩展自{@link CommonDao},将根据Hibernate的数据访问特性定义个性化数据访问接口。
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2014年6月20日 上午10:17:56
 * @since    Common-Platform/Hibernate DAO 1.0
 */
public interface HibernateDao<E extends Serializable, PK extends Serializable> extends CommonDao<E, PK> {

}
