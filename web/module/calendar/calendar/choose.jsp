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

<%@ page import="org.opencustomer.db.dao.calendar.custom.CalendarListDAO" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title"><panel:title/></tiles:put>
	<tiles:put name="page.content">
		
	<panel:attributes/>
			
	<script type="text/javascript">
	<!--
		function choose(i) {
			document.location="<%=response.encodeURL(request.getContextPath()+"/calendar/choose.do?id=")%>"+i;
		}
	//-->
	</script>
	
	<html:errors/>
	
	<html:form action="/calendar/choose" styleClass="list" style="width: 500px;">
		<fieldset class="search">
			<legend><bean:message key="default.block.search"/></legend>
			<table class="search">
				<colgroup>
					<col width="20px"/>
					<col width="1*"/>
					<col width="1*"/>
				</colgroup>
				<tr>
					<td>&nbsp;</td>
					<td>
						<common:label property="name" errorStyleId="error"><bean:message key="entity.calendar.calendar.name"/></common:label>
						<html:text property="name" styleId="name"/>
					</td>
					<td>
						<common:label property="userName" errorStyleId="error"><bean:message key="entity.system.user.userName"/></common:label>
						<html:text property="userName" styleId="userName"/>
					</td>
				</tr>
			</table>
			<div class="action">
				<html:image property="doSearch" pageKey="image.icon.search.url" styleClass="default"/>
				<html:image property="doResetSearch" pageKey="image.icon.searchClean.url" titleKey="image.icon.searchClean.text" altKey="image.icon.searchClean.text"/>
				<html:image property="doSearch" pageKey="image.icon.search.url" titleKey="image.icon.search.text" altKey="image.icon.search.text"/>
			</div>
		</fieldset>
		<fieldset class="list">
			<legend>
				<common:paginationInfo 
					textKey="entity.calendar.calendar" 
					name="panel_listScroll" 
					scope="page"/>
			</legend>
			<div class="pagination">
				<common:paginationMenu 
					name="panel_listScroll" 
					scope="page"/>
			</div>
			<table class="result">
				<colgroup>
					<col width="20px"/>
					<col width="1*"/>
					<col width="1*"/>
				</colgroup>
				<tr>
					<th>&nbsp;</th>
					<th>
						<common:sort
	  					 	messageKey="entity.calendar.calendar.name"
							property="<%=String.valueOf(CalendarListDAO.SORT_NAME)%>"/>
					</th>
					<th>
						<common:sort
	  					 	messageKey="entity.system.user.userName"
							property="<%=String.valueOf(CalendarListDAO.SORT_USERNAME)%>"/>
					</th>
				</tr>
				<logic:iterate id="data" indexId="index" name="panel_list" scope="page">
				<tr class="<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>" onmouseover="this.className='rowActive';" onmouseout="this.className='<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>'">
					<td onclick="choose(<bean:write name="data" property="calendarId" scope="page"/>);">
						<logic:equal name="data" property="assigned" scope="page" value="true">
						<html:img pageKey="image.icon.delete.url" titleKey="image.icon.delete.text" altKey="image.icon.delete.text"/>
						</logic:equal>
						<logic:equal name="data" property="assigned" scope="page" value="false">
						<html:img pageKey="image.icon.add.url" titleKey="image.icon.add.text" altKey="image.icon.add.text"/>
						</logic:equal>
					</td>
					<td onclick="choose(<bean:write name="data" property="calendarId" scope="page"/>);"><bean:write name="data" property="name" scope="page"/></td>
					<td onclick="choose(<bean:write name="data" property="calendarId" scope="page"/>);"><bean:write name="data" property="userName" scope="page"/></td>
				</tr>
				</logic:iterate>
				<logic:notPresent name="index" scope="page">
				<tr class="noRow">
					<td colspan="3"><bean:message key="default.message.noEntries"/></td>
				</tr>
				</logic:notPresent>
			</table>
		</fieldset>
	</html:form>
		
	</tiles:put>
</tiles:insert>
