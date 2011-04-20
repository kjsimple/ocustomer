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

<%@ page import="org.opencustomer.db.vo.system.ListConfigurationVO" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>

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
						<common:label property="type" errorStyleId="error"><bean:message key="entity.system.listConfiguration.type"/></common:label>
					</td>
					<td>
						<logic:equal name="panel_entity" property="type" scope="page" value="<%=ListConfigurationVO.Type.PERSON.toString()%>">
						<input type="text" value="<bean:message key="entity.crm.person"/>" disabled="disabled" class="full"/>
						</logic:equal>
						<logic:equal name="panel_entity" property="type" scope="page" value="<%=ListConfigurationVO.Type.COMPANY.toString()%>">
						<input type="text" value="<bean:message key="entity.crm.company"/>" disabled="disabled" class="full"/>
						</logic:equal>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="name" errorStyleId="error"><bean:message key="entity.system.listConfiguration.name"/></common:label>
					</td>
					<td>
						<html:text property="name" styleId="name" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<oc:authenticate right="<%=Right.ADMINISTRATION_LIST_GLOBALLIST.getLabel()%>">
				<tr>
					<td class="label">
						<common:label property="global" errorStyleId="error"><bean:message key="default.list.choose.optiongroup.global"/></common:label>
					</td>
					<td>
						<html:checkbox property="global" styleId="global"/>
					</td>
				</tr>
				</oc:authenticate>
				<tr>
					<td class="label">
						<common:label property="default" errorStyleId="error"><bean:message key="entity.system.listConfiguration.default"/></common:label>
					</td>
					<td>
						<html:checkbox property="default" styleId="default"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="description" errorStyleId="error"><bean:message key="entity.system.listConfiguration.description"/></common:label>
					</td>
					<td>
						<html:textarea property="description" styleId="description" styleClass="full"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</panel:edit>	
	</tiles:put>
</tiles:insert>
