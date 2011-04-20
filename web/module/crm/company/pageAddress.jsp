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

	<panel:edit focus="mainAddressName">
		<%-- Firmenadresse --%>
		<fieldset class="unit">
			<legend><common:label property="mainAddress" errorStyleId="error"><bean:message key="entity.crm.address.type.mainAddress"/></common:label></legend>
			<table>
				<tr>
					<td class="label"><common:label property="mainAddressName" errorStyleId="error"><bean:message key="entity.crm.address.name"/></common:label></td>
					<td><html:textarea property="mainAddressName" styleId="mainAddressName" styleClass="full"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="mainAddressStreet" errorStyleId="error"><bean:message key="entity.crm.address.street"/></common:label></td>
					<td><html:text property="mainAddressStreet" styleId="mainAddressStreet" maxlength="255" styleClass="full"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="mainAddressZip" errorStyleId="error"><bean:message key="entity.crm.address.zip"/></common:label></td>
					<td><html:text property="mainAddressZip" styleId="mainAddressZip" maxlength="5" styleClass="full"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="mainAddressCity" errorStyleId="error"><bean:message key="entity.crm.address.city"/></common:label></td>
					<td><html:text property="mainAddressCity" styleId="mainAddressCity" maxlength="255" styleClass="full"/></td>
				</tr>
			</table>
		</fieldset>
		
		<%-- Postadresse --%>
		<fieldset class="unit">
			<legend><common:label property="postalAddress" errorStyleId="error"><bean:message key="entity.crm.address.type.postalAddress"/></common:label></legend>
			<table>
				<tr>
					<td class="label"><common:label property="postalAddressName" errorStyleId="error"><bean:message key="entity.crm.address.name"/></common:label></td>
					<td><html:textarea property="postalAddressName" styleId="postalAddressName" styleClass="full"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="postalAddressStreet" errorStyleId="error"><bean:message key="entity.crm.address.street"/></common:label></td>
					<td><html:text property="postalAddressStreet" styleId="postalAddressStreet" maxlength="255" styleClass="full"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="postalAddressPostbox" errorStyleId="error"><bean:message key="entity.crm.address.postbox"/></common:label></td>
					<td><html:text property="postalAddressPostbox" styleId="postalAddressPostbox" maxlength="255" styleClass="full"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="postalAddressZip" errorStyleId="error"><bean:message key="entity.crm.address.zip"/></common:label></td>
					<td><html:text property="postalAddressZip" styleId="postalAddressZip" maxlength="5" styleClass="full"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="postalAddressCity" errorStyleId="error"><bean:message key="entity.crm.address.city"/></common:label></td>
					<td><html:text property="postalAddressCity" styleId="postalAddressCity" maxlength="255" styleClass="full"/></td>
				</tr>
			</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
