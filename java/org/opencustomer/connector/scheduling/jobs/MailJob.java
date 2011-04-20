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

import org.apache.log4j.Logger;
import org.opencustomer.connector.mail.Email;
import org.opencustomer.connector.mail.MailService;
import org.opencustomer.util.configuration.SystemConfiguration;

import com.jtheory.jdring.AlarmEntry;
import com.jtheory.jdring.AlarmListener;

/**
 * The class MailJob sends a single email on the alarm date. Every sub class of Email kann be send by this job.
 * You have to add this job th se SchedulingManager. Normaly the LoadDailyMailJob job adds these MailJobs to the SchedulingManager.
 * @author fbreske
 *
 */
public final class MailJob implements AlarmListener
{
    private final static Logger log = Logger.getLogger(MailJob.class);
    
    private Email mail;
    
    /**
     * 
     * @param mail the Email to send.
     */
    public MailJob(Email mail) {
        this.mail = mail;
    }

    public void handleAlarm(AlarmEntry entry) {
        if(SystemConfiguration.getInstance().getStringValue(SystemConfiguration.Key.MAIL_SMTP_SERVER) == null) {
            if(log.isInfoEnabled())
                log.info("no mails send: mail server not configurated");
        } else {
            if(log.isDebugEnabled())
                log.debug("sending reminder mail to: " + mail.getRecipients());
            
            new MailService().send(mail);
        }
    }
}
