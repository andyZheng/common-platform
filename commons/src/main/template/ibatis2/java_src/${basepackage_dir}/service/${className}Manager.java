<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<#assign annotationDescription = "${table.tableAlias}业务处理实现类"> 
<#assign annotationSee = "${className}Dao"> 

package ${basepackage}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

<#include "/java_imports.include">
@Component
@Transactional
@SuppressWarnings("rawtypes")
public class ${className}Manager extends BaseManager<${className},${table.idColumn.javaType}>{

	@Autowired
	private ${className}Dao ${classNameLower}Dao;
	
	public EntityDao getEntityDao() {
		return this.${classNameLower}Dao;
	}

<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return ${classNameLower}Dao.getBy${column.columnName}(v);
	}	
	</#if>
</#list>
}
