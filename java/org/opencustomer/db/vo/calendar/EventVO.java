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

package org.opencustomer.db.vo.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.opencustomer.db.EventUtility;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.vo.VersionedVO;

@Entity
@Table(name = "event")
@AttributeOverride(name = "id", column = @Column(name = "event_id"))
@org.hibernate.annotations.Entity(selectBeforeUpdate=true)
public class EventVO extends VersionedVO
{
    private static final long serialVersionUID = 3257291309659469873L;

    public static enum RecurrenceUnit {
        DAY,
        WEEK,
        MONTH,
        YEAR;
    }

    public static enum RecurrenceInWeek {
        MONDAY(1, Calendar.MONDAY),
        TUESDAY(2, Calendar.TUESDAY),
        WEDNESDAY(4, Calendar.WEDNESDAY),
        THURSDAY(8, Calendar.THURSDAY),
        FRIDAY(16, Calendar.FRIDAY),
        SATURDAY(32, Calendar.SATURDAY),
        SUNDAY(64, Calendar.SUNDAY);

        private int value;
        
        private int calendarValue;

        private RecurrenceInWeek(int value, int calendarValue)
        {
            this.value = value;
            this.calendarValue = calendarValue;
        }
        
        public static RecurrenceInWeek getForDate(Date date)
        {
            RecurrenceInWeek inWeek = null;
                        
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(date);
            
            for(RecurrenceInWeek value : RecurrenceInWeek.values())
            {
                if(cal.get(Calendar.DAY_OF_WEEK) == value.calendarValue)
                {
                    inWeek = value;
                    break;
                }
            }
            
            return inWeek;
        }
        
        public int getValue() 
        {
            return value;
        }

        public int getCalendarValue() 
        {
            return calendarValue;
        }
    }

    public static enum RecurrenceInMonth {
        DAY_OF_WEEK,
        DAY_OF_MONTH;
    }

    public static enum RecurrenceType
    {
        NONE,
        FOREVER,
        NUMBER_OF_TIMES,
        UNTIL_DATE;
    }

    private String title;

    private String description;

    private Date startDate;

    private Date endDate;
    
    private Date reminderDate;

    private boolean allDay;

    private boolean occupied;

    private String location;
    
    private String unknownParticipiants;
    
    private RecurrenceType recurrenceType;

    private Date recurrenceStartDate;

    private Date recurrenceEndDate;

    private Integer recurrenceNumberOfTimes;

    private Integer recurrenceCycle;

    private RecurrenceUnit recurrenceCycleUnit;

    private Set<RecurrenceInWeek> recurrenceInWeek = new LinkedHashSet<RecurrenceInWeek>();

    private RecurrenceInMonth recurrenceInMonth;
    
    private Date recurrenceUntilDate;

    private Set<EventCalendarVO> eventCalendars = new HashSet<EventCalendarVO>();
    
    private Set<EventPersonVO> eventPersons = new HashSet<EventPersonVO>();

    @Column(name = "all_day")
    public boolean isAllDay()
    {
        return allDay;
    }

    public void setAllDay(boolean allDay)
    {
        this.allDay = allDay;
    }

    @Column(name = "description")
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Column(name = "end_date")
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    @Column(name = "reminder_date")
    public Date getReminderDate()
    {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate)
    {
        this.reminderDate = reminderDate;
    }
    @Column(name = "occupied")
    public boolean isOccupied()
    {
        return occupied;
    }

    public void setOccupied(boolean occupied)
    {
        this.occupied = occupied;
    }

    @Column(name = "location")
    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    @Column(name = "unknown_participiants")
    public String getUnknownParticipiants()
    {
        return unknownParticipiants;
    }

    public void setUnknownParticipiants(String unknownParticipiants)
    {
        this.unknownParticipiants = unknownParticipiants;
    }

    @Column(name = "recurrence_cycle")
    public Integer getRecurrenceCycle()
    {
        return recurrenceCycle;
    }

    public void setRecurrenceCycle(Integer recurrenceCycle)
    {
        this.recurrenceCycle = recurrenceCycle;
    }

    @Column(name = "recurrence_in_week")
    protected Integer getRecurrenceInWeekValue()
    {
        Integer value = null;

        if (recurrenceInWeek != null && recurrenceInWeek.size() > 0)
        {
            value = 0;

            for (RecurrenceInWeek day : recurrenceInWeek)
                value += day.value;
        }

        return value;
    }

    protected void setRecurrenceInWeekValue(Integer value)
    {
        recurrenceInWeek.clear();

        if (value != null)
        {
            if ((value & RecurrenceInWeek.MONDAY.value) == RecurrenceInWeek.MONDAY.value)
                recurrenceInWeek.add(RecurrenceInWeek.MONDAY);
            if ((value & RecurrenceInWeek.TUESDAY.value) == RecurrenceInWeek.TUESDAY.value)
                recurrenceInWeek.add(RecurrenceInWeek.TUESDAY);
            if ((value & RecurrenceInWeek.WEDNESDAY.value) == RecurrenceInWeek.WEDNESDAY.value)
                recurrenceInWeek.add(RecurrenceInWeek.WEDNESDAY);
            if ((value & RecurrenceInWeek.THURSDAY.value) == RecurrenceInWeek.THURSDAY.value)
                recurrenceInWeek.add(RecurrenceInWeek.THURSDAY);
            if ((value & RecurrenceInWeek.FRIDAY.value) == RecurrenceInWeek.FRIDAY.value)
                recurrenceInWeek.add(RecurrenceInWeek.FRIDAY);
            if ((value & RecurrenceInWeek.SATURDAY.value) == RecurrenceInWeek.SATURDAY.value)
                recurrenceInWeek.add(RecurrenceInWeek.SATURDAY);
            if ((value & RecurrenceInWeek.SUNDAY.value) == RecurrenceInWeek.SUNDAY.value)
                recurrenceInWeek.add(RecurrenceInWeek.SUNDAY);
        }
    }

    @Transient
    public Set<RecurrenceInWeek> getRecurrenceInWeek()
    {
        return recurrenceInWeek;
    }

    public void setRecurrenceInWeek(Set<RecurrenceInWeek> recurrenceInWeek)
    {
        this.recurrenceInWeek = recurrenceInWeek;
    }


    @Column(name = "recurrence_in_month")
    @Enumerated(EnumType.STRING)
    public RecurrenceInMonth getRecurrenceInMonth()
    {
        return recurrenceInMonth;
    }

    public void setRecurrenceInMonth(RecurrenceInMonth recurrenceInMonth)
    {
        this.recurrenceInMonth = recurrenceInMonth;
    }

    
    @Column(name = "recurrence_cycle_unit")
    @Enumerated(EnumType.STRING)
    public RecurrenceUnit getRecurrenceCycleUnit()
    {
        return recurrenceCycleUnit;
    }

    public void setRecurrenceCycleUnit(RecurrenceUnit recurrenceCycleUnit)
    {
        this.recurrenceCycleUnit = recurrenceCycleUnit;
    }

    @Column(name = "recurrence_end_date")
    public Date getRecurrenceEndDate()
    {
        return recurrenceEndDate;
    }

    public void setRecurrenceEndDate(Date recurrenceEndDate)
    {
        this.recurrenceEndDate = recurrenceEndDate;
    }

    @Column(name = "recurrence_number_of_times")
    public Integer getRecurrenceNumberOfTimes()
    {
        return recurrenceNumberOfTimes;
    }

    public void setRecurrenceNumberOfTimes(Integer recurrenceNumberOfTimes)
    {
        this.recurrenceNumberOfTimes = recurrenceNumberOfTimes;
    }

    @Column(name = "recurrence_start_date")
    public Date getRecurrenceStartDate()
    {
        return recurrenceStartDate;
    }

    public void setRecurrenceStartDate(Date recurrenceStartDate)
    {
        this.recurrenceStartDate = recurrenceStartDate;
    }

    @Column(name = "start_date")
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    @Column(name = "title")
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Column(name = "recurrence_until_date")
    public Date getRecurrenceUntilDate()
    {
        return recurrenceUntilDate;
    }

    public void setRecurrenceUntilDate(Date recurrenceUntilDate)
    {
        this.recurrenceUntilDate = recurrenceUntilDate;
    }

    @Column(name = "recurrence_type")
    @Enumerated(EnumType.STRING)
    public RecurrenceType getRecurrenceType()
    {
        return recurrenceType;
    }

    public void setRecurrenceType(RecurrenceType recurrenceType)
    {
        this.recurrenceType = recurrenceType;
    }

    private void calculateRecurrenceDates() 
    {
        if(RecurrenceType.NONE.equals(this.getRecurrenceType()))
        {
            this.setRecurrenceStartDate(null);
            this.setRecurrenceEndDate(null);
        }
        else
        {
            this.setRecurrenceStartDate(this.getStartDate());
            this.setRecurrenceEndDate(EventUtility.calculateRecurrenceEndDate(this));
        }
    }
    
    @OneToMany(mappedBy = "event", cascade=CascadeType.ALL)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN }) 
    public Set<EventCalendarVO> getEventCalendars()
    {
        return eventCalendars;
    }

    public void setEventCalendars(Set<EventCalendarVO> eventCalendars)
    {
        this.eventCalendars = eventCalendars;
    }

    @OneToMany(mappedBy = "event", cascade=CascadeType.ALL)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public Set<EventPersonVO> getEventPersons()
    {
        return eventPersons;
    }

    public void setEventPersons(Set<EventPersonVO> eventPersons)
    {
        this.eventPersons = eventPersons;
    }

    public void toString(ToStringBuilder builder)
    {
        builder.append("title", title);
        builder.append("description", description);
        builder.append("startDate", startDate);
        builder.append("reminderDate",reminderDate);
        builder.append("endDate", endDate);
        builder.append("allDay", allDay);
        builder.append("occupied", occupied);
        builder.append("recurrenceType", recurrenceType);
        builder.append("recurrenceStartDate", recurrenceStartDate);
        builder.append("recurrenceEndDate", recurrenceEndDate);
        builder.append("recurrenceNumberOfTimes", recurrenceNumberOfTimes);
        builder.append("recurrenceUntilDate", recurrenceUntilDate);
        builder.append("recurrenceCycle", recurrenceCycle);
        builder.append("recurrenceCycleUnit", recurrenceCycleUnit);
        builder.append("recurrenceInWeek", recurrenceInWeek);
        builder.append("recurrenceInMonth", recurrenceInMonth);
    }
    
    @Override
    public void onInsertOrUpdate(UserVO user)
    {
        calculateRecurrenceDates();
        
        super.onInsertOrUpdate(user);
    }
}
