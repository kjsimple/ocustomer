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

package org.opencustomer.connector.scheduling;

import java.io.File;
import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.opencustomer.connector.mail.exception.NoValidSenderException;
import org.opencustomer.connector.mail.messages.JobReminderMail;
import org.opencustomer.connector.mail.messages.EventReminderMail;
import org.opencustomer.connector.scheduling.jobs.MailJob;
import org.opencustomer.db.dao.crm.JobDAO;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.util.configuration.UserConfiguration;
import org.opencustomer.webapp.Settings;
import org.opencustomer.webapp.module.calendar.util.EventBean;
import org.opencustomer.webapp.module.calendar.util.EventBeanCalculator;

import com.jtheory.jdring.AlarmEntry;
import com.jtheory.jdring.AlarmListener;
import com.jtheory.jdring.AlarmManager;
import com.jtheory.jdring.PastDateException;

/**
 * This class manages a single instance of AlarmManager, and loads alarm jobs from a xml file, or from the opencustomer databse.
 * 
 * @author fbreske
 * @see com.jtheory.jdring.AlarmManager
 *
 */
public class SchedulingManager
{
    private static Logger log = Logger.getLogger(SchedulingManager.class);
    
    private AlarmManager alarmManager;
    
    private static SchedulingManager schedulingManager;
    
    public SchedulingManager()
    {
        alarmManager = new AlarmManager();
        if(log.isDebugEnabled())
            log.debug("stating alarm manager");
    }
    
    public static SchedulingManager getInstance()
    {
        if(schedulingManager == null)
            schedulingManager = new SchedulingManager();
        return schedulingManager;
    }

    /**
     * Returns the managed instance of AlarmManager
     * @return instance of AlarmManager
     * @see com.jtheory.jdring.AlarmManager
     */
    public AlarmManager getAlarmManager()
    {
        return alarmManager;
    }
    
    /**
     * Reads alarm jobs from a xml configuration file, and adds the jobs to the AlarmManager.
     * @param file xml configuration file.
     */
    public void readXMLJobs(File file)
    {
        SAXReader reader = new SAXReader();
        Document document;
        try
        {
            document = reader.read(file);
        }
        catch (DocumentException e1)
        {
            log.warn("error parsing file " + file.getName());
            return;
        }
        Element root = document.getRootElement();
        Iterator iter = root.elementIterator();
        while(iter.hasNext())
        {
            Element element = (Element) iter.next();
            String type = element.valueOf("@type");
            String name = element.valueOf("@name");
            String className = element.valueOf("@class");
            AlarmListener alarm = null;
            
            try
            {
                Class cl = Class.forName(className);
                Constructor b = cl.getConstructor(new Class[]{});
                alarm = (AlarmListener) b.newInstance(new Object[]{});
            }
            catch (Exception e)
            {
                log.warn("unable to get class for event: " + name,e);
            }
                      
            if(alarm != null && type.equals("DATE"))
            {
                Node dateNode = root.selectSingleNode("//events/event/date");
                if(dateNode != null && dateNode.getText() != null)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    try
                    {
                        AlarmEntry alarmEntry = new AlarmEntry(name,sdf.parse(dateNode.getText()), alarm);
                        alarmEntry.setRingInNewThead();
                        if(log.isDebugEnabled())
                            log.debug("adding event: "+ alarmEntry);
                        alarmManager.addAlarm(alarmEntry);
                    }
                    catch (ParseException e)
                    {
                        log.warn("unparseable date format of event: " + name,e);
                    }
                    catch (PastDateException e)
                    {
                        if(log.isInfoEnabled())
                            log.info("ignoring old event: " + name);
                    }
                    
                } 
            }
            else if(alarm != null && type.equals("DELAY"))
            {
                Node minuteNode = root.selectSingleNode("//events/event/minute");
                if(minuteNode != null && minuteNode.getText() != null)
                {
                    int minute = Integer.parseInt(minuteNode.getText());
                    try
                    {
                        AlarmEntry alarmEntry = new AlarmEntry(name,minute,true,alarm);
                        alarmEntry.setRingInNewThead();
                        if(log.isDebugEnabled())
                            log.debug("adding event: "+ alarmEntry);
                        alarmManager.addAlarm(alarmEntry);
                    }
                    catch (PastDateException e)
                    {
                        if(log.isInfoEnabled())
                            log.info("ignoring old event: " + name);;
                    }
                }
            }
            else if(alarm != null && type.equals("CRON"))
            {
                //Minuten
                int minutes[] = getChildEntries(element, "minute");
                              
                //Stunden
                int hours[] = getChildEntries(element, "hour");
                
                //Tag des Monats
                int dayOfMonth[] = getChildEntries(element, "dayofmonth");
                
                //Monat
                int months[] = getChildEntries(element, "month");
                
                //Tag der Woche
                int dayOfWeek[] = getChildEntries(element, "dayofweek");
                
                //Jahr
                Node yearNode = root.selectSingleNode("//events/event/year");
                int year = -1;
                if(yearNode != null && yearNode.getText() != null)
                    year = Integer.parseInt(yearNode.getText());
                
                try
                {
                    AlarmEntry alarmEntry = new AlarmEntry(name,minutes,hours,dayOfMonth,months,dayOfWeek,year,alarm);
                    alarmEntry.setRingInNewThead();
                    if(log.isDebugEnabled())
                        log.debug("adding event: "+ alarmEntry);
                    alarmManager.addAlarm(alarmEntry);
                }
                catch (PastDateException e)
                {
                    if(log.isInfoEnabled())
                        log.info("ignoring old event: " + name);
                }
            } 
        }     
    }
    
    /**
     * This method reads for all users the job and event jobs for the time period. 
     * Creates for every job a ReminderMail and a MailJob.
     * @param startDate the start date of the time period
     * @param endDate the end date of the time period
     */
    public void loadReminderJobsfromDB(Date startDate, Date endDate)
    {
        if(!Settings.getInstance().isMailReminderEnabled())
        {
            if(log.isDebugEnabled())
                log.debug("reminder mail disabled");
            return;
        }
        if(log.isDebugEnabled()){
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            log.debug("loading jobs from: " + sdf.format(startDate.getTime()) + " to " + sdf.format(endDate.getTime()));
        }
        SchedulingManager manager = SchedulingManager.getInstance();
        List<UserVO> users = new UserDAO().getAll();
        for(UserVO user : users)
        {
            if(log.isDebugEnabled())
                log.debug("loading jobs for: " + user.getUserName());
            UserConfiguration userConf = new UserConfiguration(user);
            
            EventBeanCalculator calc = new EventBeanCalculator(user.getCalendar(),startDate,endDate);
            calc.setWithReminderDate(true);
            List<EventBean> eventBeanList = calc.getEventBeans();
            if(log.isDebugEnabled())
                log.debug(eventBeanList.size() + " event jobs found");
            for(EventBean event : eventBeanList)
            {
                if(event.getReminderDate() != null && event.getReminderDate().before(endDate) && event.getReminderDate().after(startDate))
                {
                    if(log.isDebugEnabled())
                        log.debug("generating job for event: " + event);
                    
                    EventReminderMail reminderMail;
                    try
                    {
                        reminderMail = new EventReminderMail();
                    }
                    catch (AddressException e1)
                    {
                        log.warn("cannot create reminder mail, incorrect sender address",e1);
                        return;
                    }
                    reminderMail.setEvent(event.getEvent());
                    try
                    {
                        reminderMail.setUser(user);
                        
                        MailJob mailJob = new MailJob(reminderMail);
                        
                        for(Object obj :  manager.getAlarmManager().getAllAlarms())
                        {
                            AlarmEntry alarmEntry = (AlarmEntry) obj;
                            String name;
                            if(event.getRecurrenceNumber() != null)
                                name = "EVENT"+event.getEvent().getId()+"."+event.getRecurrenceNumber();
                            else
                                name = "EVENT" + event.getEvent().getId();
                            if(alarmEntry.getName().equals(name)){
                                if(log.isDebugEnabled())
                                    log.debug("removing mail job for: " + event);
                                manager.getAlarmManager().removeAlarm(alarmEntry);
                            }
                        }
                        try
                        {
                            if(log.isInfoEnabled())
                                log.info("adding mailJob for: " + event);
                            String name;
                            if(event.getRecurrenceNumber() != null)
                                name = "EVENT"+event.getEvent().getId()+"."+event.getRecurrenceNumber();
                            else
                                name = "EVENT" + event.getEvent().getId();
                            AlarmEntry alarmEntry = new AlarmEntry(name,event.getReminderDate(),mailJob);
                            alarmEntry.setRingInNewThead();
                            manager.getAlarmManager().addAlarm(alarmEntry);
                        }
                        catch (PastDateException e)
                        {
                            if(log.isInfoEnabled())
                                log.info("ignoring old event alarm: " + event);
                        }
                    }catch(NoValidSenderException e)
                    {
                        log.warn("no person for user: " + user);
                    }
                }
                else
                {
                    if(log.isInfoEnabled())
                        log.info("no reminder date of old reminder date for: " + event);
                }
            }
            
            List<JobVO> jobs = new JobDAO().getByUser(user, startDate, endDate);
            if(log.isDebugEnabled())
                log.debug(jobs.size() + " job jobs found");
            for(JobVO job : jobs)
            {
                if(log.isDebugEnabled())
                    log.debug("generating jobs for job: " + job);
                
                JobReminderMail reminderMail;
                try
                {
                    reminderMail = new JobReminderMail();
                }
                catch (AddressException e1)
                {
                    log.warn("cannot create reminder mail, incorrect sender address",e1);
                    return;
                }
                reminderMail.setJob(job);
                try
                {
                    reminderMail.setUser(user);

                
                    MailJob mailJob = new MailJob(reminderMail);
                    
                    for(Object obj :  manager.getAlarmManager().getAllAlarms())
                    {
                        AlarmEntry alarmEntry = (AlarmEntry) obj;
                        if(alarmEntry.getName().equals("EVENT"+job.getId())){
                            if(log.isDebugEnabled())
                                log.debug("removing mail job for: " + job);
                            manager.getAlarmManager().removeAlarm(alarmEntry);
                        }
                    }
                    try
                    {
                        if(log.isInfoEnabled())
                            log.info("adding mailJob for: " + job);
                        Calendar reminderDate = GregorianCalendar.getInstance();
                        reminderDate.setTime(job.getDueDate());
                        reminderDate.add(Calendar.MINUTE, -1 * Settings.getInstance().getAlarmBeforeJob());
                        manager.getAlarmManager().addAlarm("EVENT"+job.getId(),reminderDate.getTime(),mailJob);
                    }
                    catch (PastDateException e)
                    {
                        if(log.isInfoEnabled())
                            log.info("ignoring old event alarm: " + job);
                    }
                }
                catch (NoValidSenderException e1)
                {
                    log.warn("no person for user: " + job.getAssignedUser());
                }
            }          
        }
    }
    
    private int[] getChildEntries(Element parent, String s)
    {
        List entryList = parent.selectNodes(s);
        int dates[] = new int[entryList.size()];
        if(entryList.size() == 0)
        {
            dates = new int[1];
            dates[0] = -1;
        }
        else
        {
            int i = 0;
            for(Object tmp : entryList){
                Element element2 = (Element) tmp;
                dates[i++] = Integer.parseInt(element2.getText());
            }
        }
        return dates;
    }
}
