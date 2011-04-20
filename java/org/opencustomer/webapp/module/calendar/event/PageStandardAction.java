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

package org.opencustomer.webapp.module.calendar.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageAction;

public class PageStandardAction extends EditPageAction<PageStandardForm>
{
    private static Logger log = Logger.getLogger(PageStandardAction.class);

    @Override
    public void writeForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        EventVO event = (EventVO) getPanel().getEntity();

        form.setTitle(event.getTitle());
        form.setDescription(event.getDescription());
        form.setLocation(event.getLocation());

        String format = null;
        if (form.isAllDay())
            format = MessageUtil.message(request, "default.format.input.date");
        else
            format = MessageUtil.message(request, "default.format.input.dateTime");
        
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (event.getStartDate() != null)
            form.setStartTime(sdf.format(event.getStartDate()));
        if (event.getEndDate() != null)
            form.setEndTime(sdf.format(event.getEndDate()));
        
        SimpleDateFormat reminderFormat = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.dateTime"));
        if (event.getReminderDate() != null)
            form.setReminderDate(reminderFormat.format(event.getReminderDate()));

        form.setAllDay(event.isAllDay());
        form.setOccupied(event.isOccupied());
    }
    
    @Override
    public void readForm(PageStandardForm form, ActionMessages errors, HttpServletRequest request)
    {
        EventVO event = (EventVO) getPanel().getEntity();

        event.setTitle(form.getTitle());
        event.setDescription(form.getDescription());
        event.setLocation(form.getLocation());

        String format = null;
        if (form.isAllDay())
            format = MessageUtil.message(request, "default.format.input.date");
        else
            format = MessageUtil.message(request, "default.format.input.dateTime");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        
        try {
            event.setStartDate(sdf.parse(form.getStartTime()));

            if (form.isAllDay()) {
                Calendar endDate = GregorianCalendar.getInstance();
                endDate.setTime(sdf.parse(form.getEndTime()));
                endDate.add(Calendar.DAY_OF_MONTH, 1);
                endDate.add(Calendar.MILLISECOND, -1);

                event.setEndDate(endDate.getTime());
            } else {
                event.setEndDate(sdf.parse(form.getEndTime()));
            }
            
            SimpleDateFormat reminderFormat = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.dateTime"));
            if(form.getReminderDate() != null) {
                event.setReminderDate(reminderFormat.parse(form.getReminderDate()));
            } else {
                event.setReminderDate(null);
            }
        } catch (ParseException e) {
            log.error("problems parsing event time start/end", e);
        }

        event.setOccupied(form.isOccupied());
        event.setAllDay(form.isAllDay());
    }
}
