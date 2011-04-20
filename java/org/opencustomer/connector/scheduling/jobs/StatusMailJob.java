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
 *                 Felix Breske <felix.breske@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.connector.scheduling.jobs;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;
import org.opencustomer.connector.mail.MailService;
import org.opencustomer.connector.mail.messages.StatusEmail;
import org.opencustomer.db.dao.calendar.EventDAO;
import org.opencustomer.db.dao.crm.JobDAO;
import org.opencustomer.db.dao.system.UserDAO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.util.configuration.SystemConfiguration;

import com.jtheory.jdring.AlarmEntry;
import com.jtheory.jdring.AlarmListener;

/**
 * This job sends a StatusEmail, with the jobs and Events for today, to every User with a referenced PersonVO
 * and a email.
 * The alarm date for this job is configured at conf/scheduling/event.xml.
 * 
 * @author fbreske
 * @see org.opencustomer.connector.mail.messages.StatusEmail
 *
 */
public final class StatusMailJob implements AlarmListener {
    
    public final static Logger log = Logger.getLogger(StatusMailJob.class);

    public void handleAlarm(AlarmEntry entry) {
        if(SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.MAIL_SMTP_SERVER) == null) {
            if(log.isInfoEnabled())
                log.info("no mails send: mail server not configurated");
        } else {
            if(log.isDebugEnabled())
                log.debug("starting status mail job");
            
            
            Calendar startDate = GregorianCalendar.getInstance();
            startDate.set(startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),startDate.get(Calendar.DATE),0,0);

            Calendar endDate = GregorianCalendar.getInstance();
            endDate.set(startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),startDate.get(Calendar.DATE)+1,0,0);
            
            if(log.isDebugEnabled())
                log.debug("startDate:" + startDate + " enddate:" + endDate);
            
            for(UserVO user : new UserDAO().getAll()) {
                if(user.getPerson() != null && user.getPerson().getEmail() != null) {
                    if(log.isDebugEnabled())
                        log.debug("generate status mail for: " + user.getUserName());
                    
                    try {
                        StatusEmail mail = new StatusEmail();
                        mail.addRecipient(user);
                        List<JobVO> jobs = new JobDAO().getByUser(user, startDate.getTime(), endDate.getTime());
                        List<EventVO> events = new EventDAO().getByTimePeriod(user.getCalendar(), startDate.getTime(), endDate.getTime(), user);
                        if(!jobs.isEmpty() || !events.isEmpty()) {
                            if(log.isDebugEnabled())
                                log.debug(jobs.size() + "jobs found");
                            for(JobVO job : jobs)
                                mail.addJobs(job);
                            
                            if(log.isDebugEnabled())
                                log.debug(events.size() + "events found");
                            for(EventVO event : events)
                                mail.addEvents(event);
                            
                            if(log.isDebugEnabled())
                                log.debug("sending status mail for: " + user.getUserName());
                            new MailService().send(mail);
                        } else {
                            if(log.isDebugEnabled())
                                log.debug("no status mail for " + user.getUserName() + ", no events and no jobs today");
                        }
                    } catch (AddressException e) {
                        log.warn("cannot gernerate statusmail for user: "  + user.getUserName(),e);
                    }              
                } else { 
                    log.warn("no person found for user " + user.getUserName());
                }
            }
        }
    }
}
