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

<%@ page import="org.opencustomer.framework.webapp.util.TagUtility" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">

	<html:errors/>
		
	<panel:edit>
	
		<fieldset class="unit">	
			<legend><bean:message key="module.system.list.block.properties"/></legend>
		<table class="rights">
			<colgroup>
				<col width="2*"/>
				<col width="1*"/>
			</colgroup>
			<tr>
				<th><bean:message key="module.system.list.property"/></th>
				<th><bean:message key="module.system.list.position"/></th>
			</tr>
		<logic:iterate id="data" name="panel_entities" scope="page">
			<tr>
				<td colspan="2" class="main"><bean:message name="data" property="messageKey" scope="page"/></td>
			</tr>
			<logic:iterate id="data2" name="data" property="properties" scope="page">
			<tr onmouseover="this.className='highlight';" onmouseout="this.className=''">
				<td class="sub">
					<common:label property="<%="position["+TagUtility.lookup(pageContext, "data2", "position", "page")+"]" %>" errorStyleId="error">
					<bean:message name="data2" property="messageKey" scope="page"/>
					</common:label>
				</td>
				<td><html:text property="<%="position["+TagUtility.lookup(pageContext, "data2", "position", "page")+"]" %>" styleId="<%="position["+TagUtility.lookup(pageContext, "data2", "position", "page")+"]" %>" style="width: 50px; text-align: center;"/></td>
			</tr>
			</logic:iterate>
		</logic:iterate>
		</table>
		</fieldset>
		
	</panel:edit>	
	</tiles:put>
</tiles:insert>
