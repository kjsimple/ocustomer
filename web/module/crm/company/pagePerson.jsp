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
<%@ page import="org.opencustomer.framework.db.vo.EntityAccess" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<html:errors/>

	<panel:edit>
		<% boolean accessPersons = false; %>
		<oc:authenticate right="<%=Right.CRM_PERSONS_READ.getLabel()%>"><% accessPersons = true; %></oc:authenticate>
		<fieldset class="list">	
			<legend><bean:message key="module.crm.company.set.persons"/></legend>
			<table class="result">
				<colgroup>
					<col width="2*"/>
					<col width="*"/><% 
					if(accessPersons) { %>
					<col width="25"/><%
                    } %>
				</colgroup>
				<tr>
					<th><bean:message key="module.crm.person.name"/></th>
					<th><bean:message key="entity.crm.person.function"/></th><% 
					if(accessPersons) { %>
					<th class="action">
						<oc:authenticate right="<%=Right.CRM_PERSONS_WRITE.getLabel()%>">
						<html:link action="/crm/person/edit" titleKey="image.icon.add.text">
							<html:img styleClass="action" altKey="image.icon.add.text" titleKey="image.icon.add.text" pageKey="image.icon.add.url"/>
						</html:link>
						</oc:authenticate>
					</th><%
                    } %>
				</tr>
				<logic:present name="panel_persons" scope="page">
				<logic:iterate id="data" indexId="index" name="panel_persons" scope="page">
				<%
					PersonVO personVO = (PersonVO)pageContext.getAttribute("data");
				%>
				<tr class="<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>">
					<td>
						<bean:write name="data" property="title" scope="page"/>
						<bean:write name="data" property="firstName" scope="page"/> <bean:write name="data" property="lastName" scope="page"/>
						<bean:write name="data" property="nameAffix" scope="page"/>
					</td>
					<td><bean:write name="data" property="function" scope="page"/></td><% 
					if(accessPersons) { %>
					<td class="action">
						<oc:entityAccess name="data" scope="page" access="<%=EntityAccess.Access.READ %>">
						<panel:image property="<%="doJumpPerson_"+personVO.getId()%>" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/>
						</oc:entityAccess>
						<oc:notEntityAccess name="data" scope="page" access="<%=EntityAccess.Access.READ %>">
						<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
						</oc:notEntityAccess>
					</td><%
                    } %>
				</tr>
				</logic:iterate>
				</logic:present>
				<logic:notPresent name="index" scope="page">
				<tr class="noRow">
					<td colspan="<% if(accessPersons) { %>3<% } else { %>2<% } %>"><bean:message key="default.message.noEntries"/></td>
				</tr>
				</logic:notPresent>
			</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
