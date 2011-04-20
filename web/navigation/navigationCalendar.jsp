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

<%@ include file="/tiles/include.jsp" %>

<%@ page import="org.opencustomer.webapp.auth.Right" %>
<%@ page import="org.opencustomer.webapp.Globals" %>
<%@ page import="org.opencustomer.framework.db.vo.EntityAccess" %>
<%@ page import="org.opencustomer.db.vo.calendar.CalendarVO" %>
<%@ page import="org.opencustomer.db.vo.system.UserVO" %>

<oc:authenticate right="<%=Right.CALENDAR_CALENDAR_READ.getLabel()%>">
<% 
	UserVO user = (UserVO)session.getAttribute(Globals.USER_KEY);
	java.util.Set<CalendarVO> calendars = new java.util.HashSet<CalendarVO>();
	for(CalendarVO calendar : user.getCalendars()) {
	    if(org.opencustomer.db.EntityAccessUtility.isAccessGranted(user, calendar, EntityAccess.Access.READ))
	        calendars.add(calendar);
	}
	pageContext.setAttribute("calendars", calendars);
%>

<logic:equal name="calendars" scope="page" property="empty" value="false">
</ul>
<h2 class="inner"><bean:message key="navigation.calendar.others"/></h2>
<ul>   
<logic:iterate id="data" indexId="index" name="calendars" scope="page">
<li>
	<html:link action="/calendar/showCalendar" paramId="id" paramName="data" paramProperty="id" paramScope="page">
		-
		<logic:present name="data" property="user">
		<bean:write name="data" property="user.userName" scope="page"/>
		</logic:present>
		<logic:notPresent name="data" property="user">
		[<bean:write name="data" property="name" scope="page"/>]
		</logic:notPresent>
	</html:link>
</li>
</logic:iterate>
</logic:equal>
</oc:authenticate>

