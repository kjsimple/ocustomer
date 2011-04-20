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
   - Thomas Bader (Bader & Jene Software-Ingenieurb�ro).
   - Portions created by the Initial Developer are Copyright (C) 2005
   - the Initial Developer. All Rights Reserved.
   -
   - Contributor(s):
   -   Thomas Bader <thomas.bader@bader-jene.de>
   -
   - ***** END LICENSE BLOCK ***** --%>

<%@ include file="/tiles/include.jsp" %>
   
<%@ page import="org.opencustomer.webapp.Globals" %>
<%@ page import="org.apache.struts.taglib.TagUtils" %>

<logic:iterate id="item" name="<%=Globals.MENU_KEY%>" property="items" scope="session">
	<html:link action="/menu" paramId="menuId" paramName="item" paramProperty="id" paramScope="page">	
		<html:img styleClass="button" altKey='<%=String.valueOf(TagUtils.getInstance().lookup(pageContext, "item", "messageKey", "page"))%>' titleKey='<%=String.valueOf(TagUtils.getInstance().lookup(pageContext, "item", "messageKey", "page"))%>' pageKey='<%=String.valueOf(TagUtils.getInstance().lookup(pageContext, "item", "imageKey", "page"))%>'/>
	</html:link>
</logic:iterate>
<html:link styleClass="buttonLogout" action="/logout"><html:img styleClass="button" altKey="navigation.exit.text" titleKey="navigation.exit.text" pageKey="navigation.exit.url"/></html:link>
