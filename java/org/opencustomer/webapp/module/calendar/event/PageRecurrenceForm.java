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
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.framework.util.EnumUtility;
import org.opencustomer.framework.util.FormUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageForm;

public final class PageRecurrenceForm extends EditPageForm
{
    private static final long serialVersionUID = 3977018439913322801L;

    private final static Logger log = Logger.getLogger(PageRecurrenceForm.class);
    
    private boolean recurrence;
    
    private String recurrenceType = EventVO.RecurrenceType.FOREVER.toString();
    
    private int numberOfTimes = 1;
    
    private String untilDate;
    
    private int cycle = 1;
    
    private String cycleUnit = EventVO.RecurrenceUnit.DAY.toString();
    
    private String[] inWeek = new String[0];
    
    private String inMonth = EventVO.RecurrenceInMonth.DAY_OF_WEEK.toString();
    
    public void validate(ActionMapping mapping, ActionMessages errors, HttpServletRequest request)
    {
        EventVO event = (EventVO)getPanel().getAttribute("event");
        
        untilDate = FormUtility.adjustParameter(untilDate);

        EventVO.RecurrenceType type = EnumUtility.valueOf(EventVO.RecurrenceType.class, recurrenceType);
        
        if(recurrence)
        {
            // add the missing startDate
            HashSet<String> newInWeek = new HashSet<String>();
            for(String day : inWeek) {
                newInWeek.add(day);
            }
            newInWeek.add(EventVO.RecurrenceInWeek.getForDate(event.getStartDate()).toString());            
            inWeek = newInWeek.toArray(new String[newInWeek.size()]);
            
            if(cycle < 1)
                errors.add("cycle", new ActionMessage("module.calendar.event.error.invalidCycle", MessageUtil.message(request, "entity.calendar.event.recurrence.cycle")));
            
            if(EventVO.RecurrenceType.NUMBER_OF_TIMES.equals(type))
            {
                if(numberOfTimes < 1)
                    errors.add("numberOfTimes", new ActionMessage("module.calendar.event.error.invalidNumberOfTimes", MessageUtil.message(request, "entity.calendar.event.recurrence.numberOfTimes")));
            }
            else if(EventVO.RecurrenceType.UNTIL_DATE.equals(type))
            {
                if (untilDate == null)
                {
                    errors.add("untilDate", new ActionMessage("default.error.missingInput", MessageUtil.message(request, "entity.calendar.event.recurrence.untilDate")));
                }
                else
                {
                    String format = MessageUtil.message(request, "default.format.input.date");
                    try
                    {
                        untilDate = DateUtility.adjustDate(format, untilDate);
                        
                        if(!event.getStartDate().before(DateUtility.getEndOfDay(new SimpleDateFormat(format).parse(untilDate))))
                            errors.add("untilDate", new ActionMessage("module.calendar.event.error.untilDateBeforeStart", MessageUtil.message(request, "entity.calendar.event.recurrence.untilDate")));
                    }
                    catch (ParseException e)
                    {
                        errors.add("untilDate", new ActionMessage("default.error.invalidFormat", MessageUtil.message(request, "entity.calendar.event.recurrence.untilDate"), format));
                    }
                }
            }
        }
    }

    public int getNumberOfTimes()
    {
        return numberOfTimes;
    }

    public void setNumberOfTimes(int numberOfTimes)
    {
        this.numberOfTimes = numberOfTimes;
    }

    public String getRecurrenceType()
    {
        return recurrenceType;
    }

    public void setRecurrenceType(String recurrenceType)
    {
        this.recurrenceType = recurrenceType;
    }

    public boolean isRecurrence()
    {
        return recurrence;
    }

    public void setRecurrence(boolean recurrence)
    {
        this.recurrence = recurrence;
    }
    
    public String getUntilDate()
    {
        return untilDate;
    }

    public void setUntilDate(String untilDate)
    {
        this.untilDate = untilDate;
    }

    public int getCycle()
    {
        return cycle;
    }

    public void setCycle(int cycle)
    {
        this.cycle = cycle;
    }

    public String getCycleUnit()
    {
        return cycleUnit;
    }

    public void setCycleUnit(String cycleUnit)
    {
        this.cycleUnit = cycleUnit;
    }

    public String getInMonth()
    {
        return inMonth;
    }

    public void setInMonth(String inMonth)
    {
        this.inMonth = inMonth;
    }

    public String[] getInWeek()
    {
        return inWeek;
    }

    public void setInWeek(String[] inWeek)
    {
        this.inWeek = inWeek;
    }
    
}
