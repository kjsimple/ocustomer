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

<%@ page import="org.opencustomer.db.vo.calendar.EventVO" %>
<%@ page import="org.opencustomer.db.vo.calendar.CalendarVO" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="body.ext">onload="check()"</tiles:put>
	<tiles:put name="page.content">
	
	<panel:attributes/>
	
	<script type="text/javascript">
	<!--
		function check() {
			form = document.getElementById("calendar.event.pageRecurrenceForm");
			if(form.recurrence.checked)
			{
				form.cycle.disabled = false;
				form.cycleUnit.disabled = false;
				
				if(form.cycleUnit.value == "<%=EventVO.RecurrenceUnit.WEEK%>")
				{
					<% 
	                    EventVO scriptEvent = (EventVO)pageContext.getAttribute("panel_event");
						for(EventVO.RecurrenceInWeek inWeek : EventVO.RecurrenceInWeek.values()) 
                        { 
                        	if(inWeek.equals(EventVO.RecurrenceInWeek.getForDate(scriptEvent.getStartDate()))) {%>
					    		form.inWeek_<%=inWeek%>.disabled = true;<% 
                            } else {
                                %>
					    		form.inWeek_<%=inWeek%>.disabled = false;<% 
                            }
                        } %>
				}
				else
				{
					<% for(EventVO.RecurrenceInWeek inWeek : EventVO.RecurrenceInWeek.values()) { %>
					    form.inWeek_<%=inWeek%>.disabled = true;<% } %>
				}
				
				if(form.cycleUnit.value == "<%=EventVO.RecurrenceUnit.MONTH%>")
				{
					<% for(EventVO.RecurrenceInMonth inMonth : EventVO.RecurrenceInMonth.values()) { %>
					    form.inMonth_<%=inMonth%>.disabled = false;<% } %>
				}
				else
				{
					<% for(EventVO.RecurrenceInMonth inMonth : EventVO.RecurrenceInMonth.values()) { %>
					    form.inMonth_<%=inMonth%>.disabled = true;<% } %>
				}

				form.recurrenceTypeForever.disabled = false;
				form.recurrenceTypeNumberOfTimes.disabled = false;
				form.recurrenceTypeUntilDate.disabled = false;

				if(form.recurrenceTypeNumberOfTimes.checked)
					form.numberOfTimes.disabled = false;
				else
					form.numberOfTimes.disabled = true;
				
				if(form.recurrenceTypeUntilDate.checked)
					form.untilDate.disabled = false;	
				else
					form.untilDate.disabled = true;	
			}
			else
			{
				form.cycle.disabled = true;
				form.cycleUnit.disabled = true;
				form.recurrenceTypeForever.disabled = true;
				form.recurrenceTypeNumberOfTimes.disabled = true;
				form.numberOfTimes.disabled = true;
				form.recurrenceTypeUntilDate.disabled = true;
				form.untilDate.disabled = true;
				<%for(EventVO.RecurrenceInWeek inWeek : EventVO.RecurrenceInWeek.values()) { %>
						    form.inWeek_<%=inWeek%>.disabled = true;  <% } %>
				<% for(EventVO.RecurrenceInMonth inMonth : EventVO.RecurrenceInMonth.values()) { %>
					    form.inMonth_<%=inMonth%>.disabled = true;<% } %>
			}
		}
	//-->
	</script>
	
	<html:errors/>
	
	<panel:edit>
		<fieldset class="unit">
			<table>
				<tr>
					<td class="label">
						<html:checkbox property="recurrence" styleId="recurrence" value="true" onchange="check()"/>
						<common:label property="recurrence" errorStyleId="error"><bean:message key="module.calendar.event.recurrence.repeat"/></common:label>
					</td>
					<td>
						<table style="width: 150px;">
							<tr>
								<td><html:text property="cycle" styleId="cycle" maxlength="255" style="width: 50px;"/></td>
								<td>
									<html:select property="cycleUnit" styleId="cycleUnit" style="width: 100px;" onchange="check()">
										<html:option key="entity.calendar.event.recurrence.cycleUnit.day" value="<%=String.valueOf(EventVO.RecurrenceUnit.DAY)%>"/>
										<html:option key="entity.calendar.event.recurrence.cycleUnit.week" value="<%=String.valueOf(EventVO.RecurrenceUnit.WEEK)%>"/>
										<html:option key="entity.calendar.event.recurrence.cycleUnit.month" value="<%=String.valueOf(EventVO.RecurrenceUnit.MONTH)%>"/>
										<html:option key="entity.calendar.event.recurrence.cycleUnit.year" value="<%=String.valueOf(EventVO.RecurrenceUnit.YEAR)%>"/>
									</html:select>
								</td>
							</tr>
						</table>
						<div>
							<div class="subLabel"><bean:message key="entity.calendar.event.recurrence.inWeek"/></div>
							<div><%
							CalendarVO calendar = (CalendarVO)pageContext.getAttribute("panel_calendar");
							java.util.Calendar cal = java.util.GregorianCalendar.getInstance();
							cal.set(java.util.Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek().getDay());	
							
							int count = 0;
							while(count < 7)
							{ 
							    EventVO.RecurrenceInWeek inWeek = EventVO.RecurrenceInWeek.getForDate(cal.getTime());
							    pageContext.setAttribute("day", cal.getTime());
							%>
							<html:multibox property="inWeek" styleId="<%="inWeek_"+inWeek%>" value="<%=inWeek.toString()%>"/>
							<common:label property="<%="inWeek_"+inWeek%>" errorStyleId="error"><bean:write name="day" scope="page" format="EE"/></common:label><%
							    cal.add(java.util.Calendar.DAY_OF_WEEK, 1);
							    count++;
							} %></div>
						</div>
						<div>
							<div class="subLabel"><bean:message key="entity.calendar.event.recurrence.inMonth"/></div><%
							EventVO event = (EventVO) pageContext.getAttribute("panel_event");
						
							java.util.Calendar cal2 = java.util.GregorianCalendar.getInstance();
							cal2.setFirstDayOfWeek(calendar.getFirstDayOfWeek().getDay());
							cal2.setTime(event.getStartDate());
							
							java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE");
							int dayOfMonth = cal2.get(java.util.Calendar.DAY_OF_MONTH);
							int dayOfWeekInMonth = cal2.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH);
							
							int calc1 = dayOfMonth / 7;
							int calc2 = dayOfMonth % 7;
							if(calc2 != 0)
							    calc1++;
						%>
							<div>
								<html:radio property="inMonth" styleId="<%="inMonth_"+EventVO.RecurrenceInMonth.DAY_OF_MONTH%>" value="<%=EventVO.RecurrenceInMonth.DAY_OF_MONTH.toString()%>"/>
								<common:label property="<%="inMonth_"+EventVO.RecurrenceInMonth.DAY_OF_MONTH%>" errorStyleId="error"><bean:message key="module.calendar.event.recurrence.inMonth.dayOfMonth" arg0="<%=String.valueOf(dayOfMonth)%>"/></common:label>
							</div>
							<div>
								<html:radio property="inMonth" styleId="<%="inMonth_"+EventVO.RecurrenceInMonth.DAY_OF_WEEK%>" value="<%=EventVO.RecurrenceInMonth.DAY_OF_WEEK.toString()%>"/>
								<common:label property="<%="inMonth_"+EventVO.RecurrenceInMonth.DAY_OF_WEEK%>" errorStyleId="error"><bean:message key="module.calendar.event.recurrence.inMonth.dayOfWeek" arg0="<%=String.valueOf(dayOfWeekInMonth)%>" arg1="<%=sdf.format(cal2.getTime())%>"/></common:label>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="label" colspan="2">
						<html:radio property="recurrenceType" styleId="recurrenceTypeForever" value="<%=EventVO.RecurrenceType.FOREVER.toString()%>" onchange="check()"/>
						<common:label property="recurrenceTypeForever" errorStyleId="error"><bean:message key="entity.calendar.event.recurrence.type.forever"/></common:label>
					</td>
				</tr>
				<tr>
					<td class="label">
						<html:radio property="recurrenceType" styleId="recurrenceTypeNumberOfTimes" value="<%=EventVO.RecurrenceType.NUMBER_OF_TIMES.toString()%>" onchange="check()"/>
						<common:label property="recurrenceTypeNumberOfTimes" errorStyleId="error"><bean:message key="entity.calendar.event.recurrence.type.numberOfTimes"/></common:label>
					</td>
					<td>
						<html:text property="numberOfTimes" styleId="numberOfTimes" maxlength="255" styleClass="full"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<html:radio property="recurrenceType" styleId="recurrenceTypeUntilDate" value="<%=EventVO.RecurrenceType.UNTIL_DATE.toString()%>" onchange="check()"/>
						<common:label property="recurrenceTypeUntilDate" errorStyleId="error"><bean:message key="entity.calendar.event.recurrence.type.untilDate"/></common:label>
					</td>
					<td>
						<html:text property="untilDate" styleId="untilDate" maxlength="255" styleClass="full"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</panel:edit>	
	
	</tiles:put>
</tiles:insert>
