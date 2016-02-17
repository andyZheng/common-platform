<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp"%>
<%@ page import="${basepackage}.model.*" %>
<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

<jsp:include page="/commons/header.jsp" flush="true">
	<jsp:param name="menu" value="pa_${className?lower_case}"/>
</jsp:include>

<script type="text/javascript">
$(document).ready(function() {
	// 分页需要依赖的初始化动作
	window.simpleTable = new SimpleTable('simpleTableForm',<@jspEl 'page.thisPageNumber'/>,<@jspEl 'page.pageSize'/>,'<@jspEl 'pageRequest.sortColumns'/>');
});
</script>
<div id="mainBd3">
<table width="98%" align="center" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="grid" align="center" valign="top">
		<form action="<@jspEl "ctx"/>${actionBasePath}/list.do" method="get" style="display: inline;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="conttab">
			<tr>
				<td width="433" height="22" align="left" class="btbj">
					<input type="submit" class="btn" style="width:60px" value="添加" onclick="getReferenceForm(this).action='<@jspEl 'ctx'/>${actionBasePath}/create.do'"/>
					<input type="button" class="btn" style="width:60px" value="删除" onclick="batchDelete('<@jspEl 'ctx'/>${actionBasePath}/delete.do','ids',document.forms.simpleTableForm)"/>
				</td>
				<td align="right">
					<input id="search" type="submit" class="btn" value="查询">
				</td>
			</tr>
		</table>
		</form>
		<form id="simpleTableForm" action="<c:url value="${actionBasePath}/list.do"/>" method="get" style="display: inline;">
			<!-- auto include parameters -->
			<c:forEach items="<@jspEl 'pageRequest.filters'/>" var="entry">
			<input type="hidden" name="s_<@jspEl 'entry.key'/>" value="<@jspEl 'entry.value'/>"/>
			</c:forEach>
			
			<input type="hidden" name="pageNumber" id="pageNumber" />
			<input type="hidden" name="pageSize" id="pageSize"/>
			<input type="hidden" name="sortColumns" id="sortColumns"/>
			
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="gridTable">
					<thead class="tableHeader">
						<tr class="head">
							<th style="width:1;"><input type="checkbox" onclick="setAllCheckboxState('ids',this.checked)"></th>
							
							<!-- 排序时为th增加sortColumn即可,new SimpleTable('sortColumns')会为tableHeader自动增加排序功能; -->
							<#list table.columns as column>
							<#if !column.htmlHidden>
							<th sortColumn="${column.sqlName}" ><%=${className}.ALIAS_${column.constantName}%></th>
							</#if>
							</#list>
			
							<th>操作</th>
						 </tr>
					</thead>
					<tbody class="tableBody">
					<c:forEach items="<@jspEl 'page.result'/>" var="item" varStatus="status">		  	  
					  <tr class="<@jspEl "status.count % 2 == 0 ? 'odd' : 'even'"/>">
						<td style="width:1px;"><input type="checkbox" name="ids" value="<@jspEl "item.id"/>"></td>
						
						<#list table.columns as column>
						<#if !column.htmlHidden>
						<td><#rt>
							<#compress>
							<#if column.isDateTimeColumn>
							<@jspEl "item."+column.columnNameLower+"String"/>&nbsp;
							<#else>
							<@jspEl "item."+column.columnNameLower/>&nbsp;
							</#if>
							</#compress>
						<#lt></td>
						</#if>
						</#list>
						<td width="80">
							<a href="<@jspEl 'ctx'/>${actionBasePath}/show.do?<@generateIdQueryString/>">查看</a>&nbsp;
							<a href="<@jspEl 'ctx'/>${actionBasePath}/edit.do?<@generateIdQueryString/>">修改</a>
						</td>
					  </tr>		  
				  	 </c:forEach>
					</tbody>
				</table>
			
				<simpletable:pageToolbar page="<@jspEl 'page'/>">
				</simpletable:pageToolbar>
				
			</div>
		</form>
		</td>
	</tr>
</table>
</body>
</html>

<#macro generateIdQueryString>
	<#if table.compositeId>
		<#assign itemPrefix = 'item.id.'>
	<#else>
		<#assign itemPrefix = 'item.'>
	</#if>
<#compress>
		<#list table.compositeIdColumns as column>
			<#t>${column.columnNameLower}=<@jspEl itemPrefix + column.columnNameLower/>&
		</#list>				
</#compress>
</#macro>