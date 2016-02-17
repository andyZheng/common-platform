/*
 * 文件名：JdoDao.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved.
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.dao.jdo;

import java.io.Serializable;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.platform.common.dao.CommonDao;

/**
 * 功能描述：<code>JdoDao</code>基于DataNucleus JDO API 定义Dao层数据访问接口。
 * <p>本类扩展自{@link CommonDao},将根据JDO的数据访问特性定义个性化数据访问接口。
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2015年6月20日 上午15:29:37
 * @since    Common-Platform/JDO DAO 1.0
 */
public interface JDODao<E extends Serializable, PK extends Serializable> extends CommonDao<E, PK> {

	/**
	 * 获取持久化管理器。
	 * @return 持久化管理对象。
	 */
	public PersistenceManager getPersistenceManager();
	
	/**
	 * 原生SQL查询。
	 * 
	 * @param sql			待查询待SQL语句,其中参数采用<b>?</b>作为占位符.
	 * @param parameters  	查询参数列表.
	 * @return				查询结果.
	 */
	public List<?> nativeSQLQuery(String sql, Object... parameters);
}
