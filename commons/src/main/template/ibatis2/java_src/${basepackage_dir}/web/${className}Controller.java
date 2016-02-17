<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<#assign annotationDescription = "${table.tableAlias}请求控制器实现类"> 
<#assign annotationSee = "${className}Manager"> 
package ${basepackage}.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.org.rapid_framework.beanutils.BeanUtils;

<#include "/java_imports.include">
@Controller
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class ${className}Controller extends BaseSpringController {
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = " id desc"; 
	
	@Autowired
	private ${className}Manager ${classNameLower}Manager;
	
	private final String LIST_ACTION = "redirect:${actionBasePath}/list.do";
	
	/** 
	 * 进入查询页面.
	 **/
	@RequestMapping
	public void query() {
		// DO NOTHIING
	}
	
	/** 
	 * 执行搜索 .
	 **/
	@RequestMapping
	public void list() {
		PageRequest pageRequest = newPageRequest(request,DEFAULT_SORT_COLUMNS);
		//pageRequest.getFilters(); //add custom filters
		
		Page page = ${classNameLower}Manager.findByPageRequest(pageRequest);
		saveIntoModel(page, pageRequest);
	}
	
	/** 
	 * 查看对象.
	 **/
	@RequestMapping
	public void show(final Long id, final ModelMap model) throws Exception {
		${className} ${classNameLower} = (${className})${classNameLower}Manager.get(id);
		model.put(${className}.FIELD_ENTIY , ${classNameLower});
	}
	
	/** 
	 * 进入添加页面.
	 **/
	@RequestMapping
	public void create() throws Exception {
		// DO NOTHING
	}
	
	/** 
	 * 保存对象.
	 **/
	@RequestMapping
	public String save(final ${className} ${classNameLower}) throws Exception {
		${classNameLower}Manager.saveOrUpdate(${classNameLower});
		return LIST_ACTION;
	}
	
	/**
	 * 进入更新页面.
	 **/
	@RequestMapping
	public void edit(final Long id, final ModelMap model) throws Exception {
		${className} ${classNameLower} = (${className})${classNameLower}Manager.get(id);
		model.put(${className}.FIELD_ENTIY , ${classNameLower});
	}
	
	/**
	 * 删除对象.
	 **/
	@RequestMapping
	public void delete(final String[] ids) {
		${classNameLower}Manager.removeByIds(ids);
		success();
	}
	
}

<#macro generateIdParameter> 
	<#if table.compositeId>
		${className}Id id = new ${className}Id();
		bind(request, id);
	<#else>
		<#list table.compositeIdColumns as column>
		${column.javaType} id = new ${column.javaType}(request.getParameter("${column.columnNameLower}"));
		</#list>
	</#if>
</#macro>