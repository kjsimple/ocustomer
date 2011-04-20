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

<%@ page import="org.opencustomer.framework.webapp.util.TagUtility" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>
<%@ page import="org.opencustomer.db.vo.system.UserVO" %>
<%@ page import="org.opencustomer.framework.db.vo.EntityAccess" %>
<%@ page import="org.opencustomer.webapp.Globals" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<html:errors/>

	<panel:edit focus="name">
		
		<fieldset class="unit">
			<legend><bean:message key="default.block.common"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="name" errorStyleId="error"><bean:message key="entity.system.role.name"/></common:label>
					</td>
					<td colspan="2">
						<html:text property="name" styleId="name" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<logic:equal name="<%=Globals.USER_KEY%>" property="profile.role.admin" scope="session" value="true">
				<tr>
					<td class="label">
						<common:label property="admin" errorStyleId="error"><bean:message key="entity.system.role.admin"/></common:label>
					</td>
					<td colspan="2">
						<logic:present name="panel_entity" property="id" scope="page">
						<logic:equal name="panel_entity" property="admin" scope="page" value="true">
						<bean:message key="default.boolean.true"/>
						</logic:equal>
						<logic:equal name="panel_entity" property="admin" scope="page" value="false">
						<bean:message key="default.boolean.false"/>
						</logic:equal>
						</logic:present>
						<logic:notPresent name="panel_entity" property="id" scope="page">
						<html:checkbox property="admin" styleId="admin"/>
						</logic:notPresent>
					</td>
				</tr>
				</logic:equal>
				<tr>
					<td class="label">
						<common:label property="defaultUsergroup" errorStyleId="error"><bean:message key="entity.system.role.defaultUsergroup"/></common:label>
					</td>
					<td>
						<logic:present name="panel_entity" property="defaultUsergroup" scope="page">
						<bean:write name="panel_entity" property="defaultUsergroup.name" scope="page"/>
						</logic:present>
						<logic:notPresent name="panel_entity" property="defaultUsergroup" scope="page">
						<bean:message key="default.select.none"/>
						</logic:notPresent>
					</td>
					<td class="action">
						<oc:authenticate right="<%=Right.ADMINISTRATION_ROLE_WRITE.getLabel()%>">
						<panel:editable condition="true">
						<html:image property="doAddUsergroup" pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text"/>
						</panel:editable>
						<panel:editable condition="false">
						<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
						</panel:editable>
						</oc:authenticate>
						
						<oc:authenticate right="<%=Right.ADMINISTRATION_USERGROUP_READ.getLabel()%>">
						<oc:entityAccess name="panel_entity" property="defaultUsergroup" scope="page" access="<%=EntityAccess.Access.READ %>">
						<panel:image property="doJumpUsergroup" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/>
						</oc:entityAccess>
						<oc:notEntityAccess name="panel_entity" property="defaultUsergroup" scope="page" access="<%=EntityAccess.Access.READ %>">
						<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
						</oc:notEntityAccess>
						</oc:authenticate>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="list">	
			<legend><bean:message key="entity.system.user"/></legend>
			<table class="result">
				<colgroup>
					<col width="1*"/>
					<oc:authenticate right="<%=Right.ADMINISTRATION_USERMANAGEMENT_READ.getLabel()+","+Right.ADMINISTRATION_USERMANAGEMENT_WRITE.getLabel()%>">
					<col width="25"/>
					</oc:authenticate>
				</colgroup>
				<tr>
					<th><bean:message key="entity.system.user"/></th>
					<oc:authenticate right="<%=Right.ADMINISTRATION_USERMANAGEMENT_READ.getLabel()+","+Right.ADMINISTRATION_USERMANAGEMENT_WRITE.getLabel()%>">
					<th>&nbsp;</th>
					</oc:authenticate>
				</tr>
				<logic:iterate id="data" indexId="index" name="panel_users" scope="page">
				<%
					UserVO userVO = (UserVO)pageContext.getAttribute("data");
				%>
				<tr class="<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>">
					<td><bean:write name="data" property="userName" scope="page"/></td>
					<oc:authenticate right="<%=Right.ADMINISTRATION_USERMANAGEMENT_READ.getLabel()+","+Right.ADMINISTRATION_USERMANAGEMENT_WRITE.getLabel()%>">
					<td>
						<oc:entityAccess name="data" scope="page" access="<%=EntityAccess.Access.READ %>">
						<panel:image property="<%="doJumpUser_"+userVO.getId()%>" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/>
						</oc:entityAccess>
						<oc:notEntityAccess name="data" scope="page" access="<%=EntityAccess.Access.READ %>">
						<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
						</oc:notEntityAccess>
					</td>
					</oc:authenticate>
				</tr>
				</logic:iterate>
				<logic:notPresent name="index" scope="page">
				<tr class="noRow">
					<td colspan="2"><bean:message key="default.message.noEntries"/></td>
				</tr>
				</logic:notPresent>
			</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
