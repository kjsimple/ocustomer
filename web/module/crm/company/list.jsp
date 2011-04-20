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
   - Portions created by the Initial Developer are Copyright (C) 2007
   - the Initial Developer. All Rights Reserved.
   -
   - Contributor(s):
   -   Thomas Bader <thomas.bader@bader-jene.de>
   -
   - ***** END LICENSE BLOCK ***** --%>
   
<%@ include file="/tiles/page.jsp" %>

<%@ page import="org.opencustomer.webapp.auth.Right" %>
<%@ page import="org.opencustomer.framework.webapp.util.TagUtility" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title"><panel:title/></tiles:put>
	<tiles:put name="page.content">

	<panel:attributes/>
	
	<html:errors/>
	
	<script type="text/javascript">
	<!--
		function edit(i) {
			document.location="<%=response.encodeURL(request.getContextPath()+"/crm/company/edit.do?id=")%>"+i;
		}
		
		function submitList() {
			listForm = document.getElementById("form.list");
			chooseList = document.getElementById("doChooseList_auto");
			chooseList.value='submit';
			
			listForm.submit();
		}
	//-->
	</script>
	
	<html:form action="/crm/company" styleClass="list" styleId="form.list">
		<div><html:image property="doSearch" pageKey="image.pixel.url"  altKey="image.pixel.text"/></div>
		<fieldset class="unit">
			<legend><bean:message key="default.list.choose.block"/></legend>
			<table>
				<tr>
					<td style="padding-right: 10px;"><bean:message key="entity.system.listConfiguration"/></td>
					<td style="padding-right: 10px;">
						<html:select property="listId" styleId="listId" onchange="submitList()" style="width: 150px;">
							<logic:present name="panel_listConfigurations" scope="page">
							<logic:notEqual name="panel_listConfigurations" property="userList.empty" scope="page" value="true">
							<logic:notEqual name="panel_listConfigurations" property="globalList.empty" scope="page" value="true">
							<optgroup label="<bean:message key="default.list.choose.optiongroup.user"/>">
							</logic:notEqual>
							<html:optionsCollection name="panel_listConfigurations" property="userList" value="id" label="name"/>
							<logic:notEqual name="panel_listConfigurations" property="globalList.empty" scope="page" value="true">
							</optgroup>
							</logic:notEqual>
							</logic:notEqual>
							<logic:notEqual name="panel_listConfigurations" property="globalList.empty" scope="page" value="true">
							<logic:notEqual name="panel_listConfigurations" property="userList.empty" scope="page" value="true">
							<optgroup label="<bean:message key="default.list.choose.optiongroup.global"/>">
							</logic:notEqual>
							<html:optionsCollection name="panel_listConfigurations" property="globalList" value="id" label="name"/>
							<logic:notEqual name="panel_listConfigurations" property="userList.empty" scope="page" value="true">
							</optgroup>
							</logic:notEqual>
							</logic:notEqual>	
							</logic:present>
						</html:select>
					</td>
					<oc:authenticate right="<%=Right.ADMINISTRATION_LIST_READ.getLabel()%>">
					<td style="padding-right: 25px;">
						<logic:present name="panel_listConfigurations" scope="page">
						<html:image property="doEditList" pageKey="image.icon.list.edit.url" altKey="image.icon.list.edit.text" titleKey="image.icon.list.edit.text"/>
						</logic:present>
						<logic:notPresent name="panel_listConfigurations" scope="page">
						<html:img pageKey="image.icon.list.edit_grey.url" altKey="image.icon.list.edit_grey.text" titleKey="image.icon.list.edit_grey.text"/>
						</logic:notPresent>
						<oc:authenticate right="<%=Right.ADMINISTRATION_LIST_WRITE.getLabel()%>">
						<html:image property="doAddList" pageKey="image.icon.list.add.url" altKey="image.icon.list.add.text" titleKey="image.icon.list.add.text"/>
						</oc:authenticate>
						<logic:present name="panel_listConfigurations" scope="page">
						<html:hidden property="doChooseList" styleId="doChooseList_auto" write="false"/>
						</logic:present>
					</td>
					</oc:authenticate>
					<oc:authenticate right="<%=Right.CRM_COMPANIES_DOWNLOAD.getLabel()%>">
					<td>
						<html:link action="/crm/company/download">
							<html:img pageKey="image.icon.list.download.url" altKey="image.icon.list.download.text" titleKey="image.icon.list.download.text"/>
						</html:link>
					</td>
					</oc:authenticate>
				</tr>
			</table>
		</fieldset>
		<fieldset class="search">
			<legend><bean:message key="default.block.search"/></legend>
			
			<common:search name="panel_table" scope="page" style="margin-top: 20px;"/>

			<div class="action">
				<html:image property="doResetSearch" pageKey="image.icon.searchClean.url" titleKey="image.icon.searchClean.text" altKey="image.icon.searchClean.text"/>
				<html:image property="doSearch" pageKey="image.icon.search.url" titleKey="image.icon.search.text" altKey="image.icon.search.text"/>
			</div>
		</fieldset>
		<oc:authenticate right="<%=Right.CRM_COMPANIES_WRITE.getLabel()%>">
		<fieldset class="listAction">
			<html:link action="/crm/company/edit" titleKey="image.icon.add.text">
				<html:img styleClass="action" altKey="image.icon.add.text" titleKey="image.icon.add.text" pageKey="image.icon.add.url"/>
			</html:link>
		</fieldset>		
		</oc:authenticate>
		<fieldset class="list">
			<legend>
				<common:paginationInfo 
					textKey="entity.crm.company" 
					name="panel_table"
					scope="page"/>
			</legend>
			<div class="pagination">
				<common:paginationMenu 
					name="panel_table"
					scope="page"/>
			</div>

			<common:table name="panel_table" scope="page" onclick="edit({id});" style="margin-top: 20px;"/>

		</fieldset>
		
		
	</html:form>
		
	</tiles:put>
</tiles:insert>