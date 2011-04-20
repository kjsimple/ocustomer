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

import org.apache.log4j.Logger;
import org.opencustomer.connector.scheduling.SchedulingManager;

import com.jtheory.jdring.AlarmEntry;
import com.jtheory.jdring.AlarmListener;

/**
 * The LoadDailyMailJobs class generates the mail jobs for the next 2 days.
 * The alarm dates for this job is configured at conf/scheduling/event.xml.
 * @author fbreske
 *
 */
public final class LoadDailyMailJobs implements AlarmListener {
    
    private final static Logger log = Logger.getLogger(LoadDailyMailJobs.class);

    public void handleAlarm(AlarmEntry entry) {
        if(log.isDebugEnabled())
            log.debug("starting LoadDailyMailJobs");
        
        Calendar startDate = GregorianCalendar.getInstance();
        Calendar endDate = GregorianCalendar.getInstance();
        startDate.set(startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),startDate.get(Calendar.DATE)+1,0,0);
        endDate.set(startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),startDate.get(Calendar.DATE)+1,0,0);

        SchedulingManager.getInstance().loadReminderJobsfromDB(startDate.getTime(), endDate.getTime());
    }
}
