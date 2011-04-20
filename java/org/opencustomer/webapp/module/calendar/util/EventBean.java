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

package org.opencustomer.webapp.module.calendar.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventVO;

public class EventBean
{
    private Date startDate;
    
    private Date endDate;
    
    private Date reminderDate;  // fbreske
    
    private EventVO event;

    private Integer recurrenceNumber;
    
    private boolean readable;
    
    private EventCalendarVO.ParticipiantType type;
    
    private EventCalendarVO.InvitationStatus status;
    
    public EventBean()
    {
    }
    
    private EventBean(Date startDate, Date endDate, int recurrenceNumber, EventVO event, boolean readable, EventCalendarVO.ParticipiantType type, EventCalendarVO.InvitationStatus status)
    {
        this.startDate = startDate;
        this.endDate   = endDate;
        this.event     = event;
        this.recurrenceNumber = recurrenceNumber;
        this.readable  = readable;
        this.type      = type;
        this.status    = status;
    }
    
    public EventBean(Date startDate, long duration, int recurrenceNumber, EventVO event, boolean readable, EventCalendarVO.ParticipiantType type, EventCalendarVO.InvitationStatus status)
    {
        this.event     = event;
        this.recurrenceNumber = recurrenceNumber;
        this.startDate = startDate;
        this.readable  = readable;
        this.type      = type;
        this.status    = status;
        
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(startDate);
        if(event.isAllDay())
        {
            int days = (int)Math.round(duration / 1000.0 / 3600.0 / 24.0);

            cal.add(Calendar.DAY_OF_MONTH, days);
            cal.add(Calendar.MILLISECOND, -1);
        }
        else
            cal.add(Calendar.MILLISECOND, (int)duration);

        this.endDate = cal.getTime();
    }
    
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }
    
    public EventVO getEvent()
    {
        return event;
    }

    public void setEvent(EventVO event)
    {
        this.event = event;
    }

    public Integer getRecurrenceNumber()
    {
        return recurrenceNumber;
    }

    public void setRecurrenceNumber(Integer recurrenceNumber)
    {
        this.recurrenceNumber = recurrenceNumber;
    }

    public final boolean isReadable()
    {
        return readable;
    }

    public final void setReadable(boolean readable)
    {
        this.readable = readable;
    }

    public Date getReminderDate()
    {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate)
    {
        this.reminderDate = reminderDate;
    }

    public EventCalendarVO.InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(EventCalendarVO.InvitationStatus status) {
        this.status = status;
    }

    public EventCalendarVO.ParticipiantType getType() {
        return type;
    }

    public void setType(EventCalendarVO.ParticipiantType type) {
        this.type = type;
    }

    @Override
    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);
        
        builder.append("startDate", startDate);
        builder.append("endDate", endDate);
        builder.append("reminderDate",reminderDate);
        builder.append("recurrenceNumber", recurrenceNumber);
        builder.append("event", event);
        builder.append("readable", readable);
        builder.append("status", status);
        builder.append("type", type);
        
        return builder.toString();
    }
 
}
