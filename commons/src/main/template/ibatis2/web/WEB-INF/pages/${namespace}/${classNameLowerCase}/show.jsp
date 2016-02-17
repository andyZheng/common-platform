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
<table  width="98%" align="center" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="grid" align="center" valign="top">
		<br/>
			<h4>查看<%=${className}.TABLE_ALIAS%></h4>
		<br/>
			<table class="formTable">
			<#list table.columns as column>
			<#if !column.htmlHidden>
				<tr>	
					<td class="field"><%=${className}.ALIAS_${column.constantName}%></td>	
					<td><#rt>
					<#compress>
					<#if column.isDateTimeColumn>
					<c:out value='<@jspEl classNameLower+"."+column.columnNameLower+"String"/>'/>
					<#else>
					<c:out value='<@jspEl classNameLower+"."+column.columnNameLower/>'/>
					</#if>
					</#compress>
					<#lt></td>
				</tr>
			</#if>
			</#list>
			</table>
			<br/>
			<input class="btn" type="button" value="返回" onclick="history.back();"/>
		</td>
	</tr>
</table>
</div>
</body>
</html>