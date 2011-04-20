/*******************************************************************************
 * ***** BEGIN LICENSE BLOCK Version: MPL 1.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is the OpenCustomer CRM.
 * 
 * The Initial Developer of the Original Code is Thomas Bader (Bader & Jene
 * Software-Ingenieurbüro). Portions created by the Initial Developer are
 * Copyright (C) 2005 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.webapp.module.calendar.calendar;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditLoadAction;
import org.opencustomer.webapp.action.EditLoadForm;
import org.opencustomer.webapp.auth.Right;

public class LoadAction extends EditLoadAction<EditLoadForm>
{
    private static Logger log = Logger.getLogger(LoadAction.class);

    @Override
    public EditPanel createPanel(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        UserVO activeUser = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        
        CalendarVO calendar = (CalendarVO)attributes.get("calendar");

        EditPanel panel = new EditPanel(Right.CALENDAR_CALENDAR_WRITE, calendar);
        
        String calendarName = MessageUtil.message(request, "module.calendar.personalCalendar"); 
        if(calendar.getUser() == null) {
            calendarName = calendar.getName();
        } else if(!calendar.getUser().equals(activeUser)) {
            calendarName = calendar.getUser().getUserName();
        }
        
        panel.setTitle(MessageUtil.message(request, "module.calendar.edit.headline", calendarName));
        
        panel.addAction(Action.Type.SAVE, "/calendar/edit/save");
        
        panel.addPage("STANDARD", "/calendar/edit/pageStandard", "module.generic.panel.tab.standard");
        
        return panel;
    }
    
    @Override
    public void loadEntity(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        Panel parentPanel = Panel.getPanelStack(request).peek();
        
        attributes.put("calendar", parentPanel.getAttribute("calendar"));
    }
    
    @Override
    public void createEntity(ActionMessages errors, EditLoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        loadEntity(errors, form, attributes, request, response);
    }
}
