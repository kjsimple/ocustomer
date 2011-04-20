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

package org.opencustomer.db.listener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.event.DeleteEvent;
import org.hibernate.event.DeleteEventListener;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.SaveOrUpdateEventListener;
import org.opencustomer.connector.mail.exception.NoValidSenderException;
import org.opencustomer.connector.mail.messages.JobReminderMail;
import org.opencustomer.connector.scheduling.SchedulingManager;
import org.opencustomer.connector.scheduling.jobs.MailJob;
import org.opencustomer.db.EventUtility;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.webapp.Settings;
import org.opencustomer.webapp.module.calendar.util.EventBean;

import com.jtheory.jdring.AlarmEntry;
import com.jtheory.jdring.PastDateException;

/**
 * This class listens to saved or delted EventVO or JobVO. If such an entity is saved, a ReminderMail and a ReminderMailJob
 * is created, and added to the SchedulingManager.
 * @author fbreske
 * @see org.opencustomer.connector.mail.messages.EventReminderMail
 * @see org.opencustomer.connector.mail.messages.JobReminderMail
 * @see org.opencustomer.connector.scheduling.jobs.MailJob
 *
 */
public class ReminderMailListener implements SaveOrUpdateEventListener, DeleteEventListener
{
    private static final long serialVersionUID = 5606056583886236327L;
    
    private static Logger log = Logger.getLogger(ReminderMailListener.class);

    /**
     * Listen to the saved or updated entities. Adds or changes a MailJob.
     */
    public void onSaveOrUpdate(SaveOrUpdateEvent saveOrUpdateEvent) throws HibernateException
    {
        if(!Settings.getInstance().isMailReminderEnabled())
        {
            if(log.isDebugEnabled())
                log.debug("reminder mail disabled");
            return;
        }
        
        if(saveOrUpdateEvent.getEntity() instanceof EventVO)
        {
            EventVO event = (EventVO) saveOrUpdateEvent.getEntity();
            CalendarVO mainCalendar = null;
            for(EventCalendarVO vo : event.getEventCalendars()) {
                if(EventCalendarVO.ParticipiantType.HOST.equals(vo.getParticipiantType())) {
                    mainCalendar = vo.getCalendar();
                }
            }
            
            
            Calendar startDate = GregorianCalendar.getInstance();
            Calendar endDate = GregorianCalendar.getInstance();
            endDate.set(startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),startDate.get(Calendar.DATE)+2,0,0);
            
            if(event.getReminderDate() != null && event.getReminderDate().after(startDate.getTime()) && event.getReminderDate().before(endDate.getTime()))
            {
                if(log.isDebugEnabled())
                    log.debug("reloading actual Jobs");
                SchedulingManager.getInstance().loadReminderJobsfromDB(startDate.getTime(), endDate.getTime());
            }
            else if(event.getReminderDate() != null && event.getRecurrenceType() != EventVO.RecurrenceType.NONE)
            {
                List<EventBean> events = EventUtility.calculateRecurrences(mainCalendar, endDate.getTime(), event, true);
                for(EventBean eventBean : events)
                {
                    if(eventBean.getReminderDate().after(startDate.getTime()) && eventBean.getReminderDate().before(endDate.getTime()))
                    {
                        if(log.isDebugEnabled())
                            log.debug("reloading actual Jobs");
                        SchedulingManager.getInstance().loadReminderJobsfromDB(startDate.getTime(), endDate.getTime());
                    }
                }
            }
            else
            {
                if(log.isInfoEnabled())
                    log.info("event ignored: " + event);
            }
        }
        else  if(saveOrUpdateEvent.getEntity() instanceof JobVO)
        {
            JobVO job = (JobVO)saveOrUpdateEvent.getEntity();
            SchedulingManager manager = SchedulingManager.getInstance();
            JobReminderMail reminderMail;
            try
            {
                reminderMail = new JobReminderMail();
                reminderMail.setUser(job.getAssignedUser());
            }
            catch (NoValidSenderException e1)
            {
                log.warn("no person for user: " + job.getAssignedUser());
                return;
            }
            catch (AddressException e1)
            {
                log.warn("cannot create reminder mail, incorrect sender address");
                return;
            }
            
            reminderMail.setJob(job);
            MailJob mailJob = new MailJob(reminderMail);
            
            for(Object obj :  manager.getAlarmManager().getAllAlarms())
            {
                AlarmEntry alarmEntry = (AlarmEntry) obj;
                String name;
                if(alarmEntry.getName().equals("JOB" + job.getId())){
                    if(log.isDebugEnabled())
                        log.debug("removing mail job for [job]: " + job);
                    manager.getAlarmManager().removeAlarm(alarmEntry);
                }
            }
            try
            {
                if(log.isInfoEnabled())
                    log.info("adding mailJob for [job]: " + job);
                String name;
                Calendar reminderDate = GregorianCalendar.getInstance();
                reminderDate.setTime(job.getDueDate());
                reminderDate.add(Calendar.MINUTE, -1 * Settings.getInstance().getAlarmBeforeJob());
                AlarmEntry alarmEntry = new AlarmEntry("JOB" + job.getId(),reminderDate.getTime(),mailJob);
                alarmEntry.setRingInNewThead();
                manager.getAlarmManager().addAlarm(alarmEntry);
            }
            catch (PastDateException e)
            {
                log.info("ignoring old job alarm: " + job);
            }
        }
    }

    /**
     * Deletes a MailJob if the entity is delted.
     */
    public void onDelete(DeleteEvent event) throws HibernateException {
        onDelete(event, null);
    }
    
    public void onDelete(DeleteEvent event, Set transientEntities) throws HibernateException {
        if(!Settings.getInstance().isMailReminderEnabled())
        {
            if(log.isDebugEnabled())
                log.debug("reminder mail disabled");
            return;
        }
        if(event.getObject() instanceof EventVO)
        {
            EventVO eventVO = (EventVO) event.getObject();
            SchedulingManager manager = SchedulingManager.getInstance();
            
            for(Object obj :  manager.getAlarmManager().getAllAlarms())
            {
                AlarmEntry alarmEntry = (AlarmEntry) obj;
                if(alarmEntry.getName().contains("EVENT"+eventVO.getId())){
                    if(log.isDebugEnabled())
                        log.debug("removing mail job for: " + eventVO);
                    manager.getAlarmManager().removeAlarm(alarmEntry);
                }
            }
        }
        else if(event.getObject() instanceof JobVO)
        {
            JobVO job = (JobVO) event.getObject();
            SchedulingManager manager = SchedulingManager.getInstance();
            
            for(Object obj :  manager.getAlarmManager().getAllAlarms())
            {
                AlarmEntry alarmEntry = (AlarmEntry) obj;
                if(alarmEntry.getName().contains("JOB" + job.getId())){
                    if(log.isDebugEnabled())
                        log.debug("removing mail job for: " + job);
                    manager.getAlarmManager().removeAlarm(alarmEntry);
                }
            }
        }
    }
}
