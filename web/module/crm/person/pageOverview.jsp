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

<%@ page import="org.opencustomer.webapp.auth.Right" %>
<%@ page import="org.opencustomer.framework.db.vo.EntityAccess" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<html:errors/>
		
	<panel:edit>
		<fieldset class="unit">
			<legend><bean:message key="module.crm.company.set.communication"/></legend>
			<table>
				<tr>
					<td class="label"><bean:message key="entity.crm.person.phone"/></td>
					<td><bean:write name="panel_person" property="phone" scope="page"/></td>
				</tr>
				<tr>
					<td class="label"><bean:message key="entity.crm.person.mobile"/></td>
					<td><bean:write name="panel_person" property="mobile" scope="page"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="fax" errorStyleId="error"><bean:message key="entity.crm.person.fax"/></common:label></td>
					<td><bean:write name="panel_person" property="fax" scope="page"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="email" errorStyleId="error"><bean:message key="entity.crm.person.email"/></common:label></td>
					<td><a href="mailto:<bean:write name="panel_person" property="email" scope="page"/>"><bean:write name="panel_person" property="email" scope="page"/></a></td>
				</tr>
				<tr>
					<td class="label"><common:label property="url" errorStyleId="error"><bean:message key="entity.crm.person.url"/></common:label></td>
					<td>
						<logic:present name="panel_person" property="url" scope="page">
						<a href="http://<bean:write name="panel_person" property="url" scope="page"/>" target="_blank"><bean:write name="panel_person" property="url" scope="page"/></a>
						</logic:present>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.crm.person.block.company"/></legend>
			<table>
				<tr>
					<td class="label"><bean:message key="entity.crm.company.companyName"/></td>
					<td>
						<logic:present name="panel_person" property="company" scope="page">
						<bean:write name="panel_person" property="company.companyName" scope="page"/>
						</logic:present>
						<logic:notPresent name="panel_person" property="company" scope="page">
						---
						</logic:notPresent>
					</td>
					<td class="action">						
						<oc:authenticate right="<%=Right.CRM_COMPANIES_READ.getLabel()%>">
						<logic:present name="panel_person" property="company" scope="page">
						<oc:entityAccess name="panel_person" property="company" scope="page" access="<%=EntityAccess.Access.READ %>">
						<panel:image property="doJumpCompany" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/>
						</oc:entityAccess>
						<oc:notEntityAccess name="panel_person" property="company" scope="page" access="<%=EntityAccess.Access.READ %>">
						<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
						</oc:notEntityAccess>
						</logic:present>
						<logic:notPresent name="panel_person" property="company" scope="page">
						<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
						</logic:notPresent>
						</oc:authenticate>
					</td>
				</tr>
				<tr>
					<td class="label"><bean:message key="entity.crm.person.function"/></td>
					<td colspan="2"><bean:write name="panel_person" property="function" scope="page"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<logic:present name="panel_mainAddress" scope="page"><% pageContext.setAttribute("showAddresses", Boolean.TRUE);   %></logic:present>
		<logic:present name="panel_postalAddress" scope="page"><% pageContext.setAttribute("showAddresses", Boolean.TRUE); %></logic:present>
		<logic:present name="showAddresses" scope="page">
		<table style="width: 100%">
			<colgroup>
				<col width="50%"/>
				<col width="50%"/>
			</colgroup>
			<tr>
				<td style="vertical-align: top;">
					<logic:present name="panel_mainAddress" scope="page">
					<fieldset class="unit">
						<legend><bean:message key="entity.crm.address.type.mainAddress"/><logic:present name="panel_mainAddressCompany" scope="page"> (<bean:message key="entity.crm.company"/>)</logic:present></legend>
						<table>
							<tr><td>
								<common:format><bean:write name="panel_mainAddress" property="name" scope="page"/></common:format>
							</td></tr>
							<logic:present name="panel_mainAddress" property="street" scope="page">
							<tr><td>
								<bean:write name="panel_mainAddress" property="street" scope="page"/>
							</td></tr>
							</logic:present>
							<logic:present name="panel_mainAddress" property="postbox" scope="page">
							<tr><td>
								<bean:message key="entity.crm.address.postbox"/> <bean:write name="panel_mainAddress" property="postbox" scope="page"/>
							</td></tr>
							</logic:present>
							<tr><td>
							<bean:write name="panel_mainAddress" property="zip" scope="page"/> <bean:write name="panel_mainAddress" property="city" scope="page"/>
							</td></tr>
						</table>
					</fieldset>
					</logic:present>
				</td>
				<td style="vertical-align: top;">
					<logic:present name="panel_postalAddress" scope="page">
					<fieldset class="unit">
						<legend><bean:message key="entity.crm.address.type.postalAddress"/><logic:present name="panel_postalAddressCompany" scope="page"> (<bean:message key="entity.crm.company"/>)</logic:present></legend>
						<table>
							<tr><td>
								<common:format><bean:write name="panel_postalAddress" property="name" scope="page"/></common:format>
							</td></tr>
							<logic:present name="panel_postalAddress" property="street" scope="page">
							<tr><td>
								<bean:write name="panel_postalAddress" property="street" scope="page"/>
							</td></tr>
							</logic:present>
							<logic:present name="panel_postalAddress" property="postbox" scope="page">
							<tr><td>
								<bean:message key="entity.crm.address.postbox"/> <bean:write name="panel_postalAddress" property="postbox" scope="page"/>
							</td></tr>
							</logic:present>
							<tr><td>
								<bean:write name="panel_postalAddress" property="zip" scope="page"/> <bean:write name="panel_postalAddress" property="city" scope="page"/>
							</td></tr>
						</table>
					</fieldset>
					</logic:present>
				</td>
			</tr>
		</table>
		</logic:present>
		<fieldset class="unit">
			<legend><bean:message key="entity.crm.company.comment"/></legend>
			<table>
				<tr>
					<td>
						<common:format><bean:write name="panel_person" property="comment" scope="page"/></common:format>
					</td>
				</tr>
			</table>					
		</fieldset>
		
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
