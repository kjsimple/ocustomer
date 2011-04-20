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

	<panel:edit focus="globalAccess">

		<fieldset class="unit">
			<legend><bean:message key="module.generic.pageSystem.entityAccess"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="globalAccess" errorStyleId="error"><bean:message key="entity.entityAccess.globalAccess"/></common:label>
					</td>
					<td>
						<html:select property="globalAccess" styleId="globalAccess" styleClass="full">
							<html:option key="entity.entityAccess.access.none" value="<%=String.valueOf(EntityAccess.Access.NONE)%>"/>
							<html:option key="entity.entityAccess.access.read" value="<%=String.valueOf(EntityAccess.Access.READ)%>"/>
							<html:option key="entity.entityAccess.access.write" value="<%=String.valueOf(EntityAccess.Access.WRITE)%>"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="group" errorStyleId="error"><bean:message key="entity.entityAccess.groupOwner"/></common:label>
					</td>
					<td>
						<html:select property="group" styleId="group" styleClass="full">
							<html:options collection="panel_usergroups" property="id" labelProperty="name"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="groupAccess" errorStyleId="error"><bean:message key="entity.entityAccess.groupAccess"/></common:label>
					</td>
					<td>
						<html:select property="groupAccess" styleId="groupAccess" styleClass="full">
							<html:option key="entity.entityAccess.access.none" value="<%=String.valueOf(EntityAccess.Access.NONE)%>"/>
							<html:option key="entity.entityAccess.access.read" value="<%=String.valueOf(EntityAccess.Access.READ)%>"/>
							<html:option key="entity.entityAccess.access.write" value="<%=String.valueOf(EntityAccess.Access.WRITE)%>"/>
						</html:select>
					</td>
				</tr>
			</table>
		</fieldset>
	</panel:edit>	
	</tiles:put>
</tiles:insert>
