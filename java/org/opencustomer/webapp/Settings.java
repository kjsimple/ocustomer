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
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.webapp;

import org.apache.log4j.Logger;
import org.opencustomer.webapp.struts.ActionServlet;

public class Settings
{
    private static final Logger log = Logger.getLogger(ActionServlet.class);

    private final static Settings INSTANCE = new Settings();
    
    private int alarmBeforeJob = 60; //alarm minutes before a job
    
    private boolean mailReminderEnabled = false; // enable or disable daily status mails

    private Settings()
    {
    }

    public static Settings getInstance()
    {
        return INSTANCE;
    }

    public int getAlarmBeforeJob()
    {
        return alarmBeforeJob;
    }

    public void setAlarmBeforeJob(int alarmBeforeJob)
    {
        this.alarmBeforeJob = alarmBeforeJob;
    }

    public boolean isMailReminderEnabled()
    {
        return mailReminderEnabled;
    }

    public void setMailReminderEnabled(boolean mailReminderEnabled)
    {
        this.mailReminderEnabled = mailReminderEnabled;
    }
}
