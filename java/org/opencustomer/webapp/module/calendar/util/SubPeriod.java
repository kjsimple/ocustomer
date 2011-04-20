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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class SubPeriod
{
    public static final int SUB_PERIOD_DAY = 1;

    public static final int SUB_PERIOD_HOUR = 2;

    private Date startDate;

    private Date endDate;

    private int subPeriod;

    private boolean inPeriod;

    private boolean inSubPeriod;

    private List<EventBean> events = new ArrayList<EventBean>();

    public SubPeriod(Date startDate, int subPeriod)
    {
        this.subPeriod = subPeriod;

        Calendar start = GregorianCalendar.getInstance();
        start.setTime(startDate);

        Calendar end = GregorianCalendar.getInstance();

        switch (subPeriod)
        {
            case SUB_PERIOD_DAY:
            {
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MILLISECOND, 0);

                end.setTime(start.getTime());
                end.set(Calendar.HOUR_OF_DAY, 0);
                end.set(Calendar.MINUTE, 0);
                end.set(Calendar.SECOND, 0);
                end.set(Calendar.MILLISECOND, 0);
                end.add(Calendar.DAY_OF_MONTH, 1);
                end.add(Calendar.MILLISECOND, -1);

                break;
            }
            case SUB_PERIOD_HOUR:
            {
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MILLISECOND, 0);

                end.setTime(start.getTime());
                end.set(Calendar.MINUTE, 0);
                end.set(Calendar.SECOND, 0);
                end.set(Calendar.MILLISECOND, 0);
                end.add(Calendar.HOUR_OF_DAY, 1);
                end.add(Calendar.MILLISECOND, -1);

                break;
            }
        }

        this.startDate = start.getTime();
        this.endDate = end.getTime();
    }

    public SubPeriod(Date startDate, Date endDate)
    {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public final boolean isInSubPeriod()
    {
        return inSubPeriod;
    }

    public final void setInSubPeriod(boolean inSubPeriod)
    {
        this.inSubPeriod = inSubPeriod;
    }

    public final Date getEndDate()
    {
        return endDate;
    }

    private final void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public final boolean isInPeriod()
    {
        return inPeriod;
    }

    public final void setInPeriod(boolean inPeriod)
    {
        this.inPeriod = inPeriod;
    }

    public final Date getStartDate()
    {
        return startDate;
    }

    private final void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public final int getSubPeriod()
    {
        return subPeriod;
    }

    private final void setSubPeriod(int subPeriod)
    {
        this.subPeriod = subPeriod;
    }

    private void setEvents(List<EventBean> events)
    {
        this.events = events;
    }

    public List<EventBean> getEvents()
    {
        return events;
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("startDate=" + startDate);
        builder.append("endDate=" + endDate);
        builder.append("subPeriod=" + subPeriod);
        builder.append("inPeriod=" + inPeriod);
        builder.append("inSubPeriod=" + inSubPeriod);

        return builder.toString();
    }

}
