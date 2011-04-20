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

<%@ page import="org.opencustomer.db.dao.system.UsergroupDAO" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>
<%@ page import="org.opencustomer.webapp.Globals" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title"><panel:title/></tiles:put>
	<tiles:put name="page.content">
		
	<panel:attributes/>
		
	<script type="text/javascript">
	<!--
		function edit(i) {
			document.location="<%=response.encodeURL(request.getContextPath()+"/system/management/usergroup/edit.do?id=")%>"+i;
		}
	//-->
	</script>
			
	<% boolean admin = false; %>	
	<logic:equal name="<%=Globals.USER_KEY%>" property="profile.role.admin" scope="session" value="true">
	<% admin = true; %>
	</logic:equal>
	
	<html:errors/>
		
	<html:form action="/system/management/usergroup" focus="name" styleClass="list" style="width: 600px;">
		<fieldset class="search">
			<legend><bean:message key="default.block.search"/></legend>
			<table class="search">
				<colgroup>
					<col/><%
					if(admin) { %>
					<col width="175px"/><%
					}%>
				</colgroup>
				<tr>
					<td>
						<common:label property="name" errorStyleId="error"><bean:message key="entity.system.usergroup.name"/></common:label>
						<html:text property="name" styleId="name"/>
					</td><%
					if(admin) { %>
					<td>
						<common:label property="admin" errorStyleId="error"><bean:message key="entity.system.usergroup.admin"/></common:label>
						<html:select property="admin" styleId="locked">
							<html:option value="<%=UsergroupDAO.AdminSelect.ALL.toString()%>">&nbsp;</html:option>
							<html:option value="<%=UsergroupDAO.AdminSelect.ADMIN.toString()%>"><bean:message key="default.boolean.true"/></html:option>
							<html:option value="<%=UsergroupDAO.AdminSelect.NOT_ADMIN.toString()%>"><bean:message key="default.boolean.false"/></html:option>
						</html:select>
					</td><%
					}%>
				</tr>
			</table>
			<div class="action">
				<html:image property="doSearch" pageKey="image.icon.search.url" styleClass="default"/>
				<html:image property="doResetSearch" pageKey="image.icon.searchClean.url" titleKey="image.icon.searchClean.text" altKey="image.icon.searchClean.text"/>
				<html:image property="doSearch" pageKey="image.icon.search.url" titleKey="image.icon.search.text" altKey="image.icon.search.text"/>
			</div>
		</fieldset>
		<oc:authenticate right="<%=Right.ADMINISTRATION_USERGROUP_WRITE.getLabel()%>">
		<fieldset class="listAction">
			<html:link action="/system/management/usergroup/edit" titleKey="image.icon.add.text">
				<html:img styleClass="action" altKey="image.icon.add.text" titleKey="image.icon.add.text" pageKey="image.icon.add.url"/>
			</html:link>
		</fieldset>		
		</oc:authenticate>
		<fieldset class="list">
			<legend>
				<common:paginationInfo 
					textKey="entity.system.usergroup" 
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
					<col/><%
					if(admin) { %>
					<col width="175px"/><%
					}%>
				</colgroup>
				<tr>
					<th>
						<common:sort
	  					 	messageKey="entity.system.usergroup.name"
							property="<%=String.valueOf(UsergroupDAO.SORT_NAME)%>"/>
					</th><%
					if(admin) { %>
					<th><bean:message key="entity.system.usergroup.admin"/></th><%
					}%>
				</tr>
				<logic:iterate id="data" indexId="index" name="panel_list" scope="page">
				<tr class="<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>" onmouseover="this.className='rowActive';" onmouseout="this.className='<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>'">
					<td onclick="edit(<bean:write name="data" property="id" scope="page"/>);"><bean:write name="data" property="name" scope="page"/></td><%
					if(admin) { %>
					<td onclick="edit(<bean:write name="data" property="id" scope="page"/>);">
						<logic:equal name="data" property="admin" scope="page" value="true">
						<bean:message key="default.boolean.true"/>
						</logic:equal>
						<logic:equal name="data" property="admin" scope="page" value="false">
						<bean:message key="default.boolean.false"/>
						</logic:equal>
					</td><%
					}%>
				</tr>
				</logic:iterate>
				<logic:notPresent name="index" scope="page">
				<tr class="noRow">
					<td colspan="<%if(admin){%>2<%}else{%>1<%}%>"><bean:message key="default.message.noEntries"/></td>
				</tr>
				</logic:notPresent>
			</table>
		</fieldset>
	</html:form>
		
	</tiles:put>
</tiles:insert>
