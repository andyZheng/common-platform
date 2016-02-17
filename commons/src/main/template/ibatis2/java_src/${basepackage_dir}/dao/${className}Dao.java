<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
<#assign annotationDescription = "${table.tableAlias}数据访问实现类"> 
<#assign annotationSee = "cn.org.rapid_framework.page.PageRequest"> 
package ${basepackage}.dao;

import org.springframework.stereotype.Component;

<#include "/java_imports.include">
@Component
@SuppressWarnings({"rawtypes" ,"unchecked"})
public class ${className}Dao extends BaseIbatisDao<${className},${table.idColumn.javaType}>{

	public Class getEntityClass() {
		return ${className}.class;
	}
	
	public void saveOrUpdate(${className} entity) {
		if(entity.get${table.idColumn.columnName}() == null) 
			save(entity);
		else 
			update(entity);
	}
	
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return (${className})getSqlMapClientTemplate().queryForObject("${className}.getBy${column.columnName}",v);
	}	
	</#if>
	</#list>

}
