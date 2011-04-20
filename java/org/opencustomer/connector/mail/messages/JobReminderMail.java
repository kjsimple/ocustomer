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
import java.util.Date;
import java.util.ResourceBundle;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.log4j.Logger;
import org.opencustomer.connector.mail.Email;
import org.opencustomer.connector.mail.exception.NoValidSenderException;
import org.opencustomer.db.vo.crm.JobVO;
import org.opencustomer.db.vo.system.UserVO;

/**
 * This class is a reminder mail for an JobVO. 
 * @author fbreske
 *
 */
public class JobReminderMail extends Email
{
    public static final Logger log = Logger.getLogger(JobReminderMail.class);

    private JobVO job;
    
    private UserVO recipient;
    
    /**
     * Reads sender email address from ResourceBundle ("WebResource") "mail.reminder.sender".
     * @throws AddressException
     */
    public JobReminderMail() throws AddressException
    {
        super();
        setSender(new InternetAddress(ResourceBundle.getBundle("WebResources").getString("mail.reminder.sender")));
    }
    
    
    /**
     * This method overrides getHtmlMessageBody from Email.
     * Reads the content from the resource bundle.
     * @return The Html Message Body from the reminder mail.
     */
    @Override
    public String getHtmlMessageBody()
    {
        String newline = "<br/>";
        ResourceBundle resourceBundle = ResourceBundle.getBundle("WebResources", recipient.getLocale());
        StringBuilder textMessage = new StringBuilder();
        textMessage.append("<html><body>");
        textMessage.append(resourceBundle.getString("mail.reminder.salutation"));
        textMessage.append(" ").append(recipient.getPerson().getFirstName());
        textMessage.append(" ").append(recipient.getPerson().getLastName()).append(",");
        textMessage.append(newline).append(newline);
        textMessage.append(resourceBundle.getString("mail.reminder.header")).append(newline).append(newline);
        
        textMessage.append(resourceBundle.getString("mail.reminder.headline.job")).append(":").append(newline);       
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
        
        textMessage.append(resourceBundle.getString("mail.status.footer"));
        textMessage.append("</body></html>");
        return textMessage.toString();
    }

    /**
     * This method overrides getTextMessageBody from Email.
     * Reads the content from the resource bundle.
     * @return The text Message Body from the reminder mail.
     */
    @Override
    public String getTextMessageBody()
    {
        String newline = System.getProperty("line.separator");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("WebResources", recipient.getLocale());
        StringBuilder textMessage = new StringBuilder();
        textMessage.append("<html><body>");
        textMessage.append(resourceBundle.getString("mail.reminder.salutation"));
        textMessage.append(" ").append(recipient.getPerson().getFirstName());
        textMessage.append(" ").append(recipient.getPerson().getLastName()).append(",");
        textMessage.append(newline).append(newline);
        textMessage.append(resourceBundle.getString("mail.reminder.header")).append(newline).append(newline);
        
        textMessage.append(resourceBundle.getString("mail.reminder.headline.job")).append(":").append(newline);       
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
        
        textMessage.append(resourceBundle.getString("mail.status.footer"));
        textMessage.append("</body></html>");
        return textMessage.toString();
    }
    
    /**
     * Adds a UserVO to the list of recipients.
     * @param recipient The UserVo of the recipient.
     * @throws NoValidSenderException if the recipient has no email address.
     */
    public void setUser(UserVO recipient) throws NoValidSenderException
    {
        this.recipient = recipient;
        if(recipient.getPerson() != null)
        {
            try
            {
                addRecipient(new Recipient(RecipientType.TO,new InternetAddress(recipient.getPerson().getEmail())));
            }
            catch (AddressException e)
            {
                throw new NoValidSenderException();
            }
        }
        else
        {
            throw new NoValidSenderException();
        }
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
        return resourceBundle.getString("mail.reminder.subject") + " ["+sdf.format(new Date())+"]";
    }
    
    /**
     * 
     * @return the JobVO to remind.
     */
    public JobVO getJob()
    {
        return job;
    }

    /**
     * 
     * @param job the JobVO to remind.
     */
    public void setJob(JobVO job)
    {
        this.job = job;
    } 
}
