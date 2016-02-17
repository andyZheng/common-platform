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
</script>
<div id="mainBd3">
<table width="98%" align="center" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="grid" align="center" valign="top">
		<br/>
			<h4>修改<%=${className}.TABLE_ALIAS%></h4>
		<br/>
		<form id="myform" action="<@jspEl "ctx"/>${actionBasePath}/save.do" method="post">
			<table class="formTable">
			<%@ include file="form_include.jsp" %>
			</table>
			<br/>
			<input class="btn" id="submitButton" name="submitButton" type="submit" value="提交" />
			<input class="btn" type="button" value="取消" onclick="history.back();"/>
		</form>
		</td>
	</tr>
</table>
</div>
</body>
</html>
