<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

<#include "/java_imports.include">

import org.springframework.stereotype.Repository;

/**
 * 功能描述：<code>${className}Dao</code>基于MyBatis实现的数据访问实现类。
 * </p>   
 * @author   andy.zheng0807@gmail.com
 * @version  ${projectVersion}, ${now}
 * @since    QN-War/DAO V1.0
 */
@Repository
public class ${className}Dao extends BaseMyBatisDao<${className},${table.idColumn.javaType}>{
	
	public void saveOrUpdate(${className} entity) {
		if(entity.get${table.idColumn.columnName}() == null) 
			save(entity);
		else 
			update(entity);
	}
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return (${className})getSqlSessionTemplate().selectOne("${className}.getBy${column.columnName}",v);
	}	
	
	</#if>
	</#list>

}
