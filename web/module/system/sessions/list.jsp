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

<tiles:insert definition="standard">
	<tiles:put name="page.title"><bean:message key="module.system.user.manage.sessionMonitor.headLine"/></tiles:put>
	<tiles:put name="page.content">
		<html:form action="/system/sessions" styleClass="default" style="width: 500px;">
			<div class="page">
				<fieldset class="list">
					<legend>
						<bean:message key="module.system.user.manage.sessionMonitor.overview"/>
					</legend>
					<table class="result">
						<colgroup>
							<col width="1*">
							<col width="1*">
							<col width="1*">
						</colgroup>
						<tr>
							<th><bean:message key="module.system.user.manage.sessionMonitor.underMinute1"/></th>
							<th><bean:message key="module.system.user.manage.sessionMonitor.underMinute5"/></th>
							<th><bean:message key="module.system.user.manage.sessionMonitor.underMinute15"/></th>
						</tr>
						<tr class="row">
							<td><bean:write name="statistic" property="underMinute1" scope="request"/></td>
							<td><bean:write name="statistic" property="underMinute5" scope="request"/></td>
							<td><bean:write name="statistic" property="underMinute15" scope="request"/></td>
						</tr>
						<tr>
							<th><bean:message key="module.system.user.manage.sessionMonitor.underMinute30"/></th>
							<th><bean:message key="module.system.user.manage.sessionMonitor.underMinute60"/></th>
							<th><bean:message key="module.system.user.manage.sessionMonitor.overMinute60"/></th>
						</tr>
						<tr class="row">
							<td><bean:write name="statistic" property="underMinute30" scope="request"/></td>
							<td><bean:write name="statistic" property="underMinute60" scope="request"/></td>
							<td><bean:write name="statistic" property="overMinute60" scope="request"/></td>
						</tr>
					</table>
				</fieldset>
				<fieldset class="list">
					<legend>
						<bean:message key="module.system.user.manage.sessionMonitor.userList"/>
					</legend>
					<table class="result">
						<colgroup>
							<col width="2*">
							<col width="1*">
							<col width="1*">
							<col width="1*">
						</colgroup>
						<tr>
							<th><bean:message key="entity.system.user"/></th>
							<th><bean:message key="module.system.user.manage.sessionMonitor.login"/></th>
							<th><bean:message key="module.system.user.manage.sessionMonitor.lastAccess"/></th>
							<th><bean:message key="module.system.user.manage.sessionMonitor.inactive"/></th>
						</tr>
					<logic:iterate id="data" indexId="index" name="list" scope="request">
						<tr class="row">
							<td><bean:write name="data" property="username" scope="page"/></td>
							<td><bean:write name="data" property="loginTime" scope="page" formatKey="default.format.date.exactTime"/></td>
							<td><bean:write name="data" property="lastAccessTime" scope="page" formatKey="default.format.date.exactTime"/></td>
							<td><bean:write name="data" property="inactiveString" scope="page"/></td>
						</tr>
					</logic:iterate>
					</table>
				</fieldset>
				<fieldset class="action">
					<html:submit><bean:message key="button.refresh"/></html:submit>
				</fieldset>
			</html:form>
		</div>
	</tiles:put>
</tiles:insert>
