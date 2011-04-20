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

<%@ page import="org.opencustomer.framework.webapp.util.TagUtility" %>   

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
					<td class="label"><bean:message key="entity.crm.company.phone"/></td>
					<td><bean:write name="panel_company" property="phone" scope="page"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="fax" errorStyleId="error"><bean:message key="entity.crm.company.fax"/></common:label></td>
					<td><bean:write name="panel_company" property="fax" scope="page"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="email" errorStyleId="error"><bean:message key="entity.crm.company.email"/></common:label></td>
					<td><a href="mailto:<bean:write name="panel_company" property="email" scope="page"/>"><bean:write name="panel_company" property="email" scope="page"/></a></td>
				</tr>
				<tr>
					<td class="label"><common:label property="url" errorStyleId="error"><bean:message key="entity.crm.company.url"/></common:label></td>
					<td>
						<logic:present name="panel_company" property="url" scope="page">
						<a href="http://<bean:write name="panel_company" property="url" scope="page"/>" target="_blank"><bean:write name="panel_company" property="url" scope="page"/></a>
						</logic:present>
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
						<legend><bean:message key="entity.crm.address.type.mainAddress"/></legend>
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
						<legend><bean:message key="entity.crm.address.type.postalAddress"/></legend>
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
						<common:format><bean:write name="panel_company" property="comment" scope="page"/></common:format>
					</td>
				</tr>
			</table>					
		</fieldset>
		
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
