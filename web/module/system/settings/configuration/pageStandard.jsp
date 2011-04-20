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
   - Portions created by the Initial Developer are Copyright (C) 2006
   - the Initial Developer. All Rights Reserved.
   -
   - Contributor(s):
   -   Thomas Bader <thomas.bader@bader-jene.de>
   -
   - ***** END LICENSE BLOCK ***** --%>
   
<%@ include file="/tiles/page.jsp" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<html:errors/>

	<panel:edit>
		
		<fieldset class="unit">
			<legend><bean:message key="module.system.configuration.block.system"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="maxFailedLogins" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.maxFailedLogins"/></common:label>
					</td>
					<td>
						<html:text property="maxFailedLogins" styleId="maxFailedLogins" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="multipleLogins" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.multipleLogins"/></common:label>
					</td>
					<td>
						<html:checkbox property="multipleLogins" styleId="multipleLoginsAllowed"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="showOverview" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.showOverview"/></common:label>
					</td>
					<td>
						<html:checkbox property="showOverview" styleId="showOverview"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.system.configuration.block.default"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="defaultTimeLock" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.defaultTimeLock"/></common:label>
					</td>
					<td>
						<html:text property="defaultTimeLock" styleId="defaultTimeLock" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="defaultIpPattern" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.defaultIpPattern"/></common:label>
					</td>
					<td>
						<html:text property="defaultIpPattern" styleId="defaultIpPattern" maxlength="255" styleClass="full"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.system.configuration.block.email"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="mailSmtpServer" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.mailSmtpServer"/></common:label>
					</td>
					<td>
						<html:text property="mailSmtpServer" styleId="mailSmtpServer" maxlength="255" styleClass="full"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
