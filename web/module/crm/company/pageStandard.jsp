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

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<html:errors/>
		
	<panel:edit focus="companyName">
		<fieldset class="unit">
			<legend><bean:message key="module.crm.company.set.common"/></legend>
			<table>
				<tr>
					<td class="label"><common:label property="companyName" errorStyleId="error"><bean:message key="entity.crm.company.companyName"/></common:label></td>
					<td colspan="2"><html:text property="companyName" styleId="companyName" maxlength="255" styleClass="full"/></td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="subject" errorStyleId="error"><bean:message key="entity.crm.company.assignedUser"/></common:label>
					</td>
					<td>
						<logic:present name="panel_company" property="assignedUser" scope="page">
						<bean:write name="panel_company" property="assignedUser.userName" scope="page"/>
						</logic:present>
						<logic:notPresent name="panel_company" property="assignedUser" scope="page">
						---
						</logic:notPresent>
					</td>
					<td class="action">
						<panel:editable condition="true">
						<logic:present name="panel_company" property="assignedUser" scope="page">
						<html:image property="doRemoveUser" pageKey="image.icon.delete.url" titleKey="image.icon.delete.text" altKey="image.icon.delete.text"/>
						</logic:present>
						<logic:notPresent name="panel_company" property="assignedUser" scope="page">
						<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
						</logic:notPresent>
						<html:image property="doAddUser" pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text"/>
						</panel:editable>
						<panel:editable condition="false">
						<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
						<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
						</panel:editable>		
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.crm.company.set.communication"/></legend>
			<table>
				<tr>
					<td class="label"><common:label property="phone" errorStyleId="error"><bean:message key="entity.crm.company.phone"/></common:label></td>
					<td><html:text property="phone" styleId="phone" maxlength="50" styleClass="full"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="fax" errorStyleId="error"><bean:message key="entity.crm.company.fax"/></common:label></td>
					<td><html:text property="fax" styleId="fax" maxlength="50" styleClass="full"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="email" errorStyleId="error"><bean:message key="entity.crm.company.email"/></common:label></td>
					<td><html:text property="email" styleId="email" maxlength="255" styleClass="full"/></td>
				</tr>
				<tr>
					<td class="label"><common:label property="url" errorStyleId="error"><bean:message key="entity.crm.company.url"/></common:label></td>
					<td>
						<table style="width: 100%;"><tr><td style="width: 100%; padding: 0px 10px 0px 0px">
						<html:text property="url" styleId="url" maxlength="255" styleClass="full"/>
						</td><td style="padding: 0px;">
						<html:img altKey="image.icon.externalLink.text" titleKey="image.icon.externalLink.text" pageKey="image.icon.externalLink.url" onclick="window.open('http://'+document.getElementById('url').value, '_blank')"/>
						</td></tr></table>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.crm.company.set.classification1"/></legend>
			<table>
				<tr>
					<td class="label"><common:label property="companyStateId" errorStyleId="error"><bean:message key="entity.crm.companyState"/></common:label></td>
					<td>
						<html:select property="companyStateId" styleId="companyStateId" styleClass="full">
							<html:option value="0"><bean:message key="default.select.none"/></html:option>
							<logic:iterate id="data" name="panel_companyStates" scope="page">
								<logic:present name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:message name="data" property="nameKey" scope="page"/></html:option>
								</logic:present>
								<logic:notPresent name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:write name="data" property="name" scope="page"/></html:option>
								</logic:notPresent>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label"><common:label property="categoryId" errorStyleId="error"><bean:message key="entity.crm.category"/></common:label></td>
					<td>
						<html:select property="categoryId" styleId="categoryId" styleClass="full">
							<html:option value="0"><bean:message key="default.select.none"/></html:option>
							<logic:iterate id="data" name="panel_categories" scope="page">
								<logic:present name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:message name="data" property="nameKey" scope="page"/></html:option>
								</logic:present>
								<logic:notPresent name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:write name="data" property="name" scope="page"/></html:option>
								</logic:notPresent>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label"><common:label property="ratingId" errorStyleId="error"><bean:message key="entity.crm.rating"/></common:label></td>
					<td>
						<html:select property="ratingId" styleId="ratingId" styleClass="full">
							<html:option value="0"><bean:message key="default.select.none"/></html:option>
							<logic:iterate id="data" name="panel_ratings" scope="page">
								<logic:present name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:write name="data" property="rating" scope="page"/> - <bean:message name="data" property="nameKey" scope="page"/></html:option>
								</logic:present>
								<logic:notPresent name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:write name="data" property="rating" scope="page"/> - <bean:write name="data" property="name" scope="page"/></html:option>
								</logic:notPresent>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.crm.company.set.classification2"/></legend>
			<table>
				<tr>
					<td class="label"><common:label property="companyTypeId" errorStyleId="error"><bean:message key="entity.crm.companyType"/></common:label></td>
					<td>
						<html:select property="companyTypeId" styleId="companyTypeId" styleClass="full">
							<html:option value="0"><bean:message key="default.select.none"/></html:option>
							<logic:iterate id="data" name="panel_companyTypes" scope="page">
								<logic:present name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:message name="data" property="nameKey" scope="page"/></html:option>
								</logic:present>
								<logic:notPresent name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:write name="data" property="name" scope="page"/></html:option>
								</logic:notPresent>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label"><common:label property="sectorId" errorStyleId="error"><bean:message key="entity.crm.sector"/></common:label></td>
					<td>
						<html:select property="sectorId" styleId="sectorId" styleClass="full">
							<html:option value="0"><bean:message key="default.select.none"/></html:option>
							<logic:iterate id="data" name="panel_sectors" scope="page">
								<logic:present name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:message name="data" property="nameKey" scope="page"/></html:option>
								</logic:present>
								<logic:notPresent name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:write name="data" property="name" scope="page"/></html:option>
								</logic:notPresent>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label"><common:label property="legalFormId" errorStyleId="error"><bean:message key="entity.crm.legalForm"/></common:label></td>
					<td>
						<html:select property="legalFormId" styleId="legalFormId" styleClass="full">
							<html:option value="0"><bean:message key="default.select.none"/></html:option>
							<logic:iterate id="legalFormGroup" name="panel_legalFormGroups" scope="page">
							<logic:present name="legalFormGroup" property="nameKey" scope="page">
							<optgroup label="<bean:message name="legalFormGroup" property="nameKey" scope="page"/>">
							</logic:present>
							<logic:notPresent name="legalFormGroup" property="nameKey" scope="page">
							<optgroup label="<bean:write name="legalFormGroup" property="name" scope="page"/>">
							</logic:notPresent>
							<bean:define id="legalForms" name="legalFormGroup" property="legalForms" scope="page"/>
							<logic:iterate id="data" name="legalForms" scope="page">
								<logic:present name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:message name="data" property="nameKey" scope="page"/></html:option>
								</logic:present>
								<logic:notPresent name="data" property="nameKey" scope="page">
								<html:option value="<%=TagUtility.lookupString(pageContext, "data", "id", "page")%>"><bean:write name="data" property="name" scope="page"/></html:option>
								</logic:notPresent>
							</logic:iterate>
							</optgroup>
							</logic:iterate>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label"><common:label property="staffCount" errorStyleId="error"><bean:message key="entity.crm.company.staffCount"/></common:label></td>
					<td><html:text property="staffCount" styleId="staffCount" maxlength="5" styleClass="full"/></td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><common:label property="comment" errorStyleId="error"><bean:message key="entity.crm.company.comment"/></common:label></legend>
			<table>
				<tr>
					<td>
						<html:textarea property="comment" styleId="comment" styleClass="full"/>
					</td>
				</tr>
			</table>					
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
