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
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.system.UserVO;

/**
 * This class is a reminder mail for an event.
 * @author fbreske
 *
 */
public class EventReminderMail extends Email
{
    public static final Logger log = Logger.getLogger(EventReminderMail.class);
    
    private EventVO event;
    
    private UserVO recipient;

    /**
     * Reads sender email address from ResourceBundle ("WebResource") "mail.reminder.sender".
     * @throws AddressException
     */
    public EventReminderMail() throws AddressException
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
        
        textMessage.append(resourceBundle.getString("mail.reminder.headline.event")).append(newline);       
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
        textMessage.append(resourceBundle.getString("mail.reminder.salutation"));
        textMessage.append(" ").append(recipient.getPerson().getFirstName());
        textMessage.append(" ").append(recipient.getPerson().getLastName()).append(",");
        textMessage.append(newline).append(newline);
        textMessage.append(resourceBundle.getString("mail.reminder.header")).append(newline).append(newline);
        
        textMessage.append(resourceBundle.getString("mail.reminder.headline.event")).append(newline);       
        textMessage.append(resourceBundle.getString("entity.calendar.event.title")).append(": ");
        textMessage.append(event.getTitle()).append(newline);
        
        SimpleDateFormat sdf = null;
        if(event.isAllDay())
            sdf = new SimpleDateFormat("dd.MM.yyyy");
        else
            sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
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
        
        textMessage.append(resourceBundle.getString("mail.status.footer"));
        return textMessage.toString();
    }

    /**
     * 
     * @return the EventVO to remind.
     */
    public EventVO getEvent()
    {
        return event;
    }

    /**
     * 
     * @param event the EventVO to remind.
     */
    public void setEvent(EventVO event)
    {
        this.event = event;
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
}
