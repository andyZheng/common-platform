<%@page import="${basepackage}.model.*" %>
<#include "/macro.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>
<script type='text/javascript'>
$(function() {
	$("#myform").validate({
		onkeyup: false,
		rules: {
		},
		messages: {
		}	
	});
});
</script>
<#list table.columns as column>
<#if column.htmlHidden>
	<input type="hidden" id="${column.columnNameLower}" name="${column.columnNameLower}" value="<@jspEl classNameLower+"."+column.columnNameLower/>"/>
</#if>
</#list>

<#list table.columns as column>
	<#if !column.htmlHidden>	
	<tr>	
		<td class="field">
			<#if !column.nullable><span class="required">*</span></#if><%=${className}.ALIAS_${column.constantName}%>:
		</td>		
		<td>
	<#if column.isDateTimeColumn>
		<input value="<@jspEl classNameLower+"."+column.columnNameLower+"String"/>" onclick="WdatePicker({dateFmt:'<%=${className}.FORMAT_${column.constantName}%>'})" id="${column.columnNameLower}String" name="${column.columnNameLower}String"  maxlength="0" class="finput ${column.validateString}" />
	<#else>
		<input value="<@jspEl classNameLower+"."+column.columnNameLower/>" id="${column.columnNameLower}" name="${column.columnNameLower}" class="finput ${column.validateString}" maxlength="${column.size}"/>
	</#if>
		</td>
	</tr>	
	
	</#if>
</#list>		


