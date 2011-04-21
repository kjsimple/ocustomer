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
   - Thomas Bader (Bader & Jene Software-Ingenieurb�ro).
   - Portions created by the Initial Developer are Copyright (C) 2005
   - the Initial Developer. All Rights Reserved.
   -
   - Contributor(s):
   -   Thomas Bader <thomas.bader@bader-jene.de>
   -
   - ***** END LICENSE BLOCK ***** --%>
   
<%@ include file="/tiles/page.jsp" %>

<%@ page import="org.opencustomer.db.vo.crm.JobVO" %>
<%@ page import="org.opencustomer.framework.db.vo.EntityAccess" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>
<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<html:errors/>

	<panel:edit focus="subject">
			<fieldset class="unit">
				<table>
					<tr>
						<td class="label">
							<common:label property="subject" errorStyleId="error"><bean:message key="entity.crm.job.subject"/></common:label>
						</td>
						<td colspan="2">
							<html:text property="subject" styleId="subject" maxlength="255" styleClass="full"/>
						</td>
					</tr>
					<tr>
						<td class="label">
							<common:label property="reference" errorStyleId="error"><bean:message key="entity.crm.job.reference"/></common:label>
						</td>
						<td><%
							boolean hasReference = true;
						%>
							<logic:present name="panel_job" property="referencedPerson" scope="page">
							<bean:write name="panel_job" property="referencedPerson.firstName" scope="page"/> <bean:write name="panel_job" property="referencedPerson.lastName" scope="page"/> 
							<logic:present name="panel_job" property="referencedCompany" scope="page">
							(<bean:write name="panel_job" property="referencedCompany.companyName" scope="page"/>)
							</logic:present>
							</logic:present>
							<logic:notPresent name="panel_job" property="referencedPerson" scope="page">
							<logic:present name="panel_job" property="referencedCompany" scope="page">
							<bean:write name="panel_job" property="referencedCompany.companyName" scope="page"/>
							</logic:present>
							</logic:notPresent>
							<logic:notPresent name="panel_job" property="referencedPerson" scope="page">
							<logic:notPresent name="panel_job" property="referencedCompany" scope="page"><%
							hasReference = false;
							%>
							---
							</logic:notPresent>
							</logic:notPresent>
						</td>
						<td class="action">
							<%-- add person/company --%>
							<panel:editable condition="true"><%
							if(hasReference) {%>
							<html:image property="doRemoveReference" pageKey="image.icon.delete.url" titleKey="image.icon.delete.text" altKey="image.icon.delete.text"/><%
							} else {%>
							<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/><%
							}%>
							<html:image property="doAddReferencedPerson" pageKey="image.icon.addPerson.url" titleKey="image.icon.addPerson.text" altKey="image.icon.addPerson.text"/>
							<html:image property="doAddReferencedCompany" pageKey="image.icon.addCompany.url" titleKey="image.icon.addCompany.text" altKey="image.icon.addCompany.text"/>
							</panel:editable>
							<panel:editable condition="false">
							<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
							<html:img pageKey="image.icon.addPerson.url_grey" titleKey="image.icon.addCompany.text_grey" altKey="image.icon.addCompany.text_grey"/>
							<html:img pageKey="image.icon.addCompany.url_grey" titleKey="image.icon.addCompany.text_grey" altKey="image.icon.addCompany.text_grey"/>
							</panel:editable>
							<%-- jump for person --%>
							<logic:present name="panel_job" property="referencedPerson" scope="page">
							<oc:authenticate right="<%=Right.CRM_PERSONS_READ.getLabel()%>">
							<oc:entityAccess name="panel_job" property="referencedPerson" scope="page" access="<%=EntityAccess.Access.READ %>">
							<panel:image property="doJumpReference" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/>
							</oc:entityAccess>
							<oc:notEntityAccess name="panel_job" property="referencedPerson" scope="page" access="<%=EntityAccess.Access.READ %>">
							<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
							</oc:notEntityAccess>
							</oc:authenticate>
							</logic:present>
							<%-- jump for company --%>
							<logic:notPresent name="panel_job" property="referencedPerson" scope="page">
							<logic:present name="panel_job" property="referencedCompany" scope="page">
							<oc:authenticate right="<%=Right.CRM_COMPANIES_READ.getLabel()%>">
							<oc:entityAccess name="panel_job" property="referencedCompany" scope="page" access="<%=EntityAccess.Access.READ %>">
							<panel:image property="doJumpReference" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/>
							</oc:entityAccess>
							<oc:notEntityAccess name="panel_job" property="referencedCompany" scope="page" access="<%=EntityAccess.Access.READ %>">
							<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
							</oc:notEntityAccess>
							</oc:authenticate>
							</logic:present>
							</logic:notPresent>
							<%-- inactive jump --%>
							<logic:notPresent name="panel_job" property="referencedPerson" scope="page">
							<logic:notPresent name="panel_job" property="referencedCompany" scope="page">
							<oc:authenticate right='<%=Right.CRM_COMPANIES_READ.getLabel()+","+Right.CRM_PERSONS_READ.getLabel()%>'>
							<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
							</oc:authenticate>
							</logic:notPresent>
							</logic:notPresent>
						</td>
					</tr>
					<tr>
						<td class="label">
							<common:label property="status" errorStyleId="error"><bean:message key="entity.crm.job.status"/></common:label>
						</td>
						<td colspan="2">
							<html:select property="status" styleId="status">
								<html:option key="entity.crm.job.status.planned" value="<%=JobVO.Status.PLANNED.toString()%>"/>
								<html:option key="entity.crm.job.status.inProgress" value="<%=JobVO.Status.IN_PROGRESS.toString()%>"/>
								<html:option key="entity.crm.job.status.completed" value="<%=JobVO.Status.COMPLETED.toString()%>"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="label">
							<common:label property="priority" errorStyleId="error"><bean:message key="entity.crm.job.priority"/></common:label>
						</td>
						<td colspan="2">
							<html:select property="priority" styleId="priority">
								<html:option key="entity.crm.job.priority.high" value="<%=JobVO.Priority.HIGH.toString()%>"/>
								<html:option key="entity.crm.job.priority.medium" value="<%=JobVO.Priority.MEDIUM.toString()%>"/>
								<html:option key="entity.crm.job.priority.low" value="<%=JobVO.Priority.LOW.toString()%>"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="label">
							<common:label property="info" errorStyleId="error"><bean:message key="entity.crm.job.info"/></common:label>
						</td>
						<td colspan="2">
							<html:textarea property="info" styleId="info" style="height: 250px;" styleClass="full"/>
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset class="unit">
				<legend><bean:message key="module.crm.job.processing"/></legend>
				<table>
					<tr>
						<td class="label">
							<common:label property="subject" errorStyleId="error"><bean:message key="entity.crm.job.assignedUser"/></common:label>
						</td>
						<td>
							<bean:write name="panel_job" property="assignedUser.userName" scope="page"/>
						</td>
						<td class="action">
							<oc:authenticate right="<%=Right.CRM_JOBS_ASSIGNUSER.getLabel()%>">
							<panel:editable condition="true">
							<html:image property="doAddUser" pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text"/>
							</panel:editable>
							<panel:editable condition="false">
							<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
							</panel:editable>		
							</oc:authenticate>
						</td>
					</tr>
					<tr>
						<td class="label">
							<common:label property="dueDate" errorStyleId="error"><bean:message key="entity.crm.job.dueDate"/></common:label>
						</td>
						<td colspan="2">
							<table style="width: 100%;"><tr><td style="width: 100%; padding: 0px 10px 0px 0px">
							<html:text property="dueDate" styleId="dueDate" styleClass="full"/>
							</td><td style="padding: 0px;">
							<html:img styleId="chooseDate" altKey="image.icon.chooseDate.text" titleKey="image.icon.chooseDate.text" pageKey="image.icon.chooseDate.url"/>
							<oc:jsCalendar input="dueDate" button="chooseDate" formatKey="default.format.input.dateTime"/>
							</td></tr></table>
						</td>
					</tr>
				</table>
			</fieldset>
		</panel:edit>
	
	</tiles:put>
</tiles:insert>
