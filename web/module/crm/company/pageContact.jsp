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

<%@ page import="org.opencustomer.db.dao.crm.ContactDAO" %>
<%@ page import="org.opencustomer.db.vo.crm.ContactVO" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<script type="text/javascript">
	<!--
		function edit(i) {
			document.location="<%=response.encodeURL(request.getContextPath()+"/crm/contact/edit.do?id=")%>"+i;
		}
	//-->
	</script>
		
	<html:errors/>

	<panel:edit>
		<fieldset class="search">
			<legend><bean:message key="default.block.search"/></legend>
				<table class="search" style="width: 100%;">
					<colgroup>
						<col width="25"/>
						<col width="25"/>
						<col width="125"/>
						<col/>
						<col width="150"/>
					</colgroup>
					<tr class="search">
						<td colspan="3">
							<common:label property="contactTimestamp" errorStyleId="error"><bean:message key="entity.crm.contact.contactTimestamp.date"/></common:label><br/>
							<table class="double"><tr>
								<td><html:text property="contactTimestampStart" styleId="contactTimestamp"/></td>
								<td class="divider">-</td>
								<td><html:text property="contactTimestampEnd"/></td>
							</tr></table>
						</td>
						<td>
							<common:label property="subject" errorStyleId="error"><bean:message key="entity.crm.contact.subject"/></common:label>
							<html:text property="subject" styleId="subject"/>
						</td>
						<td>
							<common:label property="name" errorStyleId="error"><bean:message key="module.crm.person.name"/></common:label>
							<html:text property="name" styleId="name"/>
						</td>
					</tr>
				</table>
				<div class="action">
					<html:image property="doSearch" pageKey="image.icon.search.url" styleClass="default"/>
					<html:image property="doResetSearch" pageKey="image.icon.searchClean.url" titleKey="image.icon.searchClean.text" altKey="image.icon.searchClean.text"/>
					<html:image property="doSearch" pageKey="image.icon.search.url" titleKey="image.icon.search.text" altKey="image.icon.search.text"/>
				</div>
				</fieldset>
				<oc:authenticate right="<%=Right.CRM_CONTACTS_WRITE.getLabel()%>">
				<fieldset class="listAction">
					<html:link action="/crm/contact/edit" titleKey="image.icon.add.text">
						<html:img styleClass="action" altKey="image.icon.add.text" titleKey="image.icon.add.text" pageKey="image.icon.add.url"/>
					</html:link>
				</fieldset>
				</oc:authenticate>
				<fieldset class="list">
				<legend>
					<common:paginationInfo 
						textKey="entity.crm.contact" 
						name="crm.company.edit.contactListScroll" 
						scope="request"/>
				</legend>
				<div class="pagination">
					<common:paginationMenu 
						name="crm.company.edit.contactListScroll" 
						scope="request"/>
				</div>
				<table class="result">
					<colgroup>
						<col width="25"/>
						<col width="25"/>
						<col width="125"/>
						<col/>
						<col width="150"/>
					</colgroup>
					<tr>
						<th>
							<bean:message key="entity.crm.contact.boundType"/>
						</th>
						<th>
							<bean:message key="entity.crm.contact.contactType"/>
						</th>
						<th>
							<common:sort
		  					 	messageKey="entity.crm.contact.contactTimestamp"
								property="<%=String.valueOf(ContactDAO.SORT_CONTACTTIMESTAMP)%>"/>
						</th>
						<th>
							<common:sort
		  					 	messageKey="entity.crm.contact.subject"
								property="<%=String.valueOf(ContactDAO.SORT_SUBJECT)%>"/>
						</th>
						<th>
							<bean:message key="module.crm.person.name"/>
						</th>
					</tr>
					<logic:present name="crm.company.edit.contactList" scope="request">
					<logic:iterate id="data" indexId="index" name="crm.company.edit.contactList" scope="request">
					<tr class="<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>" onmouseover="this.className='rowActive';" onmouseout="this.className='<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>'">
						<td onclick="edit(<bean:write name="data" property="id" scope="page"/>);">
							<logic:equal name="data" property="boundType" scope="page" value="<%=ContactVO.BoundType.IN.toString()%>">
								<html:img pageKey="image.entity.crm.contact.boundType.inbound.url" altKey="image.entity.crm.contact.boundType.inbound.text" titleKey="image.entity.crm.contact.boundType.inbound.text"/>
							</logic:equal>
							<logic:equal name="data" property="boundType" scope="page" value="<%=ContactVO.BoundType.OUT.toString()%>">
								<html:img pageKey="image.entity.crm.contact.boundType.outbound.url" altKey="image.entity.crm.contact.boundType.outbound.text" titleKey="image.entity.crm.contact.boundType.outbound.text"/>
							</logic:equal>
						</td>
						<td onclick="edit(<bean:write name="data" property="id" scope="page"/>);">
							<logic:equal name="data" property="contactType" scope="page" value="<%=ContactVO.ContactType.TELEPHONE.toString()%>">
								<html:img pageKey="image.entity.crm.contact.contactType.phone.url" altKey="image.entity.crm.contact.contactType.phone.text" titleKey="image.entity.crm.contact.contactType.phone.text"/>
							</logic:equal>
							<logic:equal name="data" property="contactType" scope="page" value="<%=ContactVO.ContactType.FAX.toString()%>">
								<html:img pageKey="image.entity.crm.contact.contactType.fax.url" altKey="image.entity.crm.contact.contactType.fax.text" titleKey="image.entity.crm.contact.contactType.fax.text"/>
							</logic:equal>
							<logic:equal name="data" property="contactType" scope="page" value="<%=ContactVO.ContactType.EMAIL.toString()%>">
								<html:img pageKey="image.entity.crm.contact.contactType.email.url" altKey="image.entity.crm.contact.contactType.email.text" titleKey="image.entity.crm.contact.contactType.email.text"/>
							</logic:equal>
							<logic:equal name="data" property="contactType" scope="page" value="<%=ContactVO.ContactType.LETTER.toString()%>">
								<html:img pageKey="image.entity.crm.contact.contactType.letter.url" altKey="image.entity.crm.contact.contactType.letter.text" titleKey="image.entity.crm.contact.contactType.letter.text"/>
							</logic:equal>
							<logic:equal name="data" property="contactType" scope="page" value="<%=ContactVO.ContactType.PERSONAL.toString()%>">
								<html:img pageKey="image.entity.crm.contact.contactType.personal.url" altKey="image.entity.crm.contact.contactType.personal.text" titleKey="image.entity.crm.contact.contactType.personal.text"/>
							</logic:equal>
						</td>
						<td onclick="edit(<bean:write name="data" property="id" scope="page"/>);">
							<bean:write name="data" property="contactTimestamp" scope="page" formatKey="default.format.date.datetime"/>
						</td>
						<td onclick="edit(<bean:write name="data" property="id" scope="page"/>);">
							<bean:write name="data" property="subject" scope="page"/>
						</td>
						<td onclick="edit(<bean:write name="data" property="id" scope="page"/>);">
							<logic:present name="data" property="personContacts" scope="page">
							<logic:iterate id="data2" name="data" property="personContacts" scope="page">					
							<bean:write name="data2" property="person.firstName" scope="page"/> <bean:write name="data2" property="person.lastName" scope="page"/><br/>
							</logic:iterate>
							</logic:present>
							<logic:present name="data" property="company" scope="page">
							<span class="company"><bean:write name="data" property="company.companyName" scope="page"/></span>
							</logic:present>
						</td>
					</tr>
					</logic:iterate>
					</logic:present>
					<logic:notPresent name="index" scope="page">
					<tr class="noRow">
						<td colspan="5"><bean:message key="default.message.noEntries"/></td>
					</tr>
					</logic:notPresent>
					
				</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
