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

<%@ page import="org.opencustomer.db.vo.crm.PersonVO" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>
<%@ page import="org.opencustomer.framework.db.vo.EntityAccess" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<html:errors/>

	<panel:edit focus="gender">
		<fieldset class="unit">
			<legend><bean:message key="module.crm.person.block.common"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="gender" errorStyleId="error"><bean:message key="entity.crm.person.gender"/></common:label>
					</td>
					<td>
						<html:select property="gender" styleId="gender">
							<html:option key="entity.crm.person.gender.unknown" value="<%=PersonVO.Gender.UNKNOWN.toString()%>"/>
							<html:option key="entity.crm.person.gender.male" value="<%=PersonVO.Gender.MALE.toString()%>"/>
							<html:option key="entity.crm.person.gender.female" value="<%=PersonVO.Gender.FEMALE.toString()%>"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="title" errorStyleId="error"><bean:message key="entity.crm.person.title"/></common:label>
					</td>
					<td>
						<html:text property="title" styleId="title" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="firstName" errorStyleId="error"><bean:message key="entity.crm.person.firstName"/></common:label>
					</td>
					<td>
						<html:text property="firstName" styleId="firstName" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="lastName" errorStyleId="error"><bean:message key="entity.crm.person.lastName"/></common:label>
					</td>
					<td>
						<html:text property="lastName" styleId="lastName" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="nameAffix" errorStyleId="error"><bean:message key="entity.crm.person.nameAffix"/></common:label>
					</td>
					<td>
						<html:text property="nameAffix" styleId="nameAffix" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="degree" errorStyleId="error"><bean:message key="entity.crm.person.degree"/></common:label>
					</td>
					<td>
						<html:text property="degree" styleId="degree" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="dayOfBirth" errorStyleId="error"><bean:message key="entity.crm.person.dayOfBirth"/></common:label>
					</td>
					<td>
						<table style="width: 100%;"><tr><td style="width: 100%; padding: 0px 10px 0px 0px">
						<html:text property="dayOfBirth" styleId="dayOfBirth" styleClass="full"/>
						</td><td style="padding: 0px;">
						<html:img styleId="chooseDate" altKey="image.icon.chooseDate.text" titleKey="image.icon.chooseDate.text" pageKey="image.icon.chooseDate.url"/>
						<oc:jsCalendar input="dayOfBirth" button="chooseDate" formatKey="default.format.input.date"/>
						</td></tr></table>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.crm.person.block.company"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="type" errorStyleId="error"><bean:message key="entity.crm.person.type"/></common:label>
					</td>
					<td colspan="2">
						<html:select property="type" styleId="type">
							<html:option value=""><bean:message key="default.select.none"/></html:option>
							<html:option key="entity.crm.person.type.businessman" value="<%=PersonVO.Type.BUSINESSMAN.toString()%>"/>
							<html:option key="entity.crm.person.type.employee" value="<%=PersonVO.Type.EMPLOYEE.toString()%>"/>
							<html:option key="entity.crm.person.type.freelancer" value="<%=PersonVO.Type.FREELANCER.toString()%>"/>
							<html:option key="entity.crm.person.type.individual" value="<%=PersonVO.Type.INDIVIDUAL.toString()%>"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="function" errorStyleId="error"><bean:message key="entity.crm.person.function"/></common:label>
					</td>
					<td colspan="2">
						<html:text property="function" styleId="function" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="phone" errorStyleId="error"><bean:message key="entity.crm.company.companyName"/></common:label>
					</td>
					<td>
						<logic:present name="panel_person" property="company" scope="page">
						<bean:write name="panel_person" property="company.companyName" scope="page"/>
						</logic:present>
						<logic:notPresent name="panel_person" property="company" scope="page">
						---
						</logic:notPresent>
					</td>
					<td class="action">
						<oc:authenticate right="<%=Right.CRM_PERSONS_WRITE.getLabel()%>">
						<logic:present name="panel_person" property="company" scope="page">
						<panel:editable condition="true">
						<html:image property="doRemoveCompany" pageKey="image.icon.delete.url" titleKey="image.icon.delete.text" altKey="image.icon.delete.text"/>
						</panel:editable>
						<panel:editable condition="false">
						<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
						</panel:editable>
						</logic:present>
						<logic:notPresent name="panel_person" property="company" scope="page">
						<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
						</logic:notPresent>
						
						<panel:editable condition="true">
						<html:image property="doAddCompany" pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text"/>
						</panel:editable>
						<panel:editable condition="false">
						<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
						</panel:editable>
						</oc:authenticate>
						
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
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.crm.person.block.communication"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="phone" errorStyleId="error"><bean:message key="entity.crm.person.phone"/></common:label>
					</td>
					<td>
						<html:text property="phone" styleId="phone" maxlength="50" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="mobile" errorStyleId="error"><bean:message key="entity.crm.person.mobile"/></common:label>
					</td>
					<td>
						<html:text property="mobile" styleId="mobile" maxlength="50" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="fax" errorStyleId="error"><bean:message key="entity.crm.person.fax"/></common:label>
					</td>
					<td>
						<html:text property="fax" styleId="fax" maxlength="50" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="email" errorStyleId="error"><bean:message key="entity.crm.person.email"/></common:label>
					</td>
					<td>
						<html:text property="email" styleId="email" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="url" errorStyleId="error"><bean:message key="entity.crm.person.url"/></common:label>
					</td>
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
			<legend><common:label property="comment" errorStyleId="error"><bean:message key="entity.crm.person.comment"/></common:label></legend>
			<table>
				<tr>
					<td>
						<html:textarea property="comment" styleId="comment"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
