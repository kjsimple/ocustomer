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

<%@ page import="org.opencustomer.framework.db.vo.EntityAccess" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">
	
	<html:errors/>
	
	<panel:edit>
	
		<fieldset class="unit">
			<legend><bean:message key="module.generic.pageSystem.entityAccess"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="globalAccess" errorStyleId="error"><bean:message key="entity.entityAccess.globalAccess"/></common:label>
					</td>
					<td>
						<html:select property="globalAccess" styleId="globalAccess" styleClass="full">
							<html:option key="entity.entityAccess.access.none" value="<%=EntityAccess.Access.NONE.toString()%>"/>
							<html:option key="entity.entityAccess.access.read" value="<%=EntityAccess.Access.READ.toString()%>"/>
							<html:option key="entity.entityAccess.access.write" value="<%=EntityAccess.Access.WRITE.toString()%>"/>
							<html:option key="entity.entityAccess.access.writeSystem" value="<%=EntityAccess.Access.WRITE_SYSTEM.toString()%>"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="group" errorStyleId="error"><bean:message key="entity.entityAccess.groupOwner"/></common:label>
					</td>
					<td>
						<html:select property="group" styleId="group" styleClass="full">
							<html:options collection="panel_system_usergroups" property="id" labelProperty="name"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="groupAccess" errorStyleId="error"><bean:message key="entity.entityAccess.groupAccess"/></common:label>
					</td>
					<td>
						<html:select property="groupAccess" styleId="groupAccess" styleClass="full">
							<html:option key="entity.entityAccess.access.none" value="<%=EntityAccess.Access.NONE.toString()%>"/>
							<html:option key="entity.entityAccess.access.read" value="<%=EntityAccess.Access.READ.toString()%>"/>
							<html:option key="entity.entityAccess.access.write" value="<%=EntityAccess.Access.WRITE.toString()%>"/>
							<html:option key="entity.entityAccess.access.writeSystem" value="<%=EntityAccess.Access.WRITE_SYSTEM.toString()%>"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="ownerUser" errorStyleId="error"><bean:message key="entity.entityAccess.userOwner"/></common:label>
					</td>
					<td>
						<bean:write name="panel_system_ownerUser" property="userName" scope="page"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="userAccess" errorStyleId="error"><bean:message key="entity.entityAccess.userAccess"/></common:label>
					</td>
					<td>
						<logic:equal name="panel_system_entityAccess" property="accessUser" scope="page" value="<%=EntityAccess.Access.NONE.toString()%>">
							<bean:message key="entity.entityAccess.access.none"/>
						</logic:equal>
						<logic:equal name="panel_system_entityAccess" property="accessUser" scope="page" value="<%=EntityAccess.Access.READ.toString()%>">
							<bean:message key="entity.entityAccess.access.read"/>
						</logic:equal>
						<logic:equal name="panel_system_entityAccess" property="accessUser" scope="page" value="<%=EntityAccess.Access.WRITE.toString()%>">
							<bean:message key="entity.entityAccess.access.write"/>
						</logic:equal>
						<logic:equal name="panel_system_entityAccess" property="accessUser" scope="page" value="<%=EntityAccess.Access.WRITE_SYSTEM.toString()%>">
							<bean:message key="entity.entityAccess.access.writeSystem"/>
						</logic:equal>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.generic.pageSystem.entityProperties"/></legend>
			<table>	
				<tr>
					<td class="label">
						<common:label property="id" errorStyleId="error"><bean:message key="entity.generic.base.id"/></common:label>
					</td>
					<td>
						<bean:write name="panel_entity" property="id" scope="page"/>
					</td>
				</tr>
				<logic:present name="panel_system_isStamped" scope="page">
				<tr>
					<td class="label">
						<common:label property="createUser" errorStyleId="error"><bean:message key="entity.generic.stamped.createUser"/></common:label>
					</td>
					<td>
						<logic:present name="panel_system_createUser">
						<bean:write name="panel_system_createUser" property="userName" scope="page"/>
						</logic:present>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="createDate" errorStyleId="error"><bean:message key="entity.generic.stamped.createDate"/></common:label>
					</td>
					<td>
						<bean:write name="panel_entity" property="createDate" scope="page" formatKey="default.format.date.datetime"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="modifyUser" errorStyleId="error"><bean:message key="entity.generic.stamped.modifyUser"/></common:label>
					</td>
					<td>
						<logic:present name="panel_system_modifyUser">
						<bean:write name="panel_system_modifyUser" property="userName" scope="page"/>
						</logic:present>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="modifyDate" errorStyleId="error"><bean:message key="entity.generic.stamped.modifyDate"/></common:label>
					</td>
					<td>
						<bean:write name="panel_entity" property="modifyDate" scope="page" formatKey="default.format.date.datetime"/>
					</td>
				</tr>
				</logic:present>
				<logic:present name="panel_system_isVersioned" scope="page">
				<tr>
					<td class="label">
						<common:label property="id" errorStyleId="error"><bean:message key="entity.generic.versioned.version"/></common:label>
					</td>
					<td>
						<bean:write name="panel_entity" property="version" scope="page"/>
					</td>
				</tr>
				</logic:present>
			</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
