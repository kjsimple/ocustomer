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
 * Software-Ingenieurb�ro). Portions created by the Initial Developer are
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
import org.opencustomer.db.dao.crm.PersonDAO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventPersonVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.crm.PersonVO;
import org.opencustomer.framework.webapp.panel.EntityPanel;
import org.opencustomer.webapp.module.generic.add.AddPersonForm;

public final class AddPersonAction extends org.opencustomer.webapp.module.generic.add.AddPersonAction<AddPersonForm>
{
    private static Logger log = Logger.getLogger(AddPersonAction.class);

    @Override
    public String getPath() {
        return "/calendar/event/addPerson";
    }
    
    @Override
    protected void chooseEntity(EntityPanel panel, AddPersonForm form, ActionMessages errors, HttpServletRequest request, HttpServletResponse response)
    {

        if (log.isDebugEnabled())
            log.debug("add  person with ID: " + form.getId());

        PersonDAO dao = new PersonDAO();
        PersonVO person = dao.getById(form.getId());

        if (person == null)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", new Integer(form.getId())));

            log.error("problems loading person (ID:" + form.getId() + ")");
        }
        else
        {
            EventVO event = (EventVO) panel.getEntity();

            EventPersonVO eventPerson = new EventPersonVO();
            eventPerson.setEvent(event);
            eventPerson.setPerson(person);
            eventPerson.setInvitationStatus(EventPersonVO.InvitationStatus.NEW);
            
            if (!event.getEventPersons().contains(eventPerson))
                event.getEventPersons().add(eventPerson);
        }
    }
    
    @Override
    protected List<PersonVO> getIgnoredPersons(EntityPanel panel) {
        EventVO event = (EventVO) panel.getEntity();
        
        List<PersonVO> ignorePersons = new ArrayList<PersonVO>();
        
        // ignore persons already added to this event as user
        for(EventCalendarVO eventCalendar : event.getEventCalendars()) {
            if(eventCalendar.getCalendar().getUser() != null && eventCalendar.getCalendar().getUser().getPerson() != null) {
                ignorePersons.add(eventCalendar.getCalendar().getUser().getPerson());
            }
        }
        
        // ignore persons already added to this event as person
        for(EventPersonVO eventPerson : event.getEventPersons()) {
            if(eventPerson.getPerson().getUser() == null) {
                ignorePersons.add(eventPerson.getPerson());
            }
        }
        
        return ignorePersons;
    }
    
    @Override
    protected boolean isUserExcluded() {
        return true;
    }
}
