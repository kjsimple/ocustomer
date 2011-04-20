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

package org.opencustomer.connector.mail.messages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.opencustomer.connector.mail.Email;
import org.opencustomer.connector.mail.exception.NoValidSenderException;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.db.vo.system.UserVO;

/**
 * The status email sends a overview of the actual evens and acitvities.
 * @author fbreske
 *
 */
public class StatusEmail extends Email
{
    public static final Logger log = Logger.getLogger(StatusEmail.class);

    private ArrayList<EventVO> events;
    
    private ArrayList<JobVO> jobs;
    
    private UserVO recipient;
    
    /**
     * Reads sender email address from ResourceBundle ("WebResource") "mail.reminder.sender".
     * @throws AddressException
     */
    public StatusEmail() throws AddressException
    {
        super();
        events = new ArrayList<EventVO>();
        jobs = new ArrayList<JobVO>();
        setSender(new InternetAddress(ResourceBundle.getBundle("WebResources").getString("mail.status.sender")));
    }
    
    /**
     * Adds a UserVO to the list of recipients.
     * @param recipient The UserVo of the recipient.
     * @throws NoValidSenderException if the recipient has no email address.
     */
    public void addRecipient(UserVO user)
    {
        this.recipient = user;
        if(user.getPerson() != null)
        {
            try
            {
                addRecipient(new Recipient(RecipientType.TO, new InternetAddress(user.getPerson().getEmail())));
            }
            catch (AddressException e)
            {
                log.error("recipient " + user.getUserName() + " has no valid email address",e);
            }
        }
        else
        {
            log.warn("no person for user: " + recipient.getUserName());
        }
    }

    /**
     * 
     * @return the list of ActitivyVO in this email.
     */
    public ArrayList<JobVO> getJobs()
    {
        return jobs;
    }

    /**
     * 
     * @param job the JobVO to add.
     */
    public void addJobs(JobVO job)
    {
        jobs.add(job);
    }

    /**
     * 
     * @return the list of EventVO.
     */
    public ArrayList<EventVO> getEvents()
    {
        return events;
    }

    /**
     * 
     * @param event the EventVO to add.
     */
    public void addEvents(EventVO event)
    {
        events.add(event);
    }

    /**
     * This method overrides getTextMessageBody from Email.
     * Reads the content from the resource bundle, and generates the email.
     * @return The text Message Body from the reminder mail.
     */
    @Override
    public String getTextMessageBody()
    {
        String newline = System.getProperty("line.separator");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("WebResources", recipient.getLocale());
        StringBuilder textMessage = new StringBuilder();
        textMessage.append(resourceBundle.getString("mail.status.salutation"));
        textMessage.append(" ").append(recipient.getPerson().getFirstName());
        textMessage.append(" ").append(recipient.getPerson().getLastName()).append(",");
        textMessage.append(newline).append(newline);
        textMessage.append(resourceBundle.getString("mail.status.header")).append(newline).append(newline);
        
        if(jobs.size() > 0)
        {
            textMessage.append(resourceBundle.getString("mail.status.headline.job")).append(newline);
            for(JobVO job : jobs)
            {
                textMessage.append(resourceBundle.getString("entity.crm.job.subject")).append(": ");
                textMessage.append(job.getSubject()).append(newline);
                
                if(job.getReferencedCompany() != null || job.getReferencedCompany() != null)
                {
                    textMessage.append(resourceBundle.getString("entity.crm.job.reference")).append(": ");
                    if(job.getReferencedPerson() != null)
                        textMessage.append(job.getReferencedPerson().getFirstName()).append(" ").append(job.getReferencedPerson().getLastName());
                    if(job.getReferencedCompany() != null)
                        textMessage.append(" ").append(job.getReferencedCompany());
                    textMessage.append(newline);
                }
                
                textMessage.append(resourceBundle.getString("entity.crm.job.status")).append(": ");
                if(job.getStatus() == JobVO.Status.COMPLETED)
                    textMessage.append(resourceBundle.getString("entity.crm.job.status.completed")).append(newline);
                else if(job.getStatus() == JobVO.Status.IN_PROGRESS)
                    textMessage.append(resourceBundle.getString("entity.crm.job.status.inProgress")).append(newline);
                else if(job.getStatus() == JobVO.Status.PLANNED)
                    textMessage.append(resourceBundle.getString("entity.crm.job.status.planned")).append(newline);
                
                textMessage.append(resourceBundle.getString("entity.crm.job.priority")).append(": ");
                if(job.getPriority() == JobVO.Priority.HIGH)
                    textMessage.append(resourceBundle.getString("entity.crm.job.priority.high")).append(newline);
                else if(job.getPriority() == JobVO.Priority.MEDIUM)
                    textMessage.append(resourceBundle.getString("entity.crm.job.priority.medium")).append(newline);
                else if(job.getPriority() == JobVO.Priority.LOW)
                    textMessage.append(resourceBundle.getString("entity.crm.job.priority.low")).append(newline);
               
                if(job.getInfo() != null)
                {
                    textMessage.append(resourceBundle.getString("entity.crm.job.info")).append(": ");
                    textMessage.append(job.getInfo()).append(newline);
                }
                textMessage.append(newline);
            }
        }
        
        textMessage.append(newline);
        
        if(events.size() > 0)
        {
            textMessage.append(resourceBundle.getString("mail.status.headline.event")).append(newline);
            for(EventVO event : events)
            {
                textMessage.append(resourceBundle.getString("entity.calendar.event.title")).append(": ");
                textMessage.append(event.getTitle()).append(newline);
                
                SimpleDateFormat sdf = null;
                if(event.isAllDay())
                    sdf = new SimpleDateFormat("dd.MM.yyyy");
                else
                    sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                textMessage.append(resourceBundle.getString("entity.calendar.event.startDate")).append(": ");
                textMessage.append(sdf.format(event.getStartDate())).append(newline);
                textMessage.append(resourceBundle.getString("entity.calendar.event.endDate")).append(": ");
                textMessage.append(sdf.format(event.getEndDate())).append(newline);
                
                if(event.getLocation() != null)
                {
                    textMessage.append(resourceBundle.getString("entity.calendar.event.location")).append(": ");
                    textMessage.append(event.getLocation()).append(newline);
                }
                
                if(event.getDescription() != null)
                {
                    textMessage.append(resourceBundle.getString("entity.calendar.event.description")).append(": ");
                    textMessage.append(event.getDescription()).append(newline);
                }
                textMessage.append(newline);
            }
        }
        textMessage.append(newline);
        textMessage.append(resourceBundle.getString("mail.status.footer"));
        return textMessage.toString();
    }

    /**
     * This method overrides getHtmlMessageBody from Email.
     * Reads the content from the resource bundle, and generates the email.
     * @return The Html Message Body from the status mail.
     */
    @Override
    public String getHtmlMessageBody()
    {
        String newline = "</br>";
        ResourceBundle resourceBundle = ResourceBundle.getBundle("WebResources", recipient.getLocale());
        StringBuilder textMessage = new StringBuilder();
        textMessage.append("<html><body>");
        textMessage.append(resourceBundle.getString("mail.status.salutation"));
        textMessage.append(" ").append(recipient.getPerson().getFirstName());
        textMessage.append(" ").append(recipient.getPerson().getLastName()).append(",");
        textMessage.append(newline).append(newline);
        textMessage.append(resourceBundle.getString("mail.status.header")).append(newline).append(newline);
        
        if(jobs.size() > 0)
        {
            textMessage.append(resourceBundle.getString("mail.status.headline.job")).append(newline);
            for(JobVO job : jobs)
            {
                textMessage.append(resourceBundle.getString("entity.crm.job.subject")).append(": ");
                textMessage.append(job.getSubject()).append(newline);
                
                if(job.getReferencedCompany() != null || job.getReferencedCompany() != null)
                {
                    textMessage.append(resourceBundle.getString("entity.crm.job.reference")).append(": ");
                    if(job.getReferencedPerson() != null)
                        textMessage.append(job.getReferencedPerson().getFirstName()).append(" ").append(job.getReferencedPerson().getLastName());
                    if(job.getReferencedCompany() != null)
                        textMessage.append(" ").append(job.getReferencedCompany());
                    textMessage.append(newline);
                }
                
                textMessage.append(resourceBundle.getString("entity.crm.job.status")).append(": ");
                if(job.getStatus() == JobVO.Status.COMPLETED)
                    textMessage.append(resourceBundle.getString("entity.crm.job.status.completed")).append(newline);
                else if(job.getStatus() == JobVO.Status.IN_PROGRESS)
                    textMessage.append(resourceBundle.getString("entity.crm.job.status.inProgress")).append(newline);
                else if(job.getStatus() == JobVO.Status.PLANNED)
                    textMessage.append(resourceBundle.getString("entity.crm.job.status.planned")).append(newline);
                
                textMessage.append(resourceBundle.getString("entity.crm.job.priority")).append(": ");
                if(job.getPriority() == JobVO.Priority.HIGH)
                    textMessage.append(resourceBundle.getString("entity.crm.job.priority.high")).append(newline);
                else if(job.getPriority() == JobVO.Priority.MEDIUM)
                    textMessage.append(resourceBundle.getString("entity.crm.job.priority.medium")).append(newline);
                else if(job.getPriority() == JobVO.Priority.LOW)
                    textMessage.append(resourceBundle.getString("entity.crm.job.priority.low")).append(newline);
               
                if(job.getInfo() != null)
                {
                    textMessage.append(resourceBundle.getString("entity.crm.job.info")).append(": ");
                    textMessage.append(job.getInfo()).append(newline);
                }
                textMessage.append(newline);
            }
        }
        
        textMessage.append(newline);
        
        if(events.size() > 0)
        {
            textMessage.append(resourceBundle.getString("mail.status.headline.event")).append(newline);
            for(EventVO event : events)
            {
                textMessage.append(resourceBundle.getString("entity.calendar.event.title")).append(": ");
                textMessage.append(event.getTitle()).append(newline);
                
                SimpleDateFormat sdf = null;
                if(event.isAllDay())
                    sdf = new SimpleDateFormat("dd.MM.yyyy");
                else
                    sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                textMessage.append(resourceBundle.getString("entity.calendar.event.startDate")).append(": ");
                textMessage.append(sdf.format(event.getStartDate())).append(newline);
                textMessage.append(resourceBundle.getString("entity.calendar.event.endDate")).append(": ");
                textMessage.append(sdf.format(event.getEndDate())).append(newline);
                
                if(event.getLocation() != null)
                {
                    textMessage.append(resourceBundle.getString("entity.calendar.event.location")).append(": ");
                    textMessage.append(event.getLocation()).append(newline);
                }
                
                if(event.getDescription() != null)
                {
                    textMessage.append(resourceBundle.getString("entity.calendar.event.description")).append(": ");
                    textMessage.append(event.getDescription()).append(newline);
                }
                textMessage.append(newline);
            }
        }
        textMessage.append(newline);
        textMessage.append(resourceBundle.getString("mail.status.footer"));
        textMessage.append("</body></html>");
        return textMessage.toString();
    }
    
    /**
     * Reads the subject from the resourcebundle
     * @return the subject of the reminder email.
     */
    @Override
    public String getSubject()
    {
        ResourceBundle resourceBundle = null;
        if(recipient != null)
            resourceBundle = ResourceBundle.getBundle("WebResources", recipient.getLocale());
        else
            resourceBundle = ResourceBundle.getBundle("WebResources");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return resourceBundle.getString("mail.status.subject") + " ["+sdf.format(new Date())+"]";
    }
}
