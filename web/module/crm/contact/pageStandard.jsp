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

<%@ page import="org.opencustomer.webapp.Globals" %>
<%@ page import="org.opencustomer.db.vo.crm.ContactVO" %>
<%@ page import="org.opencustomer.db.vo.crm.CompanyVO" %>
<%@ page import="org.opencustomer.db.vo.crm.PersonContactVO" %>
<%@ page import="org.opencustomer.db.vo.crm.PersonVO" %>
<%@ page import="org.opencustomer.db.vo.system.UserVO" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>
<%@ page import="java.util.List" %>
<%@ page import="org.opencustomer.framework.webapp.panel.Panel" %>
<%@ page import="org.opencustomer.framework.webapp.panel.EditPanel" %>
<%@ page import="org.opencustomer.framework.webapp.util.MessageUtil" %>
<%@ page import="org.opencustomer.framework.db.vo.EntityAccess" %>
<%@ page import="org.opencustomer.framework.webapp.util.TagUtility" %>

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
							<common:label property="subject" errorStyleId="error"><bean:message key="entity.crm.contact.subject"/></common:label>
						</td>
						<td>
							<html:text property="subject" styleId="subject" maxlength="255" styleClass="full"/>
						</td>
					</tr>
					<tr>
						<td class="label">
							<common:label property="contactDate" errorStyleId="error"><bean:message key="entity.crm.contact.contactTimestamp"/></common:label>
						</td>
						<td>
							<table style="width: 100%;"><tr><td style="width: 100%; padding: 0px 10px 0px 0px">
							<html:text property="contactTime" styleId="contactTime" styleClass="full"/>
							</td><td style="padding: 0px;">
							<html:img styleId="chooseTime" altKey="image.icon.chooseDate.text" titleKey="image.icon.chooseDate.text" pageKey="image.icon.chooseDate.url"/>
							<oc:jsCalendar input="contactTime" button="chooseTime" formatKey="default.format.input.dateTime"/>
							</td></tr></table>
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset class="unit">
				<table>
					<tr>
						<td class="label">
							<common:label property="boundType" errorStyleId="error"><bean:message key="entity.crm.contact.boundType"/></common:label>
						</td>
						<td>
							<html:select property="boundType" styleId="boundType">
								<html:option value="">---</html:option>
								<html:option key="entity.crm.contact.boundType.in" value="<%=ContactVO.BoundType.IN.toString()%>"/>
								<html:option key="entity.crm.contact.boundType.out" value="<%=ContactVO.BoundType.OUT.toString()%>"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="label">
							<common:label property="contactType" errorStyleId="error"><bean:message key="entity.crm.contact.contactType"/></common:label>
						</td>
						<td>
							<html:select property="contactType" styleId="contactType">
								<html:option value="">---</html:option>
								<html:option key="entity.crm.contact.contactType.phone" value="<%=ContactVO.ContactType.TELEPHONE.toString()%>"/>
								<html:option key="entity.crm.contact.contactType.fax" value="<%=ContactVO.ContactType.FAX.toString()%>"/>
								<html:option key="entity.crm.contact.contactType.email" value="<%=ContactVO.ContactType.EMAIL.toString()%>"/>
								<html:option key="entity.crm.contact.contactType.letter" value="<%=ContactVO.ContactType.LETTER.toString()%>"/>
								<html:option key="entity.crm.contact.contactType.personal" value="<%=ContactVO.ContactType.PERSONAL.toString()%>"/>
							</html:select>
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset class="unit">
				<table><%
					EditPanel panel = (EditPanel)Panel.getPanelStack(request).peek();
					ContactVO contact = (ContactVO)panel.getEntity();
					%>
					<tr>
						<td class="label">
							<common:label property="person" errorStyleId="error"><bean:message key="entity.crm.person"/></common:label>
						</td>
						<td colspan="2">
							<table class="inner">
								<tr>
									<td colspan="2" class="action">
										<oc:authenticate right="<%=Right.CRM_CONTACTS_WRITE.getLabel()%>">
										<logic:present name="<%=Globals.USER_KEY%>" property="person" scope="session"><%
										PersonVO ownPerson = ((UserVO)session.getAttribute(Globals.USER_KEY)).getPerson();
			                            if(!contact.getPersonContacts().contains(new PersonContactVO(ownPerson, contact))) { %>
										<html:image property="doAddOwnPerson" pageKey="image.icon.addOwnPerson.url" titleKey="image.icon.addOwnPerson.text" altKey="image.icon.addOwnPerson.text"/><% 
										} else { %>
										<html:image property="doAddOwnPerson" pageKey="image.icon.addOwnPerson_grey.url" titleKey="image.icon.addOwnPerson_grey.text" altKey="image.icon.addOwnPerson_grey.text" disabled="true"/><% 
										} %>							
										</logic:present>
										<panel:editable condition="true">					
										<html:image property="doAddPerson" pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text"/>
										</panel:editable>
										<panel:editable condition="false">					
										<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
										</panel:editable>
										</oc:authenticate>
									</td>
								</tr>
								<logic:iterate id="data" name="panel_contact" property="personContacts" scope="page" indexId="index"><%
									PersonContactVO personContact = (PersonContactVO)pageContext.getAttribute("data");
									
									String message = null;
									CompanyVO personCompany = personContact.getPerson().getCompany();
									CompanyVO pcCompany = personContact.getCompany();
									if((personCompany == null && pcCompany != null)
									        || (personCompany != null && pcCompany == null)
									        || (personCompany != null && pcCompany != null && !personCompany.equals(pcCompany)))
									{ 
									    if(personCompany == null)
									        message = MessageUtil.message(request, "module.calendar.event.message.companyChanged.noCompany");
									    else
									        message = MessageUtil.message(request, "module.calendar.event.message.companyChanged.otherCompany", personCompany.getCompanyName());
									}
								%>
								<tr>
									<td <% if(message != null) { out.println("title=\""+message+"\""); } %>>
										<bean:write name="data" property="person.title" scope="page"/>
										<bean:write name="data" property="person.firstName" scope="page"/>
										<bean:write name="data" property="person.lastName" scope="page"/>
										<bean:write name="data" property="person.nameAffix" scope="page"/>
										<logic:present name="data" property="company" scope="page">
											[<bean:write name="data" property="company.companyName" scope="page"/>]
										</logic:present> 
										<% if(message != null) out.println("*"); %>
									</td>
									<td class="action">
										<oc:authenticate right="<%=Right.CRM_CONTACTS_WRITE.getLabel()%>"><%
										if(contact.getPersonContacts().size() > 1 || contact.getCompany() != null) { %>
										<panel:editable condition="true">
										<html:image property="<%="doRemovePerson_"+personContact.getPerson().getId()%>" pageKey="image.icon.delete.url" titleKey="image.icon.delete.text" altKey="image.icon.delete.text"/>
				   						</panel:editable>
										<panel:editable condition="false">
										<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
										</panel:editable><%
										} else {%>
										<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/><%
										} %>
										</oc:authenticate>
										<oc:entityAccess name="data" property="person" scope="page" access="<%=EntityAccess.Access.READ %>">
										<panel:image property="<%="doJumpPerson_"+TagUtility.lookupString(pageContext, "data", "person.id", "page")%>" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/>
										</oc:entityAccess>
										<oc:notEntityAccess name="data" property="person" scope="page" access="<%=EntityAccess.Access.READ %>">
										<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
										</oc:notEntityAccess>
									</td>
								</tr>
								</logic:iterate>
							</table>
						</td>
					</tr>
					<tr>
						<td class="label">
							<common:label property="company" errorStyleId="error"><bean:message key="entity.crm.company"/></common:label>
						</td>
						<td>
							<logic:present name="panel_contact" property="company" scope="page">
							<bean:write name="panel_contact" property="company.companyName" scope="page"/>
							</logic:present>
							<logic:notPresent name="panel_contact" property="company" scope="page">
							---
							</logic:notPresent>
						</td>
						<td class="action">
							<oc:authenticate right="<%=Right.CRM_CONTACTS_WRITE.getLabel()%>"><%
							    if(contact.getPersonContacts().size() > 0 && contact.getCompany() != null) {%>
								<panel:editable condition="true">
							    <html:image property="doRemoveCompany" pageKey="image.icon.delete.url" titleKey="image.icon.delete.text" altKey="image.icon.delete.text"/>
		   						</panel:editable>
								<panel:editable condition="false">
								<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
								</panel:editable><%
							    } else { %>
							    <html:image property="doRemoveCompany" pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text" disabled="true"/><%
							    }%>
							
							<panel:editable condition="true">					
							<html:image property="doAddCompany" pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text"/>
							</panel:editable>
							<panel:editable condition="false">					
							<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
							</panel:editable>
							</oc:authenticate>
							<logic:present name="panel_contact" property="company" scope="page">
							<oc:entityAccess name="panel_contact" property="company" scope="page" access="<%=EntityAccess.Access.READ %>">
							<panel:image property="doJumpCompany" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/>
							</oc:entityAccess>
							<oc:notEntityAccess name="panel_contact" property="company" scope="page" access="<%=EntityAccess.Access.READ %>">
							<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
							</oc:notEntityAccess>
							</logic:present>
							<logic:notPresent name="panel_contact" property="company" scope="page">
							<html:img styleClass="action" pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
							</logic:notPresent>
						</td>
					</tr>
					<tr>
						<td class="label">
							<common:label property="contactName" errorStyleId="error"><bean:message key="entity.crm.contact.contactName"/></common:label>
						</td>
						<td colspan="2">
							<html:text property="contactName" styleId="contactName" maxlength="255" styleClass="full"/>
						</td>
					</tr>
				</table>
			</fieldset>		
			<fieldset class="unit">
				<legend>
					<common:label property="content" errorStyleId="error"><bean:message key="entity.crm.contact.content"/></common:label>
				</legend>
				<html:textarea property="content" styleId="content" style="height: 250px;"/>
			</fieldset>
		</panel:edit>
	
	</tiles:put>
</tiles:insert>
