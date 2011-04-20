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

package org.opencustomer.connector.mail;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Abstract class for email generation. Holds the sender, recipients, and subject.
 * @author fbreske
 *
 */
public abstract class Email
{
    public static final String DEFAULT_CONTENT_TYPE = "UTF-8";
    
    private String contentType = DEFAULT_CONTENT_TYPE;
    
    private InternetAddress sender;
    
    private ArrayList<Recipient> recipients;
    
    private String subject;
    
    public Email()
    {
        recipients = new ArrayList<Recipient>();
    }
    
    /**
     * Should be overriden by the email subclasses.
     * @return the text message body of the email
     */
    public abstract String getTextMessageBody();
    
    /**
     * Should be overriden by the email subclasses.
     * @return the html message body of the email
     */
    public abstract String getHtmlMessageBody();

    /**
     * 
     * @return the recipients of the email.
     */
    public ArrayList<Recipient> getRecipients()
    {
        return recipients;
    }

    /**
     * 
     * @param recipients a list of recipients.
     */
    public void setRecipients(ArrayList<Recipient> recipients)
    {
        this.recipients = recipients;
    }
    
    /**
     * Adds a single recipient to the list of recipients.
     * @param address
     */
    public void addRecipient(Recipient address)
    {
        recipients.add(address);
    }

    /**
     * 
     * @return the senderaddress of the email.
     */
    public InternetAddress getSender()
    {
        return sender;
    }

    /**
     * Sets the sender of the email.
     * @param sender the sender.
     */
    public void setSender(InternetAddress sender)
    {
        this.sender = sender;
    }

    /**
     * 
     * @return the subject of the email.
     */
    public String getSubject()
    {
        return subject;
    }

    /**
     * Sets the subject of the email.
     * @param subject the new subject.
     */
    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    /**
     * 
     * @return the content type of the email.
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * Sets the content type of the email (default: UTF-8).
     * @param contentType
     */
    public void setContentType(String contentType)
    {
        if(contentType != null)
            this.contentType = contentType;
        else
            this.contentType = DEFAULT_CONTENT_TYPE;
    }
    
    /**
     * Uses getTextMessageBody and getHtmlMessageBody to generate the email..
     * @param smtpHost the host of the SMTP server
     * @return the MimeMessage for the email.
     * @throws MessagingException error generating MimeMessage
     */
    public final MimeMessage getMessage(String smtpHost) throws MessagingException
    {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", smtpHost);
        MimeMessage message = new MimeMessage(Session.getInstance(prop));
        message.setFrom(getSender());
        for(Recipient address : getRecipients())
            message.addRecipient(address.getType(), address.getAddress());
        message.setSubject(getSubject(), getContentType());
        
        MimeMultipart multi = new MimeMultipart("alternative");
        
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(getTextMessageBody(), "text/plain;charset=" + getContentType());
        textBody.setHeader("Content-Transfer-Encoding", "8Bit");
        multi.addBodyPart(textBody);
        
        MimeBodyPart htmlBody = new MimeBodyPart();
        htmlBody.setContent(getHtmlMessageBody(), "text/html;charset=" + getContentType());
        htmlBody.setHeader("Content-Transfer-Encoding", "8Bit");
        multi.addBodyPart(htmlBody);
        
        message.setContent(multi);
        
        return message;
    }
    
    /**
     * This class holds a single recipient with a recipient type.
     * @author fbreske
     *
     */
    public final static class Recipient
    {
        private InternetAddress address;
        
        private RecipientType type;
        
        /**
         * 
         * @param type RecipientType.CC, RecipientType.TO, RecipientType.BCC
         * @param address the address of the recipient.
         */
        public Recipient(RecipientType type, InternetAddress address)
        {
            this.address = address;
            this.type = type;
        }

        /**
         * 
         * @return the address of the recipient.
         */
        public InternetAddress getAddress()
        {
            return address;
        }

        /**
         * 
         * @param address the address of the recipient.
         */
        public void setAddress(InternetAddress address)
        {
            this.address = address;
        }

        /**
         * 
         * @return the type of the recipient.
         */
        public RecipientType getType()
        {
            return type;
        }

        /**
         * 
         * @param type the type of the recipient.
         */
        public void setType(RecipientType type)
        {
            this.type = type;
        }
    }
}
