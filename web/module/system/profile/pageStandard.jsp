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
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<html:errors/>
		
	<panel:edit>
		<fieldset class="unit">
			<table>
				<tr>
					<td class="label">
						<common:label property="userName" errorStyleId="error"><bean:message key="entity.system.user.userName"/></common:label>
					</td>
					<td>
						<bean:write name="panel_user" property="userName" scope="page"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="oldPassword" errorStyleId="error"><bean:message key="module.system.user.profile.changePassword.oldPassword"/></common:label>
					</td>
					<td class="data">
						<html:password property="oldPassword" styleId="oldPassword" maxlength="20" redisplay="false" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="newPassword1" errorStyleId="error"><bean:message key="module.system.user.profile.changePassword.newPassword1"/></common:label>
					</td>
					<td class="data">
						<html:password property="newPassword1" styleId="newPassword1" maxlength="20" redisplay="false" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="newPassword2" errorStyleId="error"><bean:message key="module.system.user.profile.changePassword.newPassword2"/></common:label>
					</td>
					<td class="data">
						<html:password property="newPassword2" styleId="newPassword2" maxlength="20" redisplay="false" styleClass="full"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<table>
				<tr>
					<td class="label">
						<common:label property="person" errorStyleId="error"><bean:message key="entity.crm.person"/></common:label>
					</td>
					<td>
						<logic:present name="panel_user" property="person" scope="page">
						<bean:write name="panel_user" property="person.firstName" scope="page"/> <bean:write name="panel_user" property="person.lastName" scope="page"/>
						</logic:present>
						<logic:notPresent name="panel_user" property="person" scope="page">
						---
						</logic:notPresent>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="locale" errorStyleId="error"><bean:message key="entity.system.user.locale"/></common:label>
					</td>
					<td class="data">
						<html:select property="locale" styleId="locale" styleClass="full">
							<html:optionsCollection name="panel_locales" label="localizedName" value="locale"/>
						</html:select>
					</td>
				</tr>
			</table>
		</fieldset>		
		<fieldset class="unit">
			<table>
				<tr>
					<td class="label">
						<common:label property="roleId" errorStyleId="error"><bean:message key="entity.system.role"/></common:label>
					</td>
					<td>
						<bean:write name="panel_user" property="profile.role.name" scope="page"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="usergroup" errorStyleId="error"><bean:message key="entity.system.usergroup"/></common:label>
					</td>
					<td>
						<div style="margin-top: 5px;">
							<table class="inner">
								<logic:iterate id="data" name="panel_user" property="profile.usergroups" scope="page" indexId="index">
								<tr>
									<td>
										<bean:write name="data" property="name" scope="page"/>
									</td>
								</tr>
								</logic:iterate>
							</table>
						</div>
						<div style="margin: 10px 0px 5px 0px; font-weight: bold;">
							<common:label property="mainUsergroupId" errorStyleId="error"><bean:message key="entity.system.profile.defaultUsergroup"/></common:label>
						</div>
						<div>
							<bean:write name="panel_user" property="profile.defaultUsergroup.name" scope="page"/>
						</div>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<table>					
				<tr>
					<td class="label">
						<common:label property="lastFailedLogin" errorStyleId="error"><bean:message key="entity.system.user.lastFailedLogin"/></common:label>
					</td>
					<td>
						<logic:present name="panel_lastInvalidLogin" scope="page">
							<bean:write name="panel_lastInvalidLogin" property="loginDate" scope="page" formatKey="default.format.date.datetime"/>
						</logic:present>
						<logic:notPresent name="panel_lastInvalidLogin" scope="page">
							<bean:message key="default.select.none"/>
						</logic:notPresent>
					</td>
				</tr>
			</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
