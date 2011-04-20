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

package org.opencustomer.webapp.module.calendar.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.dao.calendar.EventCalendarDAO;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventPersonVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.util.EnumUtility;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditPageAction;

public class PageParticipiantsAction extends EditPageAction<PageParticipiantsForm>
{
    private static Logger log = Logger.getLogger(PageParticipiantsAction.class);
    
    @Override
    public void writeForm(PageParticipiantsForm form, ActionMessages errors, HttpServletRequest request)
    {        
        EventVO event = (EventVO) getPanel().getEntity();
        
        form.setUnknownParticipiants(event.getUnknownParticipiants());
        
        updateView(form, errors, request);
        
        List<EventPersonVO> eventPersons = (List<EventPersonVO>)getPanel().getAttribute("eventPersons");
        
        form.getInvitationStatus().clear();
        for(EventPersonVO eventPerson : eventPersons)
            form.getInvitationStatus().add(eventPerson.getInvitationStatus().toString());

        if(log.isDebugEnabled())
            log.debug("write form (form:"+form.getInvitationStatus().size()+"/panel:"+eventPersons.size()+")");
    }
    
    @Override
    public void readForm(PageParticipiantsForm form, ActionMessages errors, HttpServletRequest request)
    {
        EventVO event = (EventVO) getPanel().getEntity();
        
        event.setUnknownParticipiants(form.getUnknownParticipiants());
        
        List<EventPersonVO> eventPersons = (List<EventPersonVO>)getPanel().getAttribute("eventPersons");
        for(int i=0; i<form.getInvitationStatus().size(); i++)
        {
            String value = form.getInvitationStatus(i);
            eventPersons.get(i).setInvitationStatus(EnumUtility.valueOf(EventPersonVO.InvitationStatus.class, form.getInvitationStatus(i)));    
        }
        
        if(log.isDebugEnabled())
            log.debug("read form (form:"+form.getInvitationStatus().size()+"/panel:"+eventPersons.size()+")");
    }

    protected void updateView(PageParticipiantsForm form, ActionMessages errors, HttpServletRequest request) {
        if(log.isDebugEnabled())
            log.debug("post operations");
        
        EventVO event = (EventVO)getPanel().getEntity();
        
        ArrayList<EventCalendarVO> eventCalendars = new ArrayList<EventCalendarVO>(event.getEventCalendars());
        Collections.sort(eventCalendars, Utils.getComparatorForEventCalendar());
        getPanel().setAttribute("eventCalendars", eventCalendars);
        
        ArrayList<EventPersonVO> eventPersons = new ArrayList<EventPersonVO>(event.getEventPersons());
        Collections.sort(eventPersons, Utils.getComparatorForEventPerson());
        getPanel().setAttribute("eventPersons", eventPersons);
    }
    
    @Override
    public ActionForward handleCustomAction(ActionMapping mapping, PageParticipiantsForm form, ActionMessages errors, HttpServletRequest request)
    {
        if (log.isDebugEnabled())
            log.debug("handle custom action");
        
        if(form.getDoAddPerson().isSelected())
        {
            if (log.isDebugEnabled())
                log.debug("add person");
            
            return mapping.findForward("addPerson");
        }
        else if(form.getDoRemovePerson() > 0)
        {
            if (log.isDebugEnabled())
                log.debug("remove person with id: "+form.getDoRemovePerson());
            
            Set<EventPersonVO> eventPersons = ((EventVO)getPanel().getEntity()).getEventPersons();
            
            int pos = 0;
            Iterator<EventPersonVO> it = eventPersons.iterator();
            while(it.hasNext()) {
                if(it.next().getPerson().getId().intValue() == form.getDoRemovePerson()) {
                    it.remove();
                    form.getInvitationStatus().remove(pos);
                    break;
                }
                pos++;
            }
            
            return Panel.getForward(getPanel().getActivePage().getAction(), request);
        }
        else if(form.getDoJumpPerson() > 0)
        {
            if (log.isDebugEnabled())
                log.debug("jump to person with id: "+form.getDoJumpPerson());
            
            request.setAttribute("external_person_id", form.getDoJumpPerson());
            
            return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
        }
        else if(form.getDoAddUser().isSelected())
        {
            if (log.isDebugEnabled())
                log.debug("add person");
            
            return mapping.findForward("addUser");
        }
        else if(form.getDoRemoveUser() > 0)
        {
            if (log.isDebugEnabled())
                log.debug("remove user with id: "+form.getDoRemoveUser());
            
            Set<EventCalendarVO> eventCalendars = ((EventVO)getPanel().getEntity()).getEventCalendars();
            
            Iterator<EventCalendarVO> it = eventCalendars.iterator();
            while(it.hasNext()) {
                int userId = it.next().getCalendar().getUser().getId().intValue();
                if(userId == form.getDoRemoveUser()) {
                    it.remove();
                    break;
                }
            }
            
            return Panel.getForward(getPanel().getActivePage().getAction(), request);
        }
        else if(form.getDoJumpUser() > 0)
        {
            if (log.isDebugEnabled())
                log.debug("jump to person with id: "+form.getDoJumpUser());
            
            request.setAttribute("external_user_id", form.getDoJumpUser());
            
            return Panel.getForward(getPanel().getAction(Action.Type.SAVE).getAction(), request);
        }
        else if(form.getDoStatusNew().isSelected() 
                || form.getDoStatusAccept().isSelected() 
                || form.getDoStatusReject().isSelected() 
                || form.getDoStatusDelete().isSelected()) {
            if (log.isDebugEnabled())
                log.debug("change status");
            
            UserVO user = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
            CalendarVO calendar = (CalendarVO)getPanel().getAttribute("calendar");
            EventCalendarVO userEventCalendar = null;
            
            List<EventCalendarVO> eventCalendars = (List<EventCalendarVO>)getPanel().getAttribute("eventCalendars");
            for(EventCalendarVO eventCalendar : eventCalendars) {
                if(calendar.equals(eventCalendar.getCalendar())
                        && !eventCalendar.getParticipiantType().equals(EventCalendarVO.ParticipiantType.HOST)) {
                    userEventCalendar = eventCalendar;
                    break;
                }
            }
            
            if(userEventCalendar != null) {
                if(form.getDoStatusNew().isSelected())
                    userEventCalendar.setInvitationStatus(EventCalendarVO.InvitationStatus.NEW);
                else if(form.getDoStatusAccept().isSelected())
                    userEventCalendar.setInvitationStatus(EventCalendarVO.InvitationStatus.ACCEPTED);
                else if(form.getDoStatusReject().isSelected())
                    userEventCalendar.setInvitationStatus(EventCalendarVO.InvitationStatus.REJECTED);
                else if(form.getDoStatusDelete().isSelected())
                    userEventCalendar.setInvitationStatus(EventCalendarVO.InvitationStatus.DELETED);
                
                if(!getPanel().isEditable()) {
                    if(log.isDebugEnabled())
                        log.debug("save changes to event calendar without requesting save: "+userEventCalendar);
                    
                    try {
                        new EventCalendarDAO().update(userEventCalendar);
                    } catch(HibernateException e) {
                        log.error("could not save eventCalendar", e);
                    }
                }
            }
            
            return null;
        } else {
            if (log.isDebugEnabled())
                log.debug("no custom action found");
            
            return null;
        }
    }
}
