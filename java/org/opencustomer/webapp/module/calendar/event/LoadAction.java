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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.Hibernate;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.dao.calendar.EventDAO;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;
import org.opencustomer.framework.webapp.panel.Action;
import org.opencustomer.framework.webapp.panel.EditPanel;
import org.opencustomer.framework.webapp.panel.Panel;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.Globals;
import org.opencustomer.webapp.action.EditLoadAction;
import org.opencustomer.webapp.auth.Right;
import org.opencustomer.webapp.module.crm.company.DeleteAction;

public class LoadAction extends EditLoadAction<LoadForm> {
    private static Logger log = Logger.getLogger(LoadAction.class);

    @Override
    public EditPanel createPanel(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        EventVO event = (EventVO)attributes.get("event");

        EditPanel panel = new EditPanel(Right.CALENDAR_CALENDAR_WRITE, event);
        
        if(event.getId() == null) {
            panel.setTitle(MessageUtil.message(request, "module.calendar.event.headLine.create"));
        } else {
            panel.setTitle(MessageUtil.message(request, "module.calendar.event.headLine.edit", event.getTitle()));
            
            panel.setAttribute(DeleteAction.TEXT_TITLE, MessageUtil.message(request, "module.calendar.event.delete.headLine", event.getTitle()));
            panel.setAttribute(DeleteAction.TEXT_QUESTION, MessageUtil.message(request, "module.calendar.event.delete.question", event.getTitle()));
        }
        
        boolean contentEditable = isContentEditable(panel, (CalendarVO)attributes.get("calendar"), request);
        
        if(contentEditable) {
            panel.addAction(Action.Type.DELETE, "/calendar/event/delete");
        }
        panel.addAction(Action.Type.SAVE, "/calendar/event/save");
        
        panel.addPage("STANDARD",   "/calendar/event/pageStandard", "module.generic.panel.tab.standard", true, contentEditable ? null : Boolean.FALSE);
        panel.addPage("PARTICIPIANTS", "/calendar/event/pageParticipiants", "module.calendar.event.pageParticpiants", true, contentEditable ? null : Boolean.FALSE);
        panel.addPage("RECURRENCE", "/calendar/event/pageRecurrence", "module.calendar.event.pageRecurrence", true, contentEditable ? null : Boolean.FALSE);
        panel.addPage("SYSTEM",     "/calendar/event/pageSystem", "module.generic.panel.tab.system");
        
        return panel;
    }
    
    @Override
    public void loadEnvironment(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        attributes.put("calendar", Panel.getPanelStack(request).peek().getAttribute("calendar"));
    }
    
    @Override
    public void loadEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            EventVO event = new EventDAO().getById(form.getId());
            Hibernate.initialize(event.getEventCalendars());
            Hibernate.initialize(event.getEventPersons());
            
            attributes.put("event", event);
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("default.error.invalidEntity", form.getId()));

            log.debug("problems loading event (ID:" + form.getId() + ")");
        }
    }
    
    @Override
    protected boolean validateAccess(HttpServletRequest request, EditPanel panel) {
        UserVO user = (UserVO) request.getSession().getAttribute(Globals.USER_KEY);
        CalendarVO calendar = (CalendarVO)panel.getAttribute("calendar");
        
        EventCalendarVO myEventCalendar = null;
        
        EventVO event = (EventVO)panel.getEntity();

        for(EventCalendarVO eventCalendar : event.getEventCalendars()) {
            if(calendar.equals(eventCalendar.getCalendar())) {
                myEventCalendar = eventCalendar;
            }
        }
        
        if(myEventCalendar != null) {
            return EntityAccessUtility.isAccessGranted(user, myEventCalendar, EntityAccess.Access.READ);
        } else {
            return false;
        }
    }
    
    @Override
    public void createEntity(ActionMessages errors, LoadForm form, Hashtable<String, Object> attributes, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        CalendarVO calendar = (CalendarVO) Panel.getPanelStack(request).peek().getAttribute("calendar");
        
        // create event
        EventVO event = new EventVO();

        Date choosenDate = null;
        if(form.getDate() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                choosenDate = sdf.parse(form.getDate());
            } catch(ParseException e) {
                log.error("invalid date found ()", e);
            }
        }
        Calendar cal = GregorianCalendar.getInstance();
        if(choosenDate != null) {
            Calendar cal2 = GregorianCalendar.getInstance();
            cal2.setTime(choosenDate);
            cal.set(Calendar.DAY_OF_MONTH, cal2.get(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.MONTH, cal2.get(Calendar.MONTH));
            cal.set(Calendar.YEAR, cal2.get(Calendar.YEAR));
        }
        event.setStartDate(cal.getTime());
        cal.add(Calendar.MINUTE, 30);
        event.setEndDate(cal.getTime());
        event.setRecurrenceType(EventVO.RecurrenceType.NONE);
        event.setOccupied(false);
        
        // create event calendar
        EventCalendarVO eventCalendar = new EventCalendarVO();
        eventCalendar.setCalendar(calendar);
        eventCalendar.setEvent(event);
        eventCalendar.setParticipiantType(EventCalendarVO.ParticipiantType.HOST);
        eventCalendar.setInvitationStatus(EventCalendarVO.InvitationStatus.ACCEPTED);
        eventCalendar.setOwnerUser(calendar.getOwnerUser());
        eventCalendar.setOwnerGroup(calendar.getOwnerGroup());
        eventCalendar.setAccessUser(calendar.getAccessUser());
        eventCalendar.setAccessGroup(calendar.getAccessGroup());
        eventCalendar.setAccessGlobal(calendar.getAccessGlobal());
        
        event.getEventCalendars().add(eventCalendar);
        
        attributes.put("event", event);
    }

    @Override
    protected boolean isEditable(EditPanel panel, HttpServletRequest request)
    {
        UserVO user         = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);
        EventVO event       = (EventVO)panel.getEntity();
        CalendarVO calendar = (CalendarVO)panel.getAttribute("calendar");

        // active event calendar
        EventCalendarVO activeEventCalendar = null;
        for(EventCalendarVO eventCalendar : event.getEventCalendars()) {
            if(calendar.equals(eventCalendar.getCalendar())) {
                activeEventCalendar = eventCalendar;
            }
        }
        
        boolean participientCalendar = EntityAccessUtility.isAccessGranted(user, calendar, EntityAccess.Access.WRITE);
        boolean participientEvent    = EditPanel.checkEditable(request, panel, activeEventCalendar);
        
        if(log.isDebugEnabled()) {
            log.debug("editable(panel): participientCalendar="+participientCalendar+", participientEvent="+participientEvent);
        }
        
        return participientCalendar && participientEvent;
    }
        
    private boolean isContentEditable(EditPanel panel, CalendarVO calendar, HttpServletRequest request) {
        EventVO event = (EventVO)panel.getEntity();
        UserVO user   = (UserVO)request.getSession().getAttribute(Globals.USER_KEY);

        EventCalendarVO hostEventCalendar   = null;
        EventCalendarVO activeEventCalendar = null;
        
        // identify the host calendar
        for(EventCalendarVO eventCalendar : event.getEventCalendars()) {
            if(EventCalendarVO.ParticipiantType.HOST.equals(eventCalendar.getParticipiantType())) {
                hostEventCalendar = eventCalendar;
            } 
            if(eventCalendar.getCalendar().equals(calendar)) {
                activeEventCalendar = eventCalendar;
            }
        }
        
        if(hostEventCalendar.equals(activeEventCalendar)) {
            if(log.isDebugEnabled())
                log.debug("see event as host");
            
            return EntityAccessUtility.isAccessGranted(user, hostEventCalendar.getCalendar(), EntityAccess.Access.WRITE) 
                && EditPanel.checkEditable(request, panel, hostEventCalendar, user);
        } else {
            if(log.isDebugEnabled())
                log.debug("see event as participient");

            boolean hostCalendar = EntityAccessUtility.isAccessGranted(calendar.getUser(), hostEventCalendar.getCalendar(), EntityAccess.Access.WRITE);  
            boolean hostEvent = EditPanel.checkEditable(request, panel, hostEventCalendar, calendar.getUser());
            boolean participientCalendar = EntityAccessUtility.isAccessGranted(user, activeEventCalendar.getCalendar(), EntityAccess.Access.WRITE);
            boolean participientEvent = EditPanel.checkEditable(request, panel, activeEventCalendar, user);
            
            if(log.isDebugEnabled()) {
                log.debug("editable(content): hostCalendar="+hostCalendar+", hostEvent="+hostEvent+", participientCalendar="+participientCalendar+", participientEvent="+participientEvent);
            }
            
            return hostCalendar && hostEvent && participientCalendar && participientEvent;
        }
    }
}
