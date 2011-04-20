<!-- ***** BEGIN LICENSE BLOCK *****
   - Version: MPL 1.1
   -
   - The contents of this file are subject to the Mozilla Public License Version
   - 1.1 (the "License"); you may not use this file except in compliance with
   - the License. You may obtain a copy of the License at
   - http://www.mozilla.org/MPL/
   -
   - Software distributed under the License is distributed on an "AS IS" basis,
   - WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
   - for the specific language governing rights and limitations under the
   - License.
   -
   - The Original Code is the OpenCustomer CRM.
   -
   - The Initial Developer of the Original Code is
   - Thomas Bader (Bader & Jene Software-Ingenieurb�ro).
   - Portions created by the Initial Developer are Copyright (C) 2005
   - the Initial Developer. All Rights Reserved.
   -
   - Contributor(s):
   -   Thomas Bader <thomas.bader@bader-jene.de>
   -
   - ***** END LICENSE BLOCK ***** -->
   
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> 

<%@ taglib uri="/tags/struts-bean"  prefix="bean"  %>
<%@ taglib uri="/tags/struts-html"  prefix="html"  %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/oc"           prefix="oc"    %>
<%@ taglib uri="/tags/oc-common"    prefix="common" %>
<%@ taglib uri="/tags/oc-panel"     prefix="panel" %>

<%@ page import="org.opencustomer.webapp.Globals" %>

<html:html xhtml="true">
    <head>
	    <title><bean:message key="default.title"/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta http-equiv="expires" content="0"/>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="cache-control" content="no-cache"/>
        <meta http-equiv="cache-control" content="max-age=0"/>
        <meta http-equiv="cache-control" content="s-maxage=0"/>
        <link href="<%=request.getContextPath()%>/styles/standard.css" rel="stylesheet" type="text/css"/>
        <!--[if IE]>
        <link href="<%=request.getContextPath()%>/styles/standard_ie.css" rel="stylesheet" type="text/css"/>
        <![endif]-->
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/calendar/calendar.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/calendar/calendar-setup.js"></script>
    </head>
<body <tiles:getAsString name="body.ext"/>>
<div class="top" style="background-image: url('<%=request.getContextPath()%><bean:message key="image.logo.url"/>');">
	<div class="navigation">
		<tiles:insert template="/navigation/navigation1.jsp"/>
	</div>
</div>
<div class="leftHand">
	<div class="block">
		<div class="head">
			<label><bean:message key="entity.system.user"/></label>
		</div>
		<div>
			<bean:write name="<%=Globals.USER_KEY%>" property="userName" scope="session"/>
		</div>
	</div>	
	<div class="block">
		<tiles:insert template="/navigation/navigation2.jsp"/>
	</div>	
	<div class="block">
		<div>
			<% pageContext.setAttribute("myServerTime", new java.util.Date()); %><bean:write name="myServerTime" scope="page" formatKey="default.format.date.date2"/>
		</div>
	</div>
</div>
<div class="content">
    <div class="pageTitle">
		<h3>
			<tiles:getAsString name="page.title"/>
			<tiles:get name="page.title.url"/>
		</h3>
		<div class="navigation3">
			<tiles:insert template="/navigation/navigation3.jsp"/>
		</div>
	</div>
	<tiles:getAsString name="page.content"/>
</div>
<div class="bottom">
	<div class="version"><bean:message key="default.version"/></div>
	<div class="copyright"><bean:message key="default.copyright"/></div>
</div>

</body>
</html:html>