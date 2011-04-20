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
 *                 Felix Breske <felix.breske@bader-jene.de>
 * 
 * ***** END LICENSE BLOCK *****
 */

package org.opencustomer.connector.ical;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.WeekDay;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Created;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.LastModified;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Transp;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.util.CompatibilityHints;

import org.apache.log4j.Logger;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.calendar.EventVO.RecurrenceInMonth;
import org.opencustomer.db.vo.calendar.EventVO.RecurrenceInWeek;
import org.opencustomer.db.vo.calendar.EventVO.RecurrenceType;
import org.opencustomer.db.vo.calendar.EventVO.RecurrenceUnit;

/**
 * The class IcalParser generates iCalendar files out of OpenCustomer EventVOs, or EventVO out of iCalendar files.
 * @author fbreske
 *
 */

public class IcalParser
{
    private static Logger log = Logger.getLogger(IcalParser.class);
    
    private static IcalParser INSTANCE = new IcalParser();
    
    public static IcalParser getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * Generate an iCalendar file out of events, and writes the iCalendar file to the Output Stream.
     * @param events List of EventVO
     * @param out OutputStream for the iCalendar file
     * @throws IOException
     * @throws ValidationException
     * @throws ParseException 
     * @throws IOException the OutoutStream cannot be written
     */
    public void toIcal(List<EventVO> events, OutputStream out) throws ValidationException, ParseException, IOException
    {
        Calendar icalendar = new Calendar();
        icalendar.getProperties().add(new ProdId("-//OpenCustomer 0.3.0//iCal4j 1.0//DE"));
        icalendar.getProperties().add(Version.VERSION_2_0);
        icalendar.getProperties().add(CalScale.GREGORIAN);
        icalendar.getProperties().add(Method.PUBLISH);
        List<VEvent> vevents = new ArrayList<VEvent>();
        int i = 0;
        for(EventVO event : events)
        {
            VEvent vevent = new VEvent();
            if(!event.isAllDay()){
                vevent.getProperties().add(new DtStart(new DateTime(event.getStartDate())));
                vevent.getProperties().add(new DtEnd(new DateTime(event.getEndDate().getTime())));
            }
            else
            {
                vevent.getProperties().add(new DtStart(new Date(event.getStartDate())));
                vevent.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);
                vevent.getProperties().add(new DtEnd(new Date(event.getEndDate().getTime())));
                vevent.getProperties().getProperty(Property.DTEND).getParameters().add(Value.DATE);
            }
            
            if(event.isOccupied())
                vevent.getProperties().add(new Transp("OPAQUE"));
            else
                vevent.getProperties().add(new Transp("TRANSPARENT"));
            vevent.getProperties().add(new Summary(event.getTitle()));
            vevent.getProperties().add(new Created(new DateTime(event.getCreateDate())));
            vevent.getProperties().add(new DtStamp(new DateTime(event.getModifyDate())));
            vevent.getProperties().add(new LastModified(new DateTime(event.getModifyDate())));
            vevent.getProperties().add(new Description(event.getDescription()));
            vevent.getProperties().add(new Uid(event.getId().toString() + "@opencustomer"));
            vevent.getProperties().add(new XProperty("X-OC-ID",event.getId().toString()));
            if(event.getRecurrenceType() != EventVO.RecurrenceType.NONE)
                vevent.getProperties().add(getRRule(event));
            vevents.add(vevent);
        }
        icalendar.getComponents().addAll(vevents);
        CalendarOutputter calout = new CalendarOutputter();
        if(!vevents.isEmpty()) // no calendar output if there is no component
            calout.output(icalendar,out);
    }
    
    /**
     * Generates a list of EventsVO out of an iCalendar file.
     * @param ical the InputStream of the iCalendar file
     * @return list of EventVO out of the iCalendar file
     * @throws IOException
     * @throws ParserException
     * @throws ParseException
     */
    public List<EventVO> fromIcal(InputStream ical) throws IOException, ParserException, ParseException
    {
        
//        System.setProperty("ical4j.unfolding.relaxed", "true");  // relaxed parsing
        

        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_UNFOLDING, true); 
        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);
        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_OUTLOOK_COMPATIBILITY, true); 
        
        List<EventVO> events = new ArrayList<EventVO>();
        Calendar icalendar = new CalendarBuilder().build(ical);
        List<Component> components = icalendar.getComponents();
        for(Component comp : components)
        {
            EventVO event = new EventVO();
            if(log.isDebugEnabled())
                log.debug("import component: " + comp);
            if(comp instanceof VEvent)  //only VEvent are parsed
            {
                if(comp.getProperties().getProperty((Property.DESCRIPTION)) != null)
                    event.setDescription(comp.getProperties().getProperty(Property.DESCRIPTION).getValue());
                if(comp.getProperties().getProperty((Property.SUMMARY)) != null)
                    event.setTitle(comp.getProperties().getProperty(Property.SUMMARY).getValue());
                else
                    event.setTitle("XXX");
                if(comp.getProperties().getProperty((Property.DTSTART)) != null)
                    if(comp.getProperties().getProperty(Property.DTSTART).getParameter("VALUE") != null 
                       && comp.getProperties().getProperty(Property.DTSTART).getParameter("VALUE").getValue().equals("DATE"))
                    {
                        event.setStartDate(new Date(comp.getProperties().getProperty(Property.DTSTART).getValue()));
                        event.setAllDay(true);
                    }     
                    else
                        event.setStartDate(new DateTime(comp.getProperties().getProperty(Property.DTSTART).getValue()));
                if(comp.getProperties().getProperty((Property.DTEND)) != null)
                    if(comp.getProperties().getProperty(Property.DTEND).getParameter("VALUE") != null 
                            && comp.getProperties().getProperty(Property.DTEND).getParameter("VALUE").getValue().equals("DATE"))
                         {
                             event.setEndDate(new Date(comp.getProperties().getProperty(Property.DTEND).getValue()));
                         }     
                         else
                             event.setEndDate(new DateTime(comp.getProperties().getProperty(Property.DTEND).getValue()));
                else
                    event.setEndDate(event.getStartDate());
                if(comp.getProperties().getProperty(Property.RRULE) != null)
                    readRecur(comp.getProperties().getProperty(Property.RRULE).getValue(),event);
                else
                    event.setRecurrenceType(RecurrenceType.NONE);
                if(comp.getProperties().getProperty("X-OC-ID") != null)
                    event.setId(Integer.parseInt(comp.getProperties().getProperty("X-OC-ID").getValue()));
                else if(comp.getProperties().getProperty(Property.UID) != null && comp.getProperties().getProperty(Property.UID).getValue().endsWith("@opencustomner"))
                    event.setId(Integer.parseInt(comp.getProperties().getProperty(Property.UID).getValue().split("@")[0]));
                events.add(event);
                if(log.isDebugEnabled())
                    log.debug("event parsed: " + event);
            }
            else
                if(log.isDebugEnabled())
                    log.debug("component ignored");
        }
        
        return events;
    }
    
    /**
     * private method to parse the recurreces
     * @param event the given event
     * @return a RRule generated by the event
     * @throws ParseException
     */
    private RRule getRRule(EventVO event) throws ParseException
    {
        CalendarVO mainCalendar = null;
        
        for(EventCalendarVO vo : event.getEventCalendars()) {
            if(EventCalendarVO.ParticipiantType.HOST.equals(vo.getParticipiantType())) {
                mainCalendar = vo.getCalendar();
            }
        }
        
        Recur recur = new Recur("");
        if(event.getRecurrenceCycleUnit() == EventVO.RecurrenceUnit.DAY)
            recur.setFrequency(Recur.DAILY);
        else if(event.getRecurrenceCycleUnit() == EventVO.RecurrenceUnit.WEEK)
            recur.setFrequency(Recur.WEEKLY);
        else if(event.getRecurrenceCycleUnit() == EventVO.RecurrenceUnit.MONTH)
            recur.setFrequency(Recur.MONTHLY);
        else if(event.getRecurrenceCycleUnit() == EventVO.RecurrenceUnit.YEAR)
            recur.setFrequency(Recur.YEARLY);
        
        if(event.getRecurrenceCycle() != null)
            recur.setInterval(event.getRecurrenceCycle());
        
        if(event.getRecurrenceType() == RecurrenceType.UNTIL_DATE)
            recur.setUntil(new Date(event.getRecurrenceEndDate()));
        else if(event.getRecurrenceType() == RecurrenceType.NUMBER_OF_TIMES)
            recur.setCount(event.getRecurrenceNumberOfTimes());
        
        for(RecurrenceInWeek day : event.getRecurrenceInWeek())
        {
            if (day.getCalendarValue() == java.util.Calendar.MONDAY)
                recur.getDayList().add(new WeekDay("MO"));
            if (day.getCalendarValue() == java.util.Calendar.TUESDAY)
                recur.getDayList().add(new WeekDay("TU"));
            if (day.getCalendarValue() == java.util.Calendar.WEDNESDAY)
                recur.getDayList().add(new WeekDay("WE"));
            if (day.getCalendarValue() == java.util.Calendar.THURSDAY)
                recur.getDayList().add(new WeekDay("TH"));
            if (day.getCalendarValue() == java.util.Calendar.FRIDAY)
                recur.getDayList().add(new WeekDay("FR"));
            if (day.getCalendarValue() == java.util.Calendar.SATURDAY)
                recur.getDayList().add(new WeekDay("SA"));
            if (day.getCalendarValue() == java.util.Calendar.SUNDAY)
                recur.getDayList().add(new WeekDay("SU"));
        }
         
        if(event.getRecurrenceInMonth() == EventVO.RecurrenceInMonth.DAY_OF_MONTH)
        {
            java.util.Calendar cal = GregorianCalendar.getInstance();
            cal.setFirstDayOfWeek(mainCalendar.getFirstDayOfWeek().getDay());
            cal.setTime(event.getStartDate());
            recur.getMonthDayList().add(cal.get(java.util.Calendar.DAY_OF_MONTH));
        }
        else if(event.getRecurrenceInMonth() == EventVO.RecurrenceInMonth.DAY_OF_WEEK)
        {
            java.util.Calendar cal = GregorianCalendar.getInstance();
            cal.setFirstDayOfWeek(mainCalendar.getFirstDayOfWeek().getDay());
            cal.setTime(event.getStartDate());
            int week = cal.get(java.util.Calendar.WEEK_OF_MONTH);
            int day = cal.get(java.util.Calendar.DAY_OF_WEEK);
            if (day== java.util.Calendar.MONDAY)
                recur.getDayList().add(new WeekDay(week + "MO"));
            if (day == java.util.Calendar.TUESDAY)
                recur.getDayList().add(new WeekDay(week + "TU"));
            if (day == java.util.Calendar.WEDNESDAY)
                recur.getDayList().add(new WeekDay(week + "WE"));
            if (day == java.util.Calendar.THURSDAY)
                recur.getDayList().add(new WeekDay(week + "TH"));
            if (day == java.util.Calendar.FRIDAY)
                recur.getDayList().add(new WeekDay(week + "FR"));
            if (day == java.util.Calendar.SATURDAY)
                recur.getDayList().add(new WeekDay(week + "SA"));
            if (day == java.util.Calendar.SUNDAY)
                recur.getDayList().add(new WeekDay(week + "SU"));
        }
        
        recur.setWeekStartDay("MO");
        return new RRule(recur);
    }
    
    /**
     * this method parses a given rrule and write the reccurences to the event
     * @param rrule the given rrule
     * @param event the event to save the reccurences
     * @throws ParseException
     */
    private void readRecur(String rrule, EventVO event) throws ParseException
    {
        Recur recur = new Recur(rrule);
        
        if(recur.getInterval() > 0)
            event.setRecurrenceCycle(recur.getInterval());
        
        if(recur.getUntil() != null)
        {
            event.setRecurrenceUntilDate(recur.getUntil());
            event.setRecurrenceType(RecurrenceType.UNTIL_DATE);
        }
        else if(recur.getCount() != -1)
        {
            event.setRecurrenceNumberOfTimes(recur.getCount());
            event.setRecurrenceType(RecurrenceType.NUMBER_OF_TIMES);
        }
        else
            event.setRecurrenceType(RecurrenceType.FOREVER);
            
        if(recur.getFrequency().equals(Recur.DAILY))
        {
            event.setRecurrenceCycleUnit(RecurrenceUnit.DAY);
        }      
        else if(recur.getFrequency().equals(Recur.WEEKLY))
        {
            event.setRecurrenceCycleUnit(RecurrenceUnit.WEEK);
            if(recur.getDayList().contains(new WeekDay("MO")))
                event.getRecurrenceInWeek().add(RecurrenceInWeek.MONDAY);
            if(recur.getDayList().contains(new WeekDay("TU")))
                event.getRecurrenceInWeek().add(RecurrenceInWeek.TUESDAY);
            if(recur.getDayList().contains(new WeekDay("WE")))
                event.getRecurrenceInWeek().add(RecurrenceInWeek.WEDNESDAY);
            if(recur.getDayList().contains(new WeekDay("TH")))
                event.getRecurrenceInWeek().add(RecurrenceInWeek.THURSDAY);
            if(recur.getDayList().contains(new WeekDay("FR")))
                event.getRecurrenceInWeek().add(RecurrenceInWeek.FRIDAY);
            if(recur.getDayList().contains(new WeekDay("SA")))
                event.getRecurrenceInWeek().add(RecurrenceInWeek.SATURDAY);
            if(recur.getDayList().contains(new WeekDay("SU")))
                event.getRecurrenceInWeek().add(RecurrenceInWeek.SUNDAY);
        }
        else if(recur.getFrequency().equals(Recur.MONTHLY))
        {
            event.setRecurrenceCycleUnit(RecurrenceUnit.MONTH);
            if(recur.getDayList().isEmpty())
                event.setRecurrenceInMonth(RecurrenceInMonth.DAY_OF_MONTH);
            else
                event.setRecurrenceInMonth(RecurrenceInMonth.DAY_OF_WEEK);
        }
        else if(recur.getFrequency().equals(Recur.YEARLY))
        {
            event.setRecurrenceCycleUnit(RecurrenceUnit.YEAR);
        }     
    }
}
