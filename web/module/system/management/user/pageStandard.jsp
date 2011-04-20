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

<%@ page import="org.opencustomer.webapp.Globals" %>
<%@ page import="org.opencustomer.framework.webapp.util.TagUtility" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>
<%@ page import="org.opencustomer.db.vo.system.UserVO" %>
<%@ page import="org.opencustomer.framework.db.vo.EntityAccess" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<html:errors/>

	<panel:edit focus="userName"><%
		UserVO user = (UserVO)pageContext.getAttribute("panel_user");

        boolean isActiveUser = false;
        if(pageContext.getAttribute("panel_isActiveUser") != null)
            isActiveUser = (Boolean)pageContext.getAttribute("panel_isActiveUser"); 
        
        boolean isLdapUser = false;
        if(pageContext.getAttribute("panel_isLdapUser") != null)
            isLdapUser = (Boolean)pageContext.getAttribute("panel_isLdapUser"); 
	%>
		<fieldset class="unit">
			<table>
				<tr>
					<td class="label">
						<common:label property="userName" errorStyleId="error"><bean:message key="entity.system.user.userName"/></common:label>
					</td>
					<td>
						<html:text property="userName" styleId="userName" maxlength="255" styleClass="full" disabled="<%=isLdapUser%>"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="userPassword" errorStyleId="error"><bean:message key="entity.system.user.password"/></common:label>
					</td>
					<td>
						<html:password property="userPassword" styleId="userPassword" maxlength="255" styleClass="full" disabled="<%=isActiveUser||isLdapUser%>"/>
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
						<bean:message key="default.select.none"/>
						</logic:notPresent>
					</td>
					<td class="action">
						<oc:authenticate right="<%=Right.ADMINISTRATION_USERMANAGEMENT_WRITE.getLabel()%>">
						<panel:editable condition="true">
						<logic:present name="panel_user" property="person" scope="page">
						<html:image property="doRemovePerson" pageKey="image.icon.delete.url" titleKey="image.icon.delete.text" altKey="image.icon.delete.text"/>
						</logic:present>
						<logic:notPresent name="panel_user" property="person" scope="page">
						<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
						</logic:notPresent>
						</panel:editable>
						<panel:editable condition="false">
						<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
						</panel:editable>
						
						<panel:editable condition="true">
						<logic:present name="panel_entity" property="profile.role" scope="page">
						<logic:equal name="panel_entity" property="profile.role.admin" scope="page" value="false">
						<html:image property="doAddPerson" pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text"/>
						</logic:equal>
						<logic:equal name="panel_entity" property="profile.role.admin" scope="page" value="true">
						<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
						</logic:equal>
						</logic:present>
						<logic:notPresent name="panel_entity" property="profile.role" scope="page">
						<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
						</logic:notPresent>
						</panel:editable>
						<panel:editable condition="false">
						<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
						</panel:editable>
						</oc:authenticate>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="locale" errorStyleId="error"><bean:message key="entity.system.user.locale"/></common:label>
					</td>
					<td class="data" colspan="2">
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
						<common:label property="role" errorStyleId="error"><bean:message key="entity.system.role"/></common:label>
					</td>
					<td>
						<logic:present name="panel_entity" property="profile.role" scope="page">
						<bean:write name="panel_entity" property="profile.role.name" scope="page"/>
						</logic:present>
						<logic:notPresent name="panel_entity" property="profile.role" scope="page">
						<bean:message key="default.select.none"/>
						</logic:notPresent>
					</td>
					<td class="action">
						<oc:authenticate right="<%=Right.ADMINISTRATION_USERMANAGEMENT_WRITE.getLabel()%>">
						<panel:editable condition="true">
							<logic:equal name="panel_isLdapUser" scope="page" value="false">
							<html:image property="doAddRole" pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text" disabled="<%=isLdapUser%>"/>
							</logic:equal>
							<logic:equal name="panel_isLdapUser" scope="page" value="true">
							<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
							</logic:equal>
						</panel:editable>
						<panel:editable condition="false">
							<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
						</panel:editable>
						</oc:authenticate>
						<oc:authenticate right="<%=Right.ADMINISTRATION_ROLE_READ.getLabel()%>">
							<panel:image property="doJumpRole" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/>
						</oc:authenticate>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="usergroup" errorStyleId="error"><bean:message key="entity.system.usergroup"/></common:label>
					</td>
					<td colspan="2">
						<div style="margin-top: 5px;">
							<table class="inner">
								<oc:authenticate right="<%=Right.ADMINISTRATION_USERMANAGEMENT_WRITE.getLabel()%>">
								<tr>
									<td colspan="2" class="action">
										<panel:editable condition="true">
										<logic:present name="panel_entity" property="profile.role" scope="page">
										<logic:equal name="panel_isLdapUser" scope="page" value="false">
										<html:image property="doAddUsergroup" pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text"/>
										</logic:equal>
										<logic:equal name="panel_isLdapUser" scope="page" value="true">
										<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
										</logic:equal>
										</logic:present>
										<logic:notPresent name="panel_entity" property="profile.role" scope="page">
										<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
										</logic:notPresent>
										</panel:editable>
										<panel:editable condition="false">
										<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
										</panel:editable>
									</td>
								</tr>
								</oc:authenticate>
								<logic:present name="panel_user" property="profile.usergroups" scope="page">
								<logic:iterate id="data" name="panel_user" property="profile.usergroups" scope="page" indexId="index">
								<tr>
									<td>
										<bean:write name="data" property="name" scope="page"/>
									</td>
									<td class="action">
										<oc:authenticate right="<%=Right.ADMINISTRATION_USERMANAGEMENT_WRITE.getLabel()%>"><%
										if(user.getProfile().getUsergroups().size() > 1) { %>
										<panel:editable condition="true">
										<logic:equal name="panel_isLdapUser" scope="page" value="false">
										<html:image property="<%="doRemoveUsergroup_"+TagUtility.lookupString(pageContext, "data", "id", "page")%>" pageKey="image.icon.delete.url" titleKey="image.icon.delete.text" altKey="image.icon.delete.text"/>
										</logic:equal>
										<logic:equal name="panel_isLdapUser" scope="page" value="true">
										<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
										</logic:equal>
										</panel:editable>
										<panel:editable condition="false">
										<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
										</panel:editable><% 
										} else { %>
										<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/><%
										} %>
										</oc:authenticate>
										<oc:authenticate right="<%=Right.ADMINISTRATION_USERGROUP_WRITE.getLabel()+","+Right.ADMINISTRATION_USERGROUP_READ.getLabel()%>">
										<oc:entityAccess name="data" scope="page" access="<%=EntityAccess.Access.READ %>">
										<panel:image property="<%="doJumpUsergroup_"+TagUtility.lookupString(pageContext, "data", "id", "page")%>" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/>
										</oc:entityAccess>
										<oc:notEntityAccess name="data" scope="page" access="<%=EntityAccess.Access.READ %>">
										<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
										</oc:notEntityAccess>
										</oc:authenticate>
									</td>
								</tr>
								</logic:iterate>
								</logic:present>
							</table>
						</div>
						<div style="margin: 10px 0px 5px 0px; font-weight: bold;">
							<common:label property="mainUsergroupId" errorStyleId="error"><bean:message key="entity.system.profile.defaultUsergroup"/></common:label>
						</div>
						<div>
							<html:select property="mainUsergroupId" styleId="mainUsergroupId" styleClass="full" disabled="<%=isLdapUser%>">
								<html:optionsCollection name="panel_user" property="profile.usergroups" label="name" value="id"/>
							</html:select>
						</div>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<table>
				<tr>
					<td class="label">
						<common:label property="validFrom" errorStyleId="error"><bean:message key="entity.system.profile.validFrom"/></common:label>
					</td>
					<td>
						<table style="width: 100%;"><tr><td style="width: 100%; padding: 0px 10px 0px 0px">
						<html:text property="validFrom" styleId="validFrom" styleClass="full" disabled="<%=isActiveUser||isLdapUser %>"/>
						</td><td style="padding: 0px;">
						<html:img styleId="chooseValidFrom" altKey="image.icon.chooseDate.text" titleKey="image.icon.chooseDate.text" pageKey="image.icon.chooseDate.url"/>
						<oc:jsCalendar input="validFrom" button="chooseValidFrom" formatKey="default.format.input.dateTime" disabled="<%=isActiveUser||isLdapUser %>"/>
						</td></tr></table>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="validUntil" errorStyleId="error"><bean:message key="entity.system.profile.validUntil"/></common:label>
					</td>
					<td>
						<table style="width: 100%;"><tr><td style="width: 100%; padding: 0px 10px 0px 0px">
						<html:text property="validUntil" styleId="validUntil" styleClass="full" disabled="<%=isActiveUser||isLdapUser %>"/>
						</td><td style="padding: 0px;">
						<html:img styleId="chooseValidUntil" altKey="image.icon.chooseDate.text" titleKey="image.icon.chooseDate.text" pageKey="image.icon.chooseDate.url"/>
						<oc:jsCalendar input="validUntil" button="chooseValidUntil" formatKey="default.format.input.dateTime" disabled="<%=isActiveUser||isLdapUser %>"/>
						</td></tr></table>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="locked" errorStyleId="error"><bean:message key="entity.system.profile.locked"/></common:label>
					</td>
					<td>
						<html:checkbox property="locked" styleId="locked" value="true" disabled="<%=isActiveUser||isLdapUser %>"/>
					</td>
				</tr>
			</table>
		</fieldset>		
		<fieldset class="unit">
			<table>					
				<tr>
					<td class="label">
						<common:label property="lastLogin" errorStyleId="error"><bean:message key="entity.system.user.lastLogin"/></common:label>
					</td>
					<td>
						<logic:present name="panel_lastValidLogin" scope="page">
							<bean:write name="panel_lastValidLogin" property="loginDate" scope="page" formatKey="default.format.date.datetime"/>
						</logic:present>
						<logic:notPresent name="panel_lastValidLogin" scope="page">
							<bean:message key="default.select.none"/>
						</logic:notPresent>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="failedLogins" errorStyleId="error"><bean:message key="entity.system.user.failedLogins"/></common:label>
					</td>
					<td>
						<logic:present name="panel_lastInvalidLogin" scope="page">
							<bean:write name="panel_invalidLoginCount" scope="page"/>
						</logic:present>
						<logic:notPresent name="panel_lastInvalidLogin" scope="page">
							0
						</logic:notPresent>
					</td>
				</tr>
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
				<tr>
					<td class="label">
						<common:label property="timeLock" errorStyleId="error"><bean:message key="entity.system.profile.timeLock"/></common:label>
					</td>
					<td>
						<html:text property="timeLock" styleId="timeLock" maxlength="255" styleClass="full" disabled="<%=isLdapUser%>"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="ipPattern" errorStyleId="error"><bean:message key="entity.system.profile.ipPattern"/></common:label>
					</td>
					<td>
						<html:text property="ipPattern" styleId="ipPattern" maxlength="255" styleClass="full" disabled="<%=isLdapUser%>"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
