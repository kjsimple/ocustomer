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
<%@ page import="java.util.*" %>
<%@ page import="org.opencustomer.framework.webapp.util.TagUtility" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.opencustomer.framework.db.vo.EntityAccess" %>
<%@ page import="org.opencustomer.db.vo.calendar.EventCalendarVO" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title"><panel:title/></tiles:put>
	<tiles:put name="page.content">
		
	<panel:attributes/>
		
	<script type="text/javascript">
	<!--
		function edit(i) {
			document.location="<%=response.encodeURL(request.getContextPath()+"/calendar/event/edit.do?id=")%>"+i;
		}
		
		function showOnDay(value) {
            var displayValue = document.getElementById("onDay_"+value).style.display == "none" ? "inline" : "none";
			document.getElementById("onDay_"+value).style.display=displayValue;
        }
	//-->
	</script>
	
	<html:errors/>
	
	<oc:authenticate right="<%=Right.CALENDAR_CALENDAR_WRITE.getLabel()%>">	
	<oc:entityAccess name="panel_calendar" scope="page" access="<%=EntityAccess.Access.WRITE%>">
	<fieldset class="action">
		<table style="width: 100%;"><tr>
		<td>
			<html:link action="/calendar/edit" styleClass="text">
				<bean:message key="module.calendar.settings"/>
			</html:link>
		</td>
		<td style="text-align: right;">
			<html:link action="/calendar/event/edit" titleKey="image.icon.add.text">
				<html:img styleClass="action" altKey="image.icon.add.text" titleKey="image.icon.add.text" pageKey="image.icon.add.url"/>
			</html:link>
		</td>
		</tr></table>
	</fieldset>		
	</oc:entityAccess>
	</oc:authenticate>
	<fieldset class="calendarMonth">
	<table class="navigation"><tr>
		<td class="left">
			<html:link action="/calendar/showMonth?doLastMonth=true" titleKey="image.icon.lastMonth.text">
				<html:img pageKey="image.icon.lastMonth.url" altKey="image.icon.lastMonth.text"/>
			</html:link>
		</td><td class="center">
			<bean:write name="panel_calendarView" property="referenceDate" scope="page" formatKey="module.calendar.format.date.month"/>
		</td><td class="right">
			<html:link action="/calendar/showMonth?doNextMonth=true" titleKey="image.icon.nextMonth.text">
				<html:img pageKey="image.icon.nextMonth.url" altKey="image.icon.nextMonth.text"/>
			</html:link>
		</td>
	</tr></table>
		
	<table class="calendarMonth">
		<colgroup>
			<col width="10"/>
			<col width="1*"/>
			<col width="1*"/>
			<col width="1*"/>
			<col width="1*"/>
			<col width="1*"/>
			<col width="1*"/>
			<col width="1*"/>
		</colgroup>
		<tr>
			<th class="day">&nbsp;</th>
	<logic:iterate indexId="index" length="7" name="panel_calendarView" property="elements" id="data" scope="page">
			<th class="day">
				<bean:write name="data" property="startDate" scope="page" formatKey="module.calendar.format.date.dayOfWeek"/><br/>
			</th>
	</logic:iterate>
		</tr>
		<%
			Calendar day   = GregorianCalendar.getInstance();
			Calendar start = GregorianCalendar.getInstance();
			Calendar end   = GregorianCalendar.getInstance();
		%>
	<logic:iterate indexId="index" name="panel_calendarView" property="elements" id="data" scope="page"><%
    	int calculate = ((Integer)pageContext.getAttribute("index")).intValue()%7;
		if(calculate == 0) { %><tr>
			<th class="week">
				<bean:write name="data" property="startDate" scope="page" formatKey="module.calendar.format.date.week"/><br/>
			</th><% 
		} %>
			<td<logic:equal name="data" property="inPeriod" scope="page" value="false"> class="anotherMonth"</logic:equal><logic:equal name="data" property="inSubPeriod" scope="page" value="true"> id="today"</logic:equal> 
				onmouseover="showOnDay('<bean:write name="data" property="startDate" scope="page" formatKey="default.format.date.date"/>')"
				onmouseout="showOnDay('<bean:write name="data" property="startDate" scope="page" formatKey="default.format.date.date"/>')">
				<div class="day">
					<div id="onDay_<bean:write name="data" property="startDate" scope="page" formatKey="default.format.date.date"/>" style="display: none">
					<oc:entityAccess name="panel_calendar" scope="page" access="<%=EntityAccess.Access.WRITE%>">
					<html:link action="<%="/calendar/event/edit?date="+new SimpleDateFormat("yyyyMMdd").format((Date)TagUtility.lookup(pageContext, "data", "startDate", "page")) %>" titleKey="image.icon.add.text">
						+ 
					</html:link>
					</oc:entityAccess>
					</div>
					<bean:write name="data" property="startDate" scope="page" formatKey="module.calendar.format.date.dayOfMonth"/>
				</div>
				<%
					day.setTime((Date)TagUtility.lookup(pageContext, "data", "startDate", "page"));
					day.set(Calendar.HOUR_OF_DAY, 0);
					day.set(Calendar.MINUTE, 0);
					day.set(Calendar.SECOND, 0);
					day.set(Calendar.MILLISECOND, 0);
				%>
				<logic:iterate name="data" property="events" id="bean" scope="page">
					<div <logic:equal name="bean" property="readable" value="true">
							class="event" onmouseover="this.className='eventActive';" onmouseout="this.className='event';" onclick="edit(<bean:write name="bean" property="event.id" scope="page"/>);"
						</logic:equal>
						<logic:equal name="bean" property="readable" value="false">
							class="eventNotReadable"
						</logic:equal>>
						<div class="time">
						<logic:equal name="bean" property="readable" value="true">
							<table><tr><td>
						</logic:equal>
								<logic:equal name="bean" property="event.allDay" scope="page" value="true">
								<html:img pageKey="image.entity.calendar.allDayEvent.url" titleKey="image.entity.calendar.allDayEvent.text"/>
								</logic:equal>
								<logic:equal name="bean" property="event.allDay" scope="page" value="false">
									<bean:write name="bean" property="startDate" scope="page" formatKey="default.format.date.time"/> -
									<bean:write name="bean" property="endDate" scope="page" formatKey="default.format.date.time"/>
								</logic:equal>
						<logic:equal name="bean" property="readable" value="true">
							</td><td class="icons">
							<logic:equal name="bean" property="status" scope="page" value="<%=EventCalendarVO.InvitationStatus.NEW.toString()%>">
								<html:img pageKey="image.entity.calendar.event.new.url" titleKey="image.entity.calendar.event.new.text" altKey="image.entity.calendar.event.new.text"/>
							</logic:equal>
							<logic:present name="bean" property="reminderDate" scope="page">
								<html:img pageKey="image.entity.calendar.event.alarm.url" titleKey="image.entity.calendar.event.alarm.text" altKey="image.entity.calendar.event.alarm.text"/>
							</logic:present>
							</td></tr></table>
						</logic:equal>
						</div>
						<div class="title">
						<logic:equal name="bean" property="readable" value="true">
							<bean:write name="bean" property="event.title" scope="page"/>
							<logic:present name="bean" property="recurrenceNumber" scope="page">
							[<bean:write name="bean" property="recurrenceNumber" scope="page"/>]
							</logic:present>
						</logic:equal>
						<logic:equal name="bean" property="readable" value="false">
							<bean:message key="module.calendar.showCalendar.month.occupied"/>
						</logic:equal>
						</div>
					</div>
				</logic:iterate>
			</td><%
		if(calculate == 6) { %></tr><% } %>
	</logic:iterate>
	</table>
	</fieldset>
	</tiles:put>
</tiles:insert>
