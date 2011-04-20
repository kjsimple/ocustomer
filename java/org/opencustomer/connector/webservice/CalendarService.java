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

package org.opencustomer.connector.webservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.opencustomer.db.dao.calendar.CalendarDAO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.util.configuration.SystemConfiguration;
import org.opencustomer.util.configuration.UserConfiguration;
import org.opencustomer.util.logon.LdapLogon;
import org.opencustomer.util.logon.LocalLogon;
import org.opencustomer.util.logon.Logon;
import org.opencustomer.webapp.auth.Authenticator;
import org.opencustomer.webapp.auth.Right;
import org.opencustomer.webapp.module.calendar.util.EventBean;
import org.opencustomer.webapp.module.calendar.util.EventBeanCalculator;

/**
 * This class is a webservice to read the events for a user from opencustomer.
 * The webservice is used by the opencustomer client.
 * @author fbreske
 *
 */
public class CalendarService
{
    private static Logger log = Logger.getLogger(CalendarService.class);
    
    public CalendarService()
    {
    }
    
    /**
     * This method reads the jobs for a user, and return a list of actitvities.<br>
     * The webservice saves the job informations in a HashMap.<br>
     * Content of the Hashmap:<br>
     * <table>
     * <tr><th>HashMap key</th><th>JobVO entity</th></tr>
     * <tr><td>ID</td><td>event.getId()</td></tr>
     * <tr><td>title</td><td>event.getTitle()</td></tr>
     * <tr><td>description</td><td>event.getDescription()</td></tr>
     * <tr><td>startDate</td><td>event.getStartDate()</td></tr>
     * <tr><td>endDate</td><td>event.getEndDate()</td></tr>
     * <tr><td>occupied</td><td>event.isOccupied()</td></tr>
     * <tr><td>allDay</td><td>event.isAllDay()</td></tr>
     * <tr><td>deeplink</td><td>NOT SUPPORTED NOW</td></tr>
     * <tr><td>alarmdate</td><td>eventBean.getReminderDate()</td></tr>
     * <tr><td>recurrenceNumber</td><td>eventBean.getRecurrenceNumber()</td></tr>
     * <tr><td>ownevent</td><td>if EventCalendarVO.ParticipiantType.HOST</td></tr>
     * </table>
     * @param user the username
     * @param password the password of the user
     * @param startDate the start date of the time period
     * @param endDate the end date of the time period
     * @return a List of HashMaps
     */
    public List<HashMap> getEvents(String user, String password, Date startDate, Date endDate)
    {
        ArrayList<HashMap> eventList = null;
     
        Logon logon;
        
        if(!SystemConfiguration.getInstance().getBooleanValue(SystemConfiguration.Key.LDAP_AUTHENTICATION_ENABLED)){
            if(log.isDebugEnabled())
                log.debug("do local login");
            logon = new LocalLogon();
        }     
        else
        {
            if(log.isDebugEnabled())
                log.debug("do ldap login");
            logon = new LdapLogon();          
        }
        
        ActionErrors errors = new ActionErrors();
        UserVO uservo = logon.validate(user, password, Logon.Type.WEBSERVICE, errors);
        
        if(uservo != null && errors.isEmpty())
        {      
            Authenticator auth = new Authenticator(uservo);
            if(auth.isValid(Right.EXTERN_WEBSERVICE_READ))
            {
                UserConfiguration userConf = new UserConfiguration(uservo);
                
                eventList = new ArrayList<HashMap>();
                EventBeanCalculator calc = new EventBeanCalculator(uservo.getCalendar(),startDate,endDate);
                calc.setWithReminderDate(true);
                List<EventBean> eventBeanList = calc.getEventBeans();
                HashMap<String,String> eventMap;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                                
                for(EventBean eventBean : eventBeanList)
                {
                    EventVO event = eventBean.getEvent();
                    eventMap = new HashMap<String,String>();
                    eventMap.put("id",event.getId().toString());
                    eventMap.put("title",event.getTitle());
                    eventMap.put("description",event.getDescription());
                    eventMap.put("startDate",sdf.format(eventBean.getStartDate()));
                    eventMap.put("endDate",sdf.format(eventBean.getEndDate()));
                    eventMap.put("occupied",Boolean.toString(event.isOccupied()));
                    eventMap.put("allDay",Boolean.toString(event.isAllDay()));
                    eventMap.put("deeplink","http://localhost:8080/opencustomer/login.do"); //TODO: read URL from settings
                    if(eventBean.getReminderDate() != null)
                        eventMap.put("alarmDate",sdf.format(eventBean.getReminderDate()));
                    if(eventBean.getRecurrenceNumber() != null)
                        eventMap.put("recurrenceNumber", eventBean.getRecurrenceNumber().toString());
                    else
                        eventMap.put("recurrenceNumber", "0");
                    for(EventCalendarVO eventCalendar :event.getEventCalendars())
                    {
                        if(eventCalendar.getParticipiantType() == EventCalendarVO.ParticipiantType.HOST 
                                && eventCalendar.getCalendar() == new CalendarDAO().getForUser(uservo))
                        {
                            eventMap.put("ownevent",Boolean.toString(true));
                        }   
                        else
                            eventMap.put("ownevent",Boolean.toString(false));
                    }
                    
                    eventList.add(eventMap);
                }
            }
            else
            {
            if(log.isInfoEnabled())
                log.info("client access denied for username [" + user + "]");
            }
        }
        else
        {
            if(log.isInfoEnabled())
                log.info("user not found");
        }
        
        return eventList;
    }
}
