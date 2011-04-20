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
 *                 Felix Breske <felix.breske@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.connector.ical;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.ValidationException;

import org.apache.log4j.Logger;
import org.apache.slide.security.AccessDeniedException;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.dao.calendar.EventCalendarDAO;
import org.opencustomer.db.dao.calendar.EventDAO;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO.InvitationStatus;
import org.opencustomer.db.vo.calendar.EventCalendarVO.ParticipiantType;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess.Access;

/**
 * The class Ical handels the import and export of iCalendar files to OpenCustomer.
 * 
 * @author fbreske
 *
 */

public class Ical
{
    private static Logger log = Logger.getLogger(Ical.class);
    
    private static Ical INSTANCE = new Ical();
    
    public static enum EventHandling
    {
        ADD,
        DELETE;
    }
    
    public static Ical getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * This method exports a CalendaVO to an iCalendar file.
     * If there is only read access an the event for this user, the event is marked as "read only".
     * @param calendar the calendar witch will be exported
     * @param user permission for this user will be check
     * @param out the OutputStream vor the export
     * @throws IOException
     * @throws ValidationException
     * @throws ParseException
     */
    public void exportIcal(CalendarVO calendar, UserVO user , OutputStream out) throws IOException, ValidationException, ParseException
    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("WebResources", user.getLocale());
        
        List<EventCalendarVO> eventcalendars = new EventCalendarDAO().getByCalendar(calendar, true);
        List<EventVO> eventList = new ArrayList<EventVO>();
        
        EventVO event = null;
            
        for(EventCalendarVO eventcalendar : eventcalendars)
        {
            event = eventcalendar.getEvent();
            if(EntityAccessUtility.isAccessGranted(user,eventcalendar,Access.READ) && !EntityAccessUtility.isAccessGranted(user,eventcalendar,Access.WRITE)){
                event.setTitle(event.getTitle() + " ["+resourceBundle.getString("entity.calendar.event.readonly")+"] ");
                if(log.isDebugEnabled())
                    log.debug("event readonly: " + event);
            }
            eventList.add(event);
        }
        
        IcalParser.getInstance().toIcal(eventList, out);
    }
    
    /**
     * Imports an iCalendar file to an OpenCustomer calendar. If there is no write access for this user on the calendar,
     * a AccessDeniedException will be thrown.
     * 
     * If EventHandling.ADD is set, the imported events be added to the calendar. Events which exists only in the OpenCustomer calendar, will not be touched.
     * IF EventHandling.DELETE is set, events which exists only in the OpenCustomer calendar will be deleted.
     * @param ical the imported iCalendar file
     * @param calendar the calendar for the import
     * @param user the user who imports the file
     * @param handling EventHandling.ADD vor adding only, EventHandling.DELETE for adding and deleting events
     * @throws IOException
     * @throws ParserException
     * @throws ParseException
     * @throws AccessDeniedException thown if there is no write access for this user to the calendar or event.
     */
    public void importIcal(InputStream ical, CalendarVO calendar, UserVO user, EventHandling handling) throws IOException, ParserException, ParseException, AccessDeniedException
    {
        EventDAO eventdao = new EventDAO();
        EventCalendarDAO eventcalendardao = new EventCalendarDAO();

        List<EventVO> events = IcalParser.getInstance().fromIcal(ical);
        List<EventVO> insertList = new ArrayList<EventVO>();
        for(EventVO event : events)
        {
            if(log.isDebugEnabled())
                log.debug("start import: " + event);
            
            if(event.getId() != null)  // parser found oc id
            {     
                EventVO event2 = eventdao.getById(event.getId());
                if(event2 != null)
                {
                    EventCalendarVO hostEventCalendar = null;
                    EventCalendarVO myEventCalendar = null;
                    
                    for(EventCalendarVO eventCalendar : event2.getEventCalendars()) { // search for myEventCalendar
                        if(calendar.equals(eventCalendar.getCalendar())) {
                            myEventCalendar = eventCalendar;
                        }
                    }
                    
                    for(EventCalendarVO eventCalendar : event2.getEventCalendars()) { // search for the hostEventCalendar entity
                        if(EventCalendarVO.ParticipiantType.HOST.equals(eventCalendar.getParticipiantType())) {
                            hostEventCalendar = eventCalendar;
                        }
                    }
                    
                    if(EntityAccessUtility.isAccessGranted(user,myEventCalendar,Access.WRITE) 
                            && EntityAccessUtility.isAccessGranted(calendar.getUser(),hostEventCalendar,Access.WRITE)) // write access ?
                    {
                        event2.setDescription(event.getDescription());
                        event2.setTitle(event.getTitle());
                        event2.setAllDay(event.isAllDay());
                        event2.setEndDate(event.getEndDate());
                        event2.setStartDate(event.getStartDate());
                        event2.setRecurrenceCycle(event.getRecurrenceCycle());
                        event2.setRecurrenceCycleUnit(event.getRecurrenceCycleUnit());
                        event2.setRecurrenceEndDate(event.getRecurrenceEndDate());
                        event2.setRecurrenceStartDate(event.getRecurrenceStartDate());
                        event2.setRecurrenceInMonth(event.getRecurrenceInMonth());
                        event2.setRecurrenceInWeek(event.getRecurrenceInWeek());
                        event2.setRecurrenceNumberOfTimes(event.getRecurrenceNumberOfTimes());
                        event2.setRecurrenceType(event.getRecurrenceType());
                        event2.setRecurrenceUntilDate(event.getRecurrenceUntilDate());
                                               
                        if(log.isDebugEnabled())
                            log.debug("event updated: " + event2);
                        
                        eventdao.update(event2);
                        insertList.add(event2);
                    }
                    else
                    {
                        if(log.isDebugEnabled())
                            log.debug("event update denied: " + event2);
                        insertList.add(event2);
                        throw new AccessDeniedException(calendar.getName(),user.getUserName(),"WRITE");
                    }
                }
                else
                {
                    if(log.isInfoEnabled())
                        log.info("event " + event.getId() + " not found, id incorrect?");
                }
            }
            else
            {      
                // create new Event, no eventid found
                if(EntityAccessUtility.isAccessGranted(user,calendar,Access.WRITE))
                {
                    EventCalendarVO ecvo = new EventCalendarVO(event,calendar);
                    ecvo.setParticipiantType(ParticipiantType.HOST);
                    ecvo.setInvitationStatus(InvitationStatus.ACCEPTED);
                    
                    ecvo.setOwnerUser(calendar.getOwnerUser());
                    ecvo.setOwnerGroup(calendar.getOwnerGroup());
                    ecvo.setAccessUser(calendar.getAccessUser());
                    ecvo.setAccessGroup(calendar.getAccessGroup());
                    ecvo.setAccessGlobal(calendar.getAccessGlobal());
                    
                    event.getEventCalendars().add(ecvo);
                    
                    if(log.isDebugEnabled())
                        log.debug("new event created: " + event);
                    
                    eventdao.insert(event);
                    insertList.add(event);
                                       
                }
                else
                {
                    if(log.isDebugEnabled())
                        log.debug("create event denied: " + event);
                    
                    insertList.add(event);
                    throw new AccessDeniedException(calendar.getName(),user.getUserName(),"WRITE");
                }    
            }      
        }
        
        if(handling == EventHandling.DELETE)  // deleting events not in insertList
        {
            List<EventCalendarVO> actualList = eventcalendardao.getByCalendar(calendar, true);
            for(EventCalendarVO eventCalendar : actualList)
            {
                if(!insertList.contains(eventCalendar.getEvent()))
                {
                    if(log.isDebugEnabled())
                        log.debug("deleting event: " + eventCalendar.getEvent());
                    
                    eventdao.delete(eventCalendar.getEvent());     
                }           
            }
        }  
    }
}
