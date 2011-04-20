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
<%@ page import="org.opencustomer.db.vo.system.RightVO" %>

<tiles:insert definition="standard">
	<tiles:put name="page.title">
		<panel:title/>
	</tiles:put>
	<tiles:put name="page.content">
		
	<panel:attributes/>
	
	<script type="text/javascript">
	<!--
		function checkWrite(i) {
			if(document.getElementById("write_"+i).checked) {
				document.getElementById("read_"+i).checked = true;
			}
		}
		
		function checkRead(i) {
			if(document.getElementById("read_"+i).checked == false) {
				document.getElementById("write_"+i).checked = false;
			}
		}
	//-->
	</script>
	<html:errors/>
		
	<panel:edit>
		<panel:editable condition="false">
		</panel:editable>
		
		<fieldset class="unit">
			<table class="rights">
				<colgroup>
					<col width="3*"/>
					<col width="1*"/>
					<col width="1*"/>
					<col width="2*"/>
				</colgroup>
				<tr>
					<th><bean:message key="entity.system.right"/></th>
					<th><bean:message key="entity.system.right.type.read"/></th>
					<th><bean:message key="entity.system.right.type.write"/></th>
					<th><bean:message key="entity.system.right.type.other"/></th>
				</tr>
			<logic:iterate id="area" name="panel_rightAreas" scope="page">
				<tr>
					<td colspan="4" class="main"><bean:message name="area" property="namekey" scope="page"/></td>
				</tr>
			<logic:iterate id="group" name="area" property="rightGroups" scope="page">
				<tr onmouseover="this.className='highlight';" onmouseout="this.className=''">
					<td class="sub"><bean:message name="group" property="namekey" scope="page"/></td>
					<td>
						<logic:iterate id="right" name="group" property="rights" scope="page">
						<logic:equal name="right" property="type" scope="page" value="<%=RightVO.Type.READ.toString()%>">
						<html:multibox property="rights" value="<%=TagUtility.lookupString(pageContext, "right", "id", "page", true)%>" styleId="<%="read_"+TagUtility.lookupString(pageContext, "right", "rightGroup.id", "page", true)%>" onchange="<%="checkRead("+TagUtility.lookupString(pageContext, "right", "rightGroup.id", "page", true)+")"%>"/>
						</logic:equal>
						</logic:iterate>
					</td>
					<td>
						<logic:iterate id="right" name="group" property="rights" scope="page">
						<logic:equal name="right" property="type" scope="page" value="<%=RightVO.Type.WRITE.toString()%>">
						<html:multibox property="rights" value="<%=TagUtility.lookupString(pageContext, "right", "id", "page", true)%>" styleId="<%="write_"+TagUtility.lookupString(pageContext, "right", "rightGroup.id", "page", true)%>" onchange="<%="checkWrite("+TagUtility.lookupString(pageContext, "right", "rightGroup.id", "page", true)+")"%>"/>
						</logic:equal>
						</logic:iterate>
					</td>
					<td>
						<logic:iterate id="right" name="group" property="rights" scope="page">
						<logic:equal name="right" property="type" scope="page" value="<%=RightVO.Type.OTHER.toString()%>">
						<table>
							<tr>
								<td style="width: 20px;">
									<html:multibox property="rights" value="<%=TagUtility.lookupString(pageContext, "right", "id", "page", true)%>"/>
								</td>
								<td>
									<bean:message name="right" property="namekey" scope="page"/>
								</td>
							</tr>
						</table>
						</logic:equal>
						</logic:iterate>
					</td>
				</tr>
			</logic:iterate>
			</logic:iterate>
			</table>
		</fieldset>
	</panel:edit>
	
	</tiles:put>
</tiles:insert>
