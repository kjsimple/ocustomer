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

<%@ page import="org.opencustomer.db.dao.system.custom.UserListDAO" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title"><panel:title/></tiles:put>
	<tiles:put name="page.content">
		
	<panel:attributes/>
		
	<script type="text/javascript">
	<!--
		function edit(i) {
			document.location="<%=response.encodeURL(request.getContextPath()+pageContext.getAttribute("panel_path")+".do?id=")%>"+i;
		}
	//-->
	</script>
		
	<html:errors/>
		
	<html:form action="<%=(String)pageContext.getAttribute("panel_path")%>" styleClass="list" style="width: 500px;">
		<fieldset class="search">
			<legend><bean:message key="default.block.search"/></legend>
			<table class="search">
				<colgroup>
					<col width="1*"/>
					<col width="1*"/>
					<col width="1*"/>
				</colgroup>
				<tr>
					<td>
						<common:label property="userName" errorStyleId="error"><bean:message key="entity.system.user.userName"/></common:label>
						<html:text property="userName" styleId="userName"/>
					</td>
					<td>
						<common:label property="lastName" errorStyleId="error"><bean:message key="entity.crm.person.lastName"/></common:label>
						<html:text property="lastName" styleId="lastName"/>
					</td>
					<td>
						<common:label property="firstName" errorStyleId="error"><bean:message key="entity.crm.person.firstName"/></common:label>
						<html:text property="firstName" styleId="firstName"/>
					</td>
				</tr>				
			</table>
			<div class="action">
				<html:image property="doSearch" pageKey="image.icon.search.url" styleClass="default"/>
				<html:image property="doResetSearch" pageKey="image.icon.searchClean.url" titleKey="image.icon.searchClean.text" altKey="image.icon.searchClean.text"/>
				<html:image property="doSearch" pageKey="image.icon.search.url" titleKey="image.icon.search.text" altKey="image.icon.search.text"/>
			</div>
		</fieldset>
		<fieldset class="listAction">
			<html:image property="doBack" pageKey="image.icon.back.url" titleKey="image.icon.back.text" altKey="image.icon.back.text"/>
		</fieldset>		
		<fieldset class="list">
			<legend>
				<common:paginationInfo 
					textKey="entity.system.user" 
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
					<col width="1*"/>
					<col width="1*"/>
					<col width="1*"/>
				</colgroup>
				<tr>
				<th>
					<common:sort
  					 	messageKey="entity.system.user.userName"
						property="<%=String.valueOf(UserListDAO.SORT_USERNAME)%>"/>
				</th>
				<th>
					<common:sort
  					 	messageKey="entity.crm.person.lastName"
						property="<%=String.valueOf(UserListDAO.SORT_LASTNAME)%>"/>
				</th>
				<th>
					<common:sort
  					 	messageKey="entity.crm.person.firstName"
						property="<%=String.valueOf(UserListDAO.SORT_FIRSTNAME)%>"/>
				</th>
			</tr>
			<logic:iterate id="data" indexId="index" name="panel_list" scope="page">
			<tr class="<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>" onmouseover="this.className='rowActive';" onmouseout="this.className='<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>'">
				<td onclick="edit(<bean:write name="data" property="userId" scope="page"/>);">
					<bean:write name="data" property="userName" scope="page"/>
				</td>
				<td onclick="edit(<bean:write name="data" property="userId" scope="page"/>);">
					<bean:write name="data" property="lastName" scope="page"/>
				</td>
				<td onclick="edit(<bean:write name="data" property="userId" scope="page"/>);">
					<bean:write name="data" property="firstName" scope="page"/>
				</td>
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