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
   - Portions created by the Initial Developer are Copyright (C) 2006
   - the Initial Developer. All Rights Reserved.
   -
   - Contributor(s):
   -   Thomas Bader <thomas.bader@bader-jene.de>
   -
   - ***** END LICENSE BLOCK ***** --%>
   
<%@ include file="/tiles/page.jsp" %>

<%@ page import="org.opencustomer.db.dao.crm.custom.JobListDAO" %>
<%@ page import="org.opencustomer.db.vo.crm.JobVO" %>
<%@ page import="org.opencustomer.webapp.auth.Right" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<script type="text/javascript">
	<!--
		function edit(i) {
			document.location="<%=response.encodeURL(request.getContextPath()+"/crm/job/edit.do?id=")%>"+i;
		}
	//-->
	</script>
		
	<html:errors/>

	<panel:edit>
		<fieldset class="search">
			<legend><bean:message key="default.block.search"/></legend>
				<table class="search" style="width: 100%;">
					<colgroup>
						<col width="175"/>
						<col width="100"/>
						<col/>
						<col width="150"/>
					</colgroup>
					<tr class="search">
						<td>
							<common:label property="dateStart" errorStyleId="error"><bean:message key="entity.crm.job.dueDate"/></common:label><br/>
							<table class="double"><tr>
								<td><html:text property="dateStart" styleId="dateStart"/></td>
								<td class="divider">-</td>
								<td><html:text property="dateEnd"/></td>
							</tr></table>
						</td>
						<td>
							<common:label property="status" errorStyleId="error"><bean:message key="entity.crm.job.status"/></common:label>
							<html:select property="status" styleId="status">
							<html:option value=""><bean:message key="default.select.none"/></html:option>
							<html:option key="entity.crm.job.status.planned" value="<%=JobVO.Status.PLANNED.toString()%>"/>
							<html:option key="entity.crm.job.status.inProgress" value="<%=JobVO.Status.IN_PROGRESS.toString()%>"/>
							<html:option key="entity.crm.job.status.completed" value="<%=JobVO.Status.COMPLETED.toString()%>"/>
						</html:select>
						</td>
						<td colspan="2">
							<common:label property="subject" errorStyleId="error"><bean:message key="entity.crm.job.subject"/></common:label>
							<html:text property="subject" styleId="subject"/>
						</td>
					</tr>
				</table>
				<div class="action">
					<html:image property="doSearch" pageKey="image.icon.search.url" styleClass="default"/>
					<html:image property="doResetSearch" pageKey="image.icon.searchClean.url" titleKey="image.icon.searchClean.text" altKey="image.icon.searchClean.text"/>
					<html:image property="doSearch" pageKey="image.icon.search.url" titleKey="image.icon.search.text" altKey="image.icon.search.text"/>
				</div>
				</fieldset>
				<oc:authenticate right="<%=Right.CRM_JOBS_WRITE.getLabel()%>">
				<fieldset class="listAction">
					<html:link action="/crm/job/edit" titleKey="image.icon.add.text">
						<html:img styleClass="action" altKey="image.icon.add.text" titleKey="image.icon.add.text" pageKey="image.icon.add.url"/>
					</html:link>
				</fieldset>
				</oc:authenticate>
				<fieldset class="list">
				<legend>
					<common:paginationInfo 
						textKey="entity.crm.contact" 
						name="panel_jobListScroll" 
						scope="page"/>
				</legend>
				<div class="pagination">
					<common:paginationMenu 
						name="panel_jobListScroll" 
						scope="page"/>
				</div>
				<table class="result">
					<colgroup>
						<col width="175"/>
						<col width="50"/>
						<col/>
						<col width="150"/>
					</colgroup>
					<tr>
						<th>
							<common:sort
		  					 	messageKey="entity.crm.job.dueDate"
								property="<%=String.valueOf(JobListDAO.SORT_DUEDATE)%>"/>
						</th>
						<th>
							<common:sort
		  					 	messageKey="entity.crm.job.status"
								property="<%=String.valueOf(JobListDAO.SORT_STATUS)%>"/>
						</th>
						<th>
							<common:sort
		  					 	messageKey="entity.crm.job.subject"
								property="<%=String.valueOf(JobListDAO.SORT_SUBJECT)%>"/>
						</th>
						<th>
							<common:sort
		  					 	messageKey="entity.crm.job.assignedUser"
								property="<%=String.valueOf(JobListDAO.SORT_ASSIGNEDUSER)%>"/>
						</th>
					</tr>
					<logic:present name="panel_jobList" scope="page">
					<logic:iterate id="data" indexId="index" name="panel_jobList" scope="page">
					<tr class="<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>" onmouseover="this.className='rowActive';" onmouseout="this.className='<common:rowSwap name="index" number="3" odd="rowOdd" even="rowEven"/>'">
						<td onclick="edit(<bean:write name="data" property="jobId" scope="page"/>);">
							<bean:write name="data" property="dueDate" scope="page" formatKey="default.format.date.datetime"/>
						</td>
						<td onclick="edit(<bean:write name="data" property="jobId" scope="page"/>);">
							<logic:equal name="data" property="status" scope="page" value="<%=JobVO.Status.PLANNED.toString()%>">
								<bean:message key="entity.crm.job.status.planned"/>
							</logic:equal>
							<logic:equal name="data" property="status" scope="page" value="<%=JobVO.Status.IN_PROGRESS.toString()%>">
								<bean:message key="entity.crm.job.status.inProgress"/>
							</logic:equal>
							<logic:equal name="data" property="status" scope="page" value="<%=JobVO.Status.COMPLETED.toString()%>">
								<bean:message key="entity.crm.job.status.completed"/>
							</logic:equal>
						</td>
						<td onclick="edit(<bean:write name="data" property="jobId" scope="page"/>);">
							<bean:write name="data" property="subject" scope="page"/>
						</td>
						<td onclick="edit(<bean:write name="data" property="jobId" scope="page"/>);">							
							<bean:write name="data" property="assignedUserName" scope="page"/>
						</td>
					</tr>
					</logic:iterate>
					</logic:present>
					<logic:notPresent name="index" scope="page">
					<tr class="noRow">
						<td colspan="4"><bean:message key="default.message.noEntries"/></td>
					</tr>
					</logic:notPresent>
					
				</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
