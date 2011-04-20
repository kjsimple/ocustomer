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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.util.DateUtility;

public final class CalendarView
{
    public static enum Period {
        MONTH,
        WEEK,
        DAY;
    }

    private static final Logger log = Logger.getLogger(CalendarView.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private CalendarVO calendar;

    private Date referenceDate;

    private Period period;

    private Date startDate;

    private Date endDate;

    private ArrayList<SubPeriod> elements;

    private UserVO user;
    
    public CalendarView(CalendarVO calendar, Date referenceDate, Period period, UserVO user)
    {
        this.calendar = calendar;
        this.referenceDate = referenceDate;
        this.period = period;
        this.user = user;

        recalculate();
    }
    
    public void update()
    {
        readEvents();
    }

    public void setReferenceDate(Date referenceDate)
    {
        this.referenceDate = referenceDate;
        recalculate();
    }

    public Date getReferenceDate()
    {
        return referenceDate;
    }

    private void recalculate()
    {
        if (log.isDebugEnabled())
            log.debug("recalculate view");

        calculateDates();

        readEvents();
    }

    private void readEvents()
    {
        if (log.isDebugEnabled())
            log.debug("read events");

        try
        {
            EventBeanCalculator calc = new EventBeanCalculator(calendar, user, startDate, endDate);

            List<EventBean> beanList = calc.getEventBeans();
            
            for (SubPeriod subPeriod : elements)
            {
                subPeriod.getEvents().clear();

                if (log.isDebugEnabled())
                    log.debug("check events for " + subPeriod);

                for (EventBean bean : beanList)
                {
                    if (isEventValid(subPeriod, bean))
                    {
                        if (log.isDebugEnabled())
                            log.debug("   add event " + bean);
                        subPeriod.getEvents().add(bean);
                    }
                }
            }

        }
        catch (HibernateException e)
        {
            log.error("could not read events", e);
        }
    }
    
    /**
     * Test if an event is in the time of a sub period.
     * 
     * @param subPeriod the matching sub period.
     * @param event the event to check.
     * @return true, if the event is in the time of the sub period, false otherwise.
     */
    private boolean isEventValid(SubPeriod subPeriod, EventBean bean)
    {
        boolean valid = false;

        if (DateUtility.isMatching(subPeriod.getStartDate(), subPeriod.getEndDate(), bean.getStartDate(), bean.getEndDate()))
            valid = true;

        return valid;
    }
    
    private void calculateDates()
    {
        if (log.isDebugEnabled())
        {
            log.debug("calculate for " + sdf.format(referenceDate));
            log.debug("first day of week " + calendar.getFirstDayOfWeek());

            String periodName = null;
            if(Period.MONTH.equals(period))
                periodName = "month";
            else if(Period.WEEK.equals(period))
                periodName = "week";
            else if(Period.DAY.equals(period))
                periodName = "day";
            log.debug("period: " + periodName);
        }

        elements = null;

        if(Period.MONTH.equals(period))
            calculateMonth();
        else if(Period.WEEK.equals(period))
            calculateWeek();
        else if(Period.DAY.equals(period))
            calculateDay();
        
        if (log.isDebugEnabled())
        {
            log.debug("startDate is " + sdf.format(startDate));
            log.debug("endDate is   " + sdf.format(endDate));
        }
    }

    private void calculateMonth()
    {
        // berechne den ersten Tag
        Calendar firstDay = GregorianCalendar.getInstance();
        firstDay.setTime(referenceDate);

        firstDay.set(Calendar.DAY_OF_MONTH, 1);

        while (firstDay.get(Calendar.DAY_OF_WEEK) != calendar.getFirstDayOfWeek().getDay())
            firstDay.add(Calendar.DAY_OF_MONTH, -1);

        firstDay.set(Calendar.HOUR_OF_DAY, 0);
        firstDay.set(Calendar.MINUTE, 0);
        firstDay.set(Calendar.SECOND, 0);
        firstDay.set(Calendar.MILLISECOND, 0);

        startDate = firstDay.getTime();

        // berechne den letzten Tag
        Calendar lastDay = GregorianCalendar.getInstance();
        lastDay.setTime(referenceDate);
        lastDay.set(Calendar.DAY_OF_MONTH, 1);
        lastDay.add(Calendar.MONTH, 1);
        lastDay.add(Calendar.DAY_OF_MONTH, -1);

        if (lastDay.get(Calendar.DAY_OF_WEEK) == calendar.getFirstDayOfWeek().getDay())
            lastDay.add(Calendar.DAY_OF_MONTH, 1);
        while (lastDay.get(Calendar.DAY_OF_WEEK) != calendar.getFirstDayOfWeek().getDay())
            lastDay.add(Calendar.DAY_OF_MONTH, 1);
        lastDay.add(Calendar.DAY_OF_MONTH, -1);

        lastDay.set(Calendar.HOUR_OF_DAY, 0);
        lastDay.set(Calendar.MINUTE, 0);
        lastDay.set(Calendar.SECOND, 0);
        lastDay.set(Calendar.MILLISECOND, 0);
        lastDay.add(Calendar.DAY_OF_MONTH, 1);
        lastDay.add(Calendar.MILLISECOND, -1);

        endDate = lastDay.getTime();

        createDays();
    }

    private void calculateWeek()
    {
        Calendar firstDay = GregorianCalendar.getInstance();
        firstDay.setTime(referenceDate);
        while (firstDay.get(Calendar.DAY_OF_WEEK) != calendar.getFirstDayOfWeek().getDay())
            firstDay.add(Calendar.DAY_OF_MONTH, -1);

        firstDay.set(Calendar.HOUR_OF_DAY, 0);
        firstDay.set(Calendar.MINUTE, 0);
        firstDay.set(Calendar.SECOND, 0);
        firstDay.set(Calendar.MILLISECOND, 0);

        startDate = firstDay.getTime();

        Calendar lastDay = GregorianCalendar.getInstance();
        lastDay.setTime(referenceDate);
        if (lastDay.get(Calendar.DAY_OF_WEEK) == calendar.getFirstDayOfWeek().getDay())
            lastDay.add(Calendar.DAY_OF_MONTH, 1);
        while (lastDay.get(Calendar.DAY_OF_WEEK) != calendar.getFirstDayOfWeek().getDay())
            lastDay.add(Calendar.DAY_OF_MONTH, 1);
        lastDay.add(Calendar.DAY_OF_MONTH, -1);

        lastDay.set(Calendar.HOUR_OF_DAY, 0);
        lastDay.set(Calendar.MINUTE, 0);
        lastDay.set(Calendar.SECOND, 0);
        lastDay.set(Calendar.MILLISECOND, 0);
        lastDay.add(Calendar.DAY_OF_MONTH, 1);
        lastDay.add(Calendar.MILLISECOND, -1);

        endDate = lastDay.getTime();

        createDays();
    }

    private void calculateDay()
    {
        Calendar firstDay = GregorianCalendar.getInstance();
        firstDay.setTime(referenceDate);
        firstDay.set(Calendar.HOUR_OF_DAY, 0);
        firstDay.set(Calendar.MINUTE, 0);
        firstDay.set(Calendar.SECOND, 0);
        firstDay.set(Calendar.MILLISECOND, 0);

        Calendar lastDay = GregorianCalendar.getInstance();
        lastDay.setTime(referenceDate);
        lastDay.set(Calendar.HOUR_OF_DAY, 0);
        lastDay.set(Calendar.MINUTE, 0);
        lastDay.set(Calendar.SECOND, 0);
        lastDay.set(Calendar.MILLISECOND, 0);
        lastDay.add(Calendar.DAY_OF_MONTH, 1);
        lastDay.add(Calendar.MILLISECOND, -1);
    }

    private void createDays()
    {
        // create days
        elements = new ArrayList<SubPeriod>();
        Calendar refDay = GregorianCalendar.getInstance();
        refDay.setTime(referenceDate);
        Calendar today = GregorianCalendar.getInstance();
        Calendar thisDay = GregorianCalendar.getInstance();
        thisDay.setTime(startDate);
        while (thisDay.getTime().before(endDate))
        {
            SubPeriod day = new SubPeriod(thisDay.getTime(), SubPeriod.SUB_PERIOD_DAY);

            if (today.get(Calendar.DAY_OF_YEAR) == thisDay.get(Calendar.DAY_OF_YEAR) && today.get(Calendar.YEAR) == thisDay.get(Calendar.YEAR))
                day.setInSubPeriod(true);
            else
                day.setInSubPeriod(false);

            if (refDay.get(Calendar.MONTH) == thisDay.get(Calendar.MONTH))
                day.setInPeriod(true);
            else
                day.setInPeriod(false);

            elements.add(day);

            thisDay.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (log.isDebugEnabled())
            log.debug("stored " + elements.size() + " elements");

    }

    /**
     * @return Returns the endDate.
     */
    public final Date getEndDate()
    {
        return endDate;
    }

    /**
     * @return Returns the startDate.
     */
    public final Date getStartDate()
    {
        return startDate;
    }

    public List<SubPeriod> getElements()
    {
        return elements;
    }

}
