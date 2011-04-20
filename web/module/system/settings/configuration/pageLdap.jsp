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
	<tiles:put name="body.ext">onload="check()"</tiles:put>
	<tiles:put name="page.content">

	<script type="text/javascript">
	<!--
		function check() {
			form = document.getElementById("system.settings.configuration.pageLdapForm");
			if(form.authenticationEnabled.checked || form.addressExportEnabled.checked) {
				form.server.disabled = false;
				form.port.disabled   = false;
				form.baseDn.disabled = false;
			} else {
				form.server.disabled = true;
				form.port.disabled   = true;
				form.baseDn.disabled = true;
			}
			
			if(form.authenticationEnabled.checked) {
				form.systemUser.disabled  = false;
				form.userPrefix.disabled  = false;
				form.groupPrefix.disabled = false;
			} else {
				form.systemUser.disabled  = true;
				form.userPrefix.disabled  = true;
				form.groupPrefix.disabled = true;
			}
			
			if(form.addressExportEnabled.checked) {
				form.addressInitialExport.disabled = false;
				
				form.adminUser.disabled            = false;
				form.adminPassword.disabled        = false;
				form.addressPrefix.disabled        = false;
				form.addressCompanyPrefix.disabled = false;
				form.addressPersonPrefix.disabled  = false;
			} else {
				form.addressInitialExport.disabled = true;

				form.adminUser.disabled            = true;
				form.adminPassword.disabled        = true;
				form.addressPrefix.disabled        = true;
				form.addressCompanyPrefix.disabled = true;
				form.addressPersonPrefix.disabled  = true;
			}
		}
	//-->
	</script>
	
	<html:errors/>

	<panel:edit>
		
		<fieldset class="unit">
			<legend><bean:message key="module.system.configuration.pageLdap.block.global"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="authenticationEnabled" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.authenticationEnabled"/></common:label>
					</td>
					<td>
						<html:checkbox property="authenticationEnabled" styleId="authenticationEnabled" onchange="check()"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="addressExportEnabled" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.addressExportEnabled"/></common:label>
					</td>
					<td>
						<html:checkbox property="addressExportEnabled" styleId="addressExportEnabled" onchange="check()"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="addressInitialExport" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.addressInitialExport"/></common:label>
					</td>
					<td>
						<html:checkbox property="addressInitialExport" styleId="addressInitialExport" onchange="check()"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.system.configuration.pageLdap.block.server"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="server" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.server"/></common:label>
					</td>
					<td>
						<html:text property="server" styleId="server" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="port" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.port"/></common:label>
					</td>
					<td>
						<html:text property="port" styleId="port" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="baseDn" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.baseDn"/></common:label>
					</td>
					<td>
						<html:text property="baseDn" styleId="baseDn" maxlength="255" styleClass="full"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.system.configuration.pageLdap.block.userManagement"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="systemUser" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.systemUser"/></common:label>
					</td>
					<td>
						<html:text property="systemUser" styleId="systemUser" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="userPrefix" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.addressUserPrefix"/></common:label>
					</td>
					<td>
						<html:text property="userPrefix" styleId="userPrefix" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="groupPrefix" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.addressGroupPrefix"/></common:label>
					</td>
					<td>
						<html:text property="groupPrefix" styleId="groupPrefix" maxlength="255" styleClass="full"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.system.configuration.pageLdap.block.addressExport"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="adminUser" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.adminUser"/></common:label>
					</td>
					<td>
						<html:text property="adminUser" styleId="adminUser" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="adminPassword" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.adminPassword"/></common:label>
					</td>
					<td>
						<html:text property="adminPassword" styleId="adminPassword" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="addressPrefix" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.addressPrefix"/></common:label>
					</td>
					<td>
						<html:text property="addressPrefix" styleId="addressPrefix" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="addressCompanyPrefix" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.addressCompanyPrefix"/></common:label>
					</td>
					<td>
						<html:text property="addressCompanyPrefix" styleId="addressCompanyPrefix" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="addressPersonPrefix" errorStyleId="error"><bean:message key="entity.system.configuration.key.system.ldap.addressPersonPrefix"/></common:label>
					</td>
					<td>
						<html:text property="addressPersonPrefix" styleId="addressPersonPrefix" maxlength="255" styleClass="full"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
