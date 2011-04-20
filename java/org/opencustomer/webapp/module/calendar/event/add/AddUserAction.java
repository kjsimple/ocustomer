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

package org.opencustomer.webapp.module.calendar.event.add;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventPersonVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.webapp.panel.EntityPanel;
import org.opencustomer.webapp.module.generic.add.AddUserForm;

public final class AddUserAction extends org.opencustomer.webapp.module.generic.add.AddUserAction<AddUserForm>
{
    private static Logger log = Logger.getLogger(AddUserAction.class);

    protected String getPath() {
        return "/calendar/event/addUser";
    }
    
    @Override
    protected void chooseEntity(EntityPanel panel, AddUserForm form, ActionMessages errors, HttpServletRequest request, HttpServletResponse response)
    {
        if (log.isDebugEnabled())
            log.debug("add calendar for user with ID: " + form.getId());

        UserVO user = new UserDAO().getById(form.getId());

        if (user == null || user.getCalendar() == null)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", form.getId()));

            log.error("problems loading calendar for user (ID:" + form.getId() + ")");
        }
        else
        {
            EventVO event = (EventVO) panel.getEntity();
            
            EventCalendarVO eventCalendar = new EventCalendarVO();
            eventCalendar.setEvent(event);
            eventCalendar.setCalendar(user.getCalendar());
            eventCalendar.setInvitationStatus(EventCalendarVO.InvitationStatus.NEW);
            eventCalendar.setParticipiantType(EventCalendarVO.ParticipiantType.GUEST);
            
            eventCalendar.setOwnerUser(user.getCalendar().getOwnerUser());
            eventCalendar.setOwnerGroup(user.getCalendar().getOwnerGroup());
            eventCalendar.setAccessUser(user.getCalendar().getAccessUser());
            eventCalendar.setAccessGroup(user.getCalendar().getAccessGroup());
            eventCalendar.setAccessGlobal(user.getCalendar().getAccessGlobal());
            
            if (!event.getEventCalendars().contains(eventCalendar))
                event.getEventCalendars().add(eventCalendar);
        }
    }
    
    @Override
    protected List<UserVO> getIgnoredUsers(EntityPanel panel)
    {
        EventVO event = (EventVO) panel.getEntity();
        
        List<UserVO> ignoredUsers = new ArrayList<UserVO>();
        for(EventCalendarVO eventCalendar : event.getEventCalendars()) {
            if(eventCalendar.getCalendar().getUser() != null)
                ignoredUsers.add(eventCalendar.getCalendar().getUser());
        }
        
        for(EventPersonVO eventPerson : event.getEventPersons()) {
            if(eventPerson.getPerson().getUser() != null)
                ignoredUsers.add(eventPerson.getPerson().getUser());
        }
        
        return ignoredUsers;
    }
}
