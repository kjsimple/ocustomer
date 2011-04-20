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
   
<%@ page import="org.opencustomer.webapp.Globals" %>
<%@ page import="org.apache.struts.taglib.TagUtils" %>

<logic:iterate id="item" name="<%=Globals.MENU_KEY%>" property="items" scope="session">
<logic:equal name="item" property="active" scope="page" value="true">
	<bean:define name="item" scope="page" id="activeItem" toScope="page"/>
</logic:equal>
</logic:iterate>
<logic:present name="activeItem" scope="page">
<logic:iterate id="item" name="activeItem" property="childItems" scope="page">
<logic:equal name="item" property="active" scope="page" value="true">
	<bean:define name="item" scope="page" id="active2Item" toScope="page"/>
</logic:equal>
</logic:iterate>
<logic:iterate id="item" name="active2Item" property="childItems" scope="page">
<html:link action="/menu" paramId="menuId" paramName="item" paramProperty="id" paramScope="page">
	<bean:message name="item" property="messageKey" scope="page"/>
</html:link>
</logic:iterate>
</logic:present>