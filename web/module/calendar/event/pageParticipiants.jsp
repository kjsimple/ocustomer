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

<%@ page import="org.opencustomer.webapp.auth.Right" %>
<%@ page import="org.opencustomer.framework.webapp.util.TagUtility" %>
<%@ page import="org.opencustomer.db.vo.calendar.EventCalendarVO" %>
<%@ page import="org.opencustomer.db.vo.calendar.EventPersonVO" %>
<%@ page import="org.opencustomer.framework.db.vo.EntityAccess" %>
<%@ page import="org.opencustomer.webapp.Globals" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<html:errors/>
	
	<panel:edit>
		<fieldset class="unit">
			<legend><bean:message key="module.calendar.event.block.users"/></legend>
			<table class="light">
				<colgroup>
					<col width="30"/>
					<col width="100"/>
					<col/>
					<col width="100"/>
					<col width="50"/>
				</colgroup>
				<tr>
					<th>&nbsp;</th>
					<th><bean:message key="entity.system.user"/></th>
					<th><bean:message key="entity.crm.person"/></th>
					<th>&nbsp;</th>
					<th style="text-align: right;">
						<oc:authenticate right="<%=Right.CALENDAR_CALENDAR_WRITE.getLabel()%>">	
						<panel:editable condition="true">	
						<html:image property="doAddUser" pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text"/>
						</panel:editable>
						<panel:editable condition="false">					
						<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
						</panel:editable>
						</oc:authenticate>
					</th>
				</tr>
				<logic:present name="panel_eventCalendars" scope="page">
				<logic:iterate id="data" name="panel_eventCalendars" scope="page" indexId="index"><%
					boolean changeStatus = false;
				%>
				<logic:notEqual name="data" property="participiantType" scope="page" value="<%=EventCalendarVO.ParticipiantType.HOST.toString()%>"> 
				<logic:equal name="data" property="calendar.id" scope="page" value="<%=TagUtility.lookup(pageContext, "panel_calendar", "id", "page").toString()%>">
				<oc:entityAccess name="data" scope="page" access="<%=EntityAccess.Access.WRITE%>">				
				<oc:entityAccess name="data" property="calendar" scope="page" access="<%=EntityAccess.Access.WRITE%>">
				<% changeStatus = true; %>
				</oc:entityAccess>
				</oc:entityAccess>
				</logic:equal>
				</logic:notEqual>
				<tr <logic:equal name="data" property="participiantType" scope="page" value="<%=EventCalendarVO.ParticipiantType.HOST.toString()%>"> style="font-style: italic"</logic:equal>>
					<td>
						<logic:equal name="data" property="invitationStatus" scope="page" value="<%=EventCalendarVO.InvitationStatus.NEW.toString()%>">
							<html:img pageKey="image.entity.calendar.invitationStatus.event.new.url" titleKey="image.entity.calendar.invitationStatus.event.new.text" altKey="image.entity.calendar.invitationStatus.event.new.text"/>
						</logic:equal>
						<logic:equal name="data" property="invitationStatus" scope="page" value="<%=EventCalendarVO.InvitationStatus.ACCEPTED.toString()%>">
							<html:img pageKey="image.entity.calendar.invitationStatus.event.accepted.url" titleKey="image.entity.calendar.invitationStatus.event.accepted.text" altKey="image.entity.calendar.invitationStatus.event.accepted.text"/>
						</logic:equal>
						<logic:equal name="data" property="invitationStatus" scope="page" value="<%=EventCalendarVO.InvitationStatus.REJECTED.toString()%>">
							<html:img pageKey="image.entity.calendar.invitationStatus.event.rejected.url" titleKey="image.entity.calendar.invitationStatus.event.rejected.text" altKey="image.entity.calendar.invitationStatus.event.rejected.text"/>
						</logic:equal>
						<logic:equal name="data" property="invitationStatus" scope="page" value="<%=EventCalendarVO.InvitationStatus.DELETED.toString()%>">
							<% if(changeStatus) { %>
							<html:img pageKey="image.entity.calendar.invitationStatus.event.deleted.url" titleKey="image.entity.calendar.invitationStatus.event.deleted.text" altKey="image.entity.calendar.invitationStatus.event.deleted.text"/>
							<% } else { %>
							<logic:equal name="data" property="calendar.deleted" scope="page" value="false">
							<html:img pageKey="image.entity.calendar.invitationStatus.event.rejected.url" titleKey="image.entity.calendar.invitationStatus.event.rejected.text" altKey="image.entity.calendar.invitationStatus.event.rejected.text"/>
							</logic:equal>
							<logic:equal name="data" property="calendar.deleted" scope="page" value="true">
							<html:img pageKey="image.entity.calendar.invitationStatus.event.deleted.url" titleKey="image.entity.calendar.invitationStatus.event.deleted.text" altKey="image.entity.calendar.invitationStatus.event.deleted.text"/>
							</logic:equal>
							<% } %>
						</logic:equal>
					</td>
					<td>
						<bean:write name="data" property="calendar.user.userName" scope="page"/>
					</td>
					<td<% if(!changeStatus) { %> colspan="2"<% } %>>
						<logic:present name="data" property="calendar.user">
						<logic:present name="data" property="calendar.user.person" scope="page">
							<bean:write name="data" property="calendar.user.person.title" scope="page"/>
							<bean:write name="data" property="calendar.user.person.firstName" scope="page"/> <bean:write name="data" property="calendar.user.person.lastName" scope="page"/>
							<bean:write name="data" property="calendar.user.person.nameAffix" scope="page"/>
							<logic:present name="data" property="calendar.user.person.company" scope="page">
								[<bean:write name="data" property="calendar.user.person.company.companyName" scope="page"/>]
							</logic:present>
						</logic:present>
						</logic:present>
						<logic:notPresent name="data" property="calendar.user">
						<bean:write name="data" property="calendar.name" scope="page"/>
						</logic:notPresent>
					</td><%
					if(changeStatus) 
					{ %>
					<td>
						<panel:editable condition="true" scope="panel">
						<panel:image property="doStatusNew" pageKey="image.entity.calendar.invitationStatus.event.new.url" titleKey="module.calendar.event.user.changeStatus.new" altKey="module.calendar.event.user.changeStatus.new" enable="true"/>
						<panel:image property="doStatusAccept" pageKey="image.entity.calendar.invitationStatus.event.accepted.url" titleKey="module.calendar.event.user.changeStatus.accept" altKey="module.calendar.event.user.changeStatus.accept" enable="true"/>
						<panel:image property="doStatusReject" pageKey="image.entity.calendar.invitationStatus.event.rejected.url" titleKey="module.calendar.event.user.changeStatus.reject" altKey="module.calendar.event.user.changeStatus.reject" enable="true"/>
						<panel:image property="doStatusDelete" pageKey="image.entity.calendar.invitationStatus.event.deleted.url" titleKey="module.calendar.event.user.changeStatus.delete" altKey="module.calendar.event.user.changeStatus.delete" enable="true"/>
						</panel:editable>
						<panel:editable condition="false" scope="panel">
						<panel:image property="doStatusNew" pageKey="image.entity.calendar.invitationStatus.event.new.url" titleKey="module.calendar.event.user.changeStatusImmediately.new" altKey="module.calendar.event.user.changeStatusImmediately.new" enable="true"/>
						<panel:image property="doStatusAccept" pageKey="image.entity.calendar.invitationStatus.event.accepted.url" titleKey="module.calendar.event.user.changeStatusImmediately.accept" altKey="module.calendar.event.user.changeStatusImmediately.accept" enable="true"/>
						<panel:image property="doStatusReject" pageKey="image.entity.calendar.invitationStatus.event.rejected.url" titleKey="module.calendar.event.user.changeStatusImmediately.reject" altKey="module.calendar.event.user.changeStatusImmediately.reject" enable="true"/>
						<panel:image property="doStatusDelete" pageKey="image.entity.calendar.invitationStatus.event.deleted.url" titleKey="module.calendar.event.user.changeStatusImmediately.delete" altKey="module.calendar.event.user.changeStatusImmediately.delete" enable="true"/>
						</panel:editable>
					</td><%
					} %>
					<td class="action">
						<logic:notEqual name="data" property="participiantType" scope="page" value="<%=EventCalendarVO.ParticipiantType.HOST.toString()%>">
						<oc:authenticate right="<%=Right.CALENDAR_CALENDAR_WRITE.getLabel()%>">
						<panel:editable condition="true">
						<html:image property="<%="doRemoveUser_"+TagUtility.lookup(pageContext, "data", "calendar.user.id", "page")%>" pageKey="image.icon.delete.url" titleKey="image.icon.delete.text" altKey="image.icon.delete.text"/>
						</panel:editable>
						<panel:editable condition="false">
						<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
						</panel:editable>
						</oc:authenticate>
						</logic:notEqual>
						<oc:authenticate right="<%=Right.CRM_PERSONS_READ.getLabel()%>">
						<% boolean doJumpPerson1 = false; %>
						<logic:present name="data" property="calendar.user" scope="page">
						<logic:present name="data" property="calendar.user.person" scope="page">
						<oc:entityAccess name="data" property="calendar.user.person" scope="page" access="<%=EntityAccess.Access.READ %>">
						<% doJumpPerson1 = true; %>
						</oc:entityAccess>
						</logic:present>
						</logic:present><%
						if(doJumpPerson1) { %>
						<panel:image property="<%="doJumpPerson_"+TagUtility.lookupString(pageContext, "data", "calendar.user.person.id", "page")%>" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/><%
						} else { %>
						<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/><%
						}%>
						</oc:authenticate>
					</td>
				</tr>
				</logic:iterate>
				</logic:present>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><bean:message key="module.calendar.event.block.persons"/></legend>
			<table class="light">
				<colgroup>
					<col width="30"/>
					<col width="100"/>
					<col/>
					<col width="50"/>
				</colgroup>
				<tr>
					<th colspan="2"><bean:message key="entity.calendar.event.invitationStatus"/></th>
					<th><bean:message key="entity.crm.person"/></th>
					<th style="text-align: right;">
						<oc:authenticate right="<%=Right.CALENDAR_CALENDAR_WRITE.getLabel()%>">	
						<panel:editable condition="true">					
						<html:image property="doAddPerson" pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text"/>
						</panel:editable>
						<panel:editable condition="false">					
						<html:img pageKey="image.icon.add_grey.url" titleKey="image.icon.add_grey.text" altKey="image.icon.add_grey.text"/>
						</panel:editable>
						</oc:authenticate>
					</th>
				</tr>
				<logic:present name="panel_eventPersons" scope="page">
				<logic:iterate id="data" name="panel_eventPersons" scope="page" indexId="index">
				<tr>
					<td colspan="2">
						<html:select property="<%="invitationStatus["+index+"]"%>" styleClass="full">
							<html:option key="entity.calendar.event.invitationStatus.new" value="<%=EventPersonVO.InvitationStatus.NEW.toString()%>"/>
							<html:option key="entity.calendar.event.invitationStatus.accept" value="<%=EventPersonVO.InvitationStatus.ACCEPTED.toString()%>"/>
							<html:option key="entity.calendar.event.invitationStatus.reject" value="<%=EventPersonVO.InvitationStatus.REJECTED.toString()%>"/>
						</html:select>
					</td>
					<td>
						<bean:write name="data" property="person.title" scope="page"/>
						<bean:write name="data" property="person.firstName" scope="page"/> <bean:write name="data" property="person.lastName" scope="page"/>
						<bean:write name="data" property="person.nameAffix" scope="page"/>
						<logic:present name="data" property="person.company" scope="page">
							[<bean:write name="data" property="person.company.companyName" scope="page"/>]
						</logic:present>
					</td>
					<td class="action">
						<oc:authenticate right="<%=Right.CALENDAR_CALENDAR_WRITE.getLabel()%>">
						<panel:editable condition="true">
						<html:image property="<%="doRemovePerson_"+TagUtility.lookup(pageContext, "data", "person.id", "page")%>" pageKey="image.icon.delete.url" titleKey="image.icon.delete.text" altKey="image.icon.delete.text"/>
						</panel:editable>
						<panel:editable condition="false">
						<html:img pageKey="image.icon.delete_grey.url" titleKey="image.icon.delete_grey.text" altKey="image.icon.delete_grey.text"/>
						</panel:editable>
						</oc:authenticate>
						<oc:authenticate right="<%=Right.CRM_PERSONS_READ.getLabel()+","+Right.CRM_PERSONS_WRITE.getLabel()%>">
						<oc:entityAccess name="data" property="person" scope="page" access="<%=EntityAccess.Access.READ %>">
						<panel:image property="<%="doJumpPerson_"+TagUtility.lookupString(pageContext, "data", "person.id", "page")%>" pageKey="image.icon.jump.url" titleKey="image.icon.jump.text" altKey="image.icon.jump.text" enable="true"/>
						</oc:entityAccess>
						<oc:notEntityAccess name="data" property="person" scope="page" access="<%=EntityAccess.Access.READ %>">
						<html:img pageKey="image.icon.jump_grey.url" titleKey="image.icon.jump_grey.text" altKey="image.icon.jump_grey.text"/>
						</oc:notEntityAccess>
						</oc:authenticate>
					</td>
				</tr>
				</logic:iterate>
				</logic:present>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><common:label property="unknownParticipiants" errorStyleId="error"><bean:message key="entity.calendar.event.unknownParticipiants"/></common:label></legend>
			<html:text property="unknownParticipiants" styleId="unknownParticipiants" maxlength="255" styleClass="full"/>
		</fieldset>
		
	</panel:edit>	 
	 
	</tiles:put>
</tiles:insert>
