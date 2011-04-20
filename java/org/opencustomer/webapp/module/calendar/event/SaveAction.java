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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.HibernateException;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.dao.calendar.EventDAO;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO.InvitationStatus;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditSaveAction;
import org.opencustomer.webapp.module.calendar.util.EventBean;
import org.opencustomer.webapp.module.calendar.util.EventBeanCalculator;

public class SaveAction extends EditSaveAction
{
    private static Logger log = Logger.getLogger(SaveAction.class);
    
    @Override
    protected void saveEntity(EditPanel panel, ActionMessages errors, HttpServletRequest request)
    {
        UserVO user = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        
        EventVO event = (EventVO) panel.getEntity();
        try
        {
            HibernateContext.getSession().clear();
            
            HibernateContext.beginTransaction();
            
            if (event.getId() == null)
            {
                if (log.isDebugEnabled())
                    log.debug("create event");
                new EventDAO().insert(event);
            }
            else
            {
                if (log.isDebugEnabled())
                    log.debug("save event (ID:" + event.getId() + ")");
                new EventDAO().update(event);
            }
            
            HibernateContext.commitTransaction();
        }
        catch (HibernateException e)
        {
            HibernateContext.rollbackTransaction();
            
            log.error("problems saving event", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.rollback"));
        }
        
    }
    
    @Override
    protected boolean validateData(EditPanel panel, ActionMessages errors, HttpServletRequest request)
    {
        UserVO user            = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        EventVO event          = (EventVO) panel.getAttribute("event");
        CalendarVO calendar    = (CalendarVO) panel.getAttribute("calendar");
        
        EntityAccess testEntity = null;
        if (event.getId() == null)
            testEntity = calendar;
        else {
            EventCalendarVO myEventCalendar = null;
            
            for(EventCalendarVO eventCalendar : event.getEventCalendars()) {
                if(calendar.equals(eventCalendar.getCalendar())) {
                    testEntity = eventCalendar;
                    break;
                }
            }
            
        }
        if (!EntityAccessUtility.isAccessGranted(user, testEntity, EntityAccess.Access.WRITE))
            errors.add("globalAccess", new ActionMessage("default.error.noEntityAccess"));

        if(event.isOccupied()) { // validiere Termin, wenn dieser besetzt ist
            for(EventCalendarVO eventCalendar : event.getEventCalendars()) { // prüfe jeden Kalender, von dem es Beteiligte gibt
                if(log.isDebugEnabled())
                    log.debug("validate calendar with id="+eventCalendar.getCalendar().getId());
                
                EventBeanCalculator calc = new EventBeanCalculator(eventCalendar.getCalendar(), event.getStartDate(), event.getEndDate());
                for(EventBean eventBean : calc.getEventBeans()) {
                    if(!eventBean.getEvent().equals(event) 
                            && eventBean.getEvent().isOccupied()
                            && isEventValid(eventBean.getEvent(), eventCalendar.getCalendar())) { // muß der Termin geprüft werden? (abgelehnte und gelöschte interessieren nicht)
                        if(DateUtility.isMatching(eventBean.getStartDate(), eventBean.getEndDate(), event.getStartDate(), event.getEndDate())) {
                            if(eventCalendar.getCalendar().getUser() != null) {
                                if(eventCalendar.getCalendar().getUser().equals(user)) {
                                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.calendar.event.error.isOccupied"));
                                } else {
                                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.calendar.event.error.userIsOccupied", eventCalendar.getCalendar().getUser().getUserName()));
                                }
                            } else {
                                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("module.calendar.event.error.calendarIsOccupied", eventCalendar.getCalendar().getName()));
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        if (errors.isEmpty())
            return true;
        else
            return false;
    }

    private boolean isEventValid(EventVO event, CalendarVO calendar) {
        EventCalendarVO eventCalendar = null;
        
        for(EventCalendarVO ec : event.getEventCalendars()) {
            if(ec.getCalendar().equals(calendar)) {
                eventCalendar = ec;
                break;
            }
        }
        
        return eventCalendar.getInvitationStatus().equals(InvitationStatus.ACCEPTED) 
            || eventCalendar.getInvitationStatus().equals(InvitationStatus.NEW);
    }
    
    @Override
    protected ActionForward getActionForward(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getAttribute("external_user_id") != null)
            return mapping.findForward("jumpUser");
        else if(request.getAttribute("external_person_id") != null)
            return mapping.findForward("jumpPerson");
        else
            return null;
    }
}