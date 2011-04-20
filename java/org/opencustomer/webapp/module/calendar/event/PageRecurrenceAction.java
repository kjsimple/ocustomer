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
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.calendar.EventVO.RecurrenceInMonth;
import org.opencustomer.framework.util.EnumUtility;
import org.opencustomer.framework.webapp.util.MessageUtil;
import org.opencustomer.webapp.action.EditPageAction;

public class PageRecurrenceAction extends EditPageAction<PageRecurrenceForm>
{
    private static Logger log = Logger.getLogger(PageRecurrenceAction.class);

    @Override
    public void writeForm(PageRecurrenceForm form, ActionMessages errors, HttpServletRequest request)
    {
        EventVO event = (EventVO) getPanel().getEntity();

        if(!EventVO.RecurrenceType.NONE.equals(event.getRecurrenceType()))
        {
            form.setRecurrence(true);
            form.setRecurrenceType(event.getRecurrenceType().toString());
        }

        if(event.getRecurrenceNumberOfTimes() != null)
            form.setNumberOfTimes(event.getRecurrenceNumberOfTimes());

        SimpleDateFormat sdfDate = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.date"));
        if (event.getRecurrenceUntilDate() != null)
            form.setUntilDate(sdfDate.format(event.getRecurrenceUntilDate()));
        else 
            form.setUntilDate(sdfDate.format(event.getStartDate()));

        if(event.getRecurrenceCycle() != null)
            form.setCycle(event.getRecurrenceCycle());  
        
        if(event.getRecurrenceCycleUnit() != null)
            form.setCycleUnit(event.getRecurrenceCycleUnit().toString());
        
        if(event.getRecurrenceInMonth() != null)
            form.setInMonth(event.getRecurrenceInMonth().toString());
        else
            form.setInMonth(RecurrenceInMonth.DAY_OF_MONTH.toString());
        
        HashSet<String> values = new HashSet<String>();
        if(event.getRecurrenceInWeek() != null)
        {
            for(EventVO.RecurrenceInWeek value : event.getRecurrenceInWeek())
                values.add(value.toString());
        }
        // add default value
        values.add(EventVO.RecurrenceInWeek.getForDate(event.getStartDate()).toString());

        form.setInWeek(values.toArray(new String[values.size()]));
    }
    
    @Override
    public void readForm(PageRecurrenceForm form, ActionMessages errors, HttpServletRequest request)
    {
        EventVO event = (EventVO) getPanel().getEntity();

        EventVO.RecurrenceType type = EnumUtility.valueOf(EventVO.RecurrenceType.class, form.getRecurrenceType());
        
        if(!form.isRecurrence())
        {
            event.setRecurrenceType(EventVO.RecurrenceType.NONE);
            event.setRecurrenceNumberOfTimes(null);
            event.setRecurrenceUntilDate(null);
            event.setRecurrenceCycle(null);
            event.setRecurrenceCycleUnit(null);
            event.setRecurrenceInMonth(null);
            event.getRecurrenceInWeek().clear();
        }
        else
        {                
            event.setRecurrenceType(EnumUtility.valueOf(EventVO.RecurrenceType.class, form.getRecurrenceType()));
            
            if(type.equals(EventVO.RecurrenceType.NUMBER_OF_TIMES))
            {
                event.setRecurrenceNumberOfTimes(form.getNumberOfTimes());
                event.setRecurrenceUntilDate(null);
            }
            else if(type.equals(EventVO.RecurrenceType.UNTIL_DATE))
            {
                try
                {
                    SimpleDateFormat sdf = new SimpleDateFormat(MessageUtil.message(request, "default.format.input.date"));
                    
                    Calendar cal = GregorianCalendar.getInstance();
                    cal.setTime(sdf.parse(form.getUntilDate()));
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    cal.add(Calendar.MILLISECOND, -1);
                    
                    event.setRecurrenceUntilDate(cal.getTime());
                }
                catch (ParseException e)
                {
                    log.error("problems parsing recurrence until date", e);
                }
                event.setRecurrenceNumberOfTimes(null);
            }
            else if(type.equals(EventVO.RecurrenceType.FOREVER))
            {
                event.setRecurrenceNumberOfTimes(null);
                event.setRecurrenceUntilDate(null);
            }
            
            event.setRecurrenceCycle(form.getCycle());
            event.setRecurrenceCycleUnit(EnumUtility.valueOf(EventVO.RecurrenceUnit.class, form.getCycleUnit()));
            
            if(EventVO.RecurrenceUnit.WEEK.equals(event.getRecurrenceCycleUnit()))
            {
                event.getRecurrenceInWeek().clear();
                for(int i=0; i<form.getInWeek().length; i++)
                    event.getRecurrenceInWeek().add(EnumUtility.valueOf(EventVO.RecurrenceInWeek.class, form.getInWeek()[i]));
                
                event.setRecurrenceInMonth(null);
            }
            else if(EventVO.RecurrenceUnit.MONTH.equals(event.getRecurrenceCycleUnit()))
            {
                event.setRecurrenceInMonth(EnumUtility.valueOf(EventVO.RecurrenceInMonth.class, form.getInMonth()));
                event.getRecurrenceInWeek().clear();
            }
        }

    }

}
