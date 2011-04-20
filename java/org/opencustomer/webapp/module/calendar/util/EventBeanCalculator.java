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
 * Copyright (C) 2006 the Initial Developer. All Rights Reserved.
 * 
 * Contributor(s): Thomas Bader <thomas.bader@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.webapp.module.calendar.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.opencustomer.db.EntityAccessUtility;
import org.opencustomer.db.EventUtility;
import org.opencustomer.db.dao.calendar.EventDAO;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class EventBeanCalculator
{
    private static final Logger log = Logger.getLogger(EventBeanCalculator.class);
    
    private CalendarVO calendar;

    private UserVO user;

    private Date startDate;

    private Date endDate;
    
    private List<EventBean> eventBeans;
    
    private boolean withReminderDate = false; // fbreske

    public EventBeanCalculator(CalendarVO calendar, Date startDate, Date endDate) {
        this(calendar, null, startDate, endDate);
    }
    
    public EventBeanCalculator(CalendarVO calendar, UserVO user, Date startDate, Date endDate)
    {
        this.calendar = calendar;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        
        readEvents();
    }
    
    private void readEvents() {
        List<EventVO> events = new EventDAO().getByTimePeriod(calendar, startDate, endDate, user, withReminderDate);
        
        eventBeans = calculateEventBeans(events);
    }
    
    public List<EventBean> getEventBeans() {
        return eventBeans;
    }
    
    private List<EventBean> calculateEventBeans(List<EventVO> events)
    {
        List<EventBean> beans = new ArrayList<EventBean>();
        
        for(EventVO event : events)
        {
            Hibernate.initialize(event.getEventCalendars());
            Hibernate.initialize(event.getEventPersons());
            
            EventBean mainBean = new EventBean();
            mainBean.setStartDate(event.getStartDate());
            mainBean.setEndDate(event.getEndDate());
            mainBean.setReminderDate(event.getReminderDate());
            mainBean.setEvent(event);
            
            EventCalendarVO myEventCalendar = null;
            for(EventCalendarVO eventCalendar : event.getEventCalendars()) {
                if(calendar.equals(eventCalendar.getCalendar())) {
                    myEventCalendar = eventCalendar;
                }
            }
            mainBean.setType(myEventCalendar.getParticipiantType());
            mainBean.setStatus(myEventCalendar.getInvitationStatus());
            
            if(user != null) {
                if(myEventCalendar != null) {
                    mainBean.setReadable(EntityAccessUtility.isAccessGranted(user, myEventCalendar, EntityAccess.Access.READ));
                } else {
                    log.warn("missing eventCalendar for calendar: "+calendar);
                }
            }
            
            beans.add(mainBean);
            
            if(!EventVO.RecurrenceType.NONE.equals(event.getRecurrenceType()))
                beans.addAll(EventUtility.calculateRecurrences(calendar, endDate, event, mainBean.isReadable()));
        }
        
        return beans;
    }
    
    public void setWithReminderDate(boolean withReminderDate)
    {
        this.withReminderDate = withReminderDate;
        readEvents();
    }
}
