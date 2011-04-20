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
	
	<panel:edit focus="title">
		<fieldset class="unit">
			<legend><bean:message key="module.calendar.event.block.common"/></legend>
			<table>
				<tr>
					<td class="label">
						<common:label property="title" errorStyleId="error"><bean:message key="entity.calendar.event.title"/></common:label>
					</td>
					<td>
						<html:text property="title" styleId="title" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="startTime" errorStyleId="error"><bean:message key="entity.calendar.event.startDate"/></common:label>
					</td>
					<td>
						<table style="width: 100%;"><tr><td style="width: 100%; padding: 0px 10px 0px 0px">
						<html:text property="startTime" styleId="startTime" styleClass="full"/>
						</td><td style="padding: 0px;">
						<html:img styleId="chooseTime1" altKey="image.icon.chooseDate.text" titleKey="image.icon.chooseDate.text" pageKey="image.icon.chooseDate.url"/>
						<oc:jsCalendar input="startTime" button="chooseTime1" formatKey="default.format.input.dateTime"/>
						</td></tr></table>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="endTime" errorStyleId="error"><bean:message key="entity.calendar.event.endDate"/></common:label>
					</td>
					<td>
						<table style="width: 100%;"><tr><td style="width: 100%; padding: 0px 10px 0px 0px">
						<html:text property="endTime" styleId="endTime" styleClass="full"/>
						</td><td style="padding: 0px;">
						<html:img styleId="chooseTime2" altKey="image.icon.chooseDate.text" titleKey="image.icon.chooseDate.text" pageKey="image.icon.chooseDate.url"/>
						<oc:jsCalendar input="endTime" button="chooseTime2" formatKey="default.format.input.dateTime"/>
						</td></tr></table>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="allDay" errorStyleId="error"><bean:message key="entity.calendar.event.allDay"/></common:label>
					</td>
					<td>
						<html:checkbox property="allDay" styleId="allDay"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<table>
				<tr>
					<td class="label">
						<common:label property="reminderDate" errorStyleId="error"><bean:message key="entity.calendar.event.reminderDate"/></common:label>
					</td>
					<td>
						<table style="width: 100%;"><tr><td style="width: 100%; padding: 0px 10px 0px 0px">
						<html:text property="reminderDate" styleId="reminderDate" styleClass="full"/>
						</td><td style="padding: 0px;">
						<html:img styleId="chooseTime3" altKey="image.icon.chooseDate.text" titleKey="image.icon.chooseDate.text" pageKey="image.icon.chooseDate.url"/>
						<oc:jsCalendar input="reminderDate" button="chooseTime3" formatKey="default.format.input.dateTime"/>
						</td></tr></table>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="occupied" errorStyleId="error"><bean:message key="entity.calendar.event.occupied"/></common:label>
					</td>
					<td>
						<html:checkbox property="occupied" styleId="occupied"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<common:label property="location" errorStyleId="error"><bean:message key="entity.calendar.event.location"/></common:label>
					</td>
					<td>
						<html:text property="location" styleId="location" maxlength="255" styleClass="full"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset class="unit">
			<legend><common:label property="description" errorStyleId="error"><bean:message key="entity.calendar.event.description"/></common:label></legend>
			<table>
				<tr>
					<td>
						<html:textarea property="description" styleId="description"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</panel:edit>	 
	 
	</tiles:put>
</tiles:insert>
