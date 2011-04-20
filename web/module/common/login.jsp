<%-- ***** BEGIN LICENSE BLOCK *****
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
   - Thomas Bader (Bader & Jene Software-Ingenieurbüro).
   - Portions created by the Initial Developer are Copyright (C) 2005
   - the Initial Developer. All Rights Reserved.
   -
   - Contributor(s):
   -   Thomas Bader <thomas.bader@bader-jene.de>
   -
   - ***** END LICENSE BLOCK ***** --%>
   
<%@ include file="/tiles/page.jsp" %>

<html:html xhtml="true">
    <head>
	    <title><bean:message key="default.title"/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <meta http-equiv="expires" content="0"/>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="cache-control" content="no-cache"/>
        <meta http-equiv="cache-control" content="max-age=0"/>
        <meta http-equiv="cache-control" content="s-maxage=0"/>
        <link href="<%=request.getContextPath()%>/styles/standard.css" rel="stylesheet" type="text/css"/>
    </head>
<body style="text-align: center;">
<%-- CONTENT START --%>
<html:form action="/login" focus="login" styleClass="default">
	<div class="page" style="width: 400px; margin-top: 100px; margin-left: auto; margin-right: auto;">
		<fieldset class="login" style="border: none; padding: 0px;">
			<table>
				<tr>
					<td rowspan="3" style="text-align: center; vertical-align: middle">
						<html:img altKey="image.logo.text" titleKey="image.logo.text" pageKey="image.logo.url"/>
					</td>
					<td>
						<common:label property="login" errorStyleId="error"><bean:message key="entity.system.user.userName"/></common:label><br/>
						<html:text property="login" styleId="login" maxlength="20"/>
					</td>
				</tr>
				<tr>
					<td>
						<common:label property="password" errorStyleId="error"><bean:message key="entity.system.user.password"/></common:label><br/>
						<html:password property="password" styleId="password" maxlength="20" redisplay="false"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="action">
			<html:submit property="doLogin" styleClass="action"><bean:message key="button.login"/></html:submit>
		</fieldset>
	</div>
</html:form>

<logic:present name="<%=org.apache.struts.Globals.ERROR_KEY%>" scope="request">
<table class="loginError">
	<tr>
		<td>
			<html:errors/>
		</td>
	</tr>
</table>
</logic:present>
<%-- CONTENT END --%>
    </body>
</html:html>