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
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageStandardForm extends EditPageForm
{
    private static final long serialVersionUID = 3258690996551430963L;

    private final static Logger log = Logger.getLogger(PageStandardForm.class);

    private String title;

    private String description;

    private String startTime;

    private String endTime;

    private boolean allDay;

    private boolean occupied;

    private int eventCategoryId;
    
    private String location;

    private String reminderDate;
    
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        title        = FormUtility.adjustParameter(title, 255);
        description  = FormUtility.adjustParameter(description);
        startTime    = FormUtility.adjustParameter(startTime);
        endTime      = FormUtility.adjustParameter(endTime);
        location     = FormUtility.adjustParameter(location, 255);
        reminderDate = FormUtility.adjustParameter(reminderDate);
        
        String format = null;
        if (allDay)
            format = MessageUtil.message(request, "default.format.input.date");
        else
            format = MessageUtil.message(request, "default.format.input.dateTime");
        
        if (title == null)
            errors.add("title", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.calendar.event.title")));

        if (startTime == null)
        {
            errors.add("startDate", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.calendar.event.startDate")));
        }
        else
        {
            try
            {
                startTime = DateUtility.adjustDate(format, startTime);
            }
            catch (ParseException e)
            {
                errors.add("startTime", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "entity.calendar.event.startDate"), format));
            }
        }

        if (endTime == null)
        {
            errors.add("endTime", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.calendar.event.endDate")));
        }
        else
        {
            try
            {
                endTime = DateUtility.adjustDate(format, endTime);
            }
            catch (ParseException e)
            {
                errors.add("endTime", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "entity.calendar.event.endDate"), format));
            }
        }


        if (reminderDate != null){
            try {
                reminderDate = DateUtility.adjustDate(MessageUtil.message(request, "default.format.input.dateTime"), reminderDate);
            } catch (ParseException e) {
                errors.add("reminderDate", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "entity.calendar.event.reminderDate"), format));
            }
        }
        
        if (errors.size("startDate") == 0 && errors.size("endDate") == 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(format);

            try
            {
                Date start = sdf.parse(startTime);
                Date end = sdf.parse(endTime);

                if (end.before(start))
                    errors.add("endTime", new ActionMessage("module.calendar.event.error.endBeforeStart"));

            }
            catch (ParseException e)
            {
                log.error("problems parsing event start/end", e);
            }
        }
        
        if(errors.size("startDate") == 0 && reminderDate != null && errors.size("reminderDate") == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);

            try {
                Date start = sdf.parse(startTime);
                Date reminder = sdf.parse(reminderDate);

                if (reminder.after(start))
                    errors.add("reminderDate", new ActionMessage("module.calendar.event.error.reminderAfterStart"));

            } catch (ParseException e) {
                log.error("problems parsing event start/reminder", e);
            }
        }
    }

    public final String getDescription()
    {
        return description;
    }

    public final void setDescription(String description)
    {
        this.description = description;
    }

    public final String getTitle()
    {
        return title;
    }

    public final void setTitle(String title)
    {
        this.title = title;
    }

    public final boolean isAllDay()
    {
        return allDay;
    }

    public final void setAllDay(boolean allDay)
    {
        this.allDay = allDay;
    }

    public final int getEventCategoryId()
    {
        return eventCategoryId;
    }

    public final void setEventCategoryId(int eventCategoryId)
    {
        this.eventCategoryId = eventCategoryId;
    }

    public final boolean isOccupied()
    {
        return occupied;
    }

    public final void setOccupied(boolean occupied)
    {
        this.occupied = occupied;
    }

    public final String getEndTime()
    {
        return endTime;
    }

    public final void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public final String getStartTime()
    {
        return startTime;
    }

    public final void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public final String getLocation()
    {
        return location;
    }

    public final void setLocation(String location)
    {
        this.location = location;
    }

    public String getReminderDate()
    {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate)
    {
        this.reminderDate = reminderDate;
    }

}
