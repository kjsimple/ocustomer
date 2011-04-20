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

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.opencustomer.util.configuration.SystemConfiguration;

/**
 * The MailService sends a email over a smtp host.
 * @author fbreske
 *
 */
public class MailService {
    public static final Logger log = Logger.getLogger(MailService.class);
    
    private String smtpHost;
    
    /**
     * Uses the smtp server from Settings class
     *
     */
    public MailService() {
        this(SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.MAIL_SMTP_SERVER));
    }
    
    /**
     * 
     * @param smtpHost the smtp server
     */
    public MailService(String smtpHost) {
        this.smtpHost = smtpHost;
    }
    
    /**
     * Sends a email to the smtp host.
     * @param mail andy class extends Email
     */
    public void send(Email mail) {
        MimeMessage message;
        try {
            message = mail.getMessage(smtpHost);
        
            Transport.send(message);
        
        } catch (MessagingException e) {
            log.error("could not send email ",e);
        }
    }
}
