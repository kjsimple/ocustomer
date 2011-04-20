package org.opencustomer.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.calendar.EventVO.RecurrenceInWeek;
import org.opencustomer.framework.util.DateUtility;
import org.opencustomer.webapp.module.calendar.util.EventBean;

public final class EventUtility
{
    private static final Logger log = Logger.getLogger(EventUtility.class);
    
    public static Date calculateRecurrenceEndDate(EventVO event) 
    {
        Date recurrenceEndDate = event.getStartDate();
        
        CalendarVO calendar = null;
        for(EventCalendarVO vo : event.getEventCalendars()) {
            if(vo.getParticipiantType().equals(EventCalendarVO.ParticipiantType.HOST)) {
                calendar = vo.getCalendar();
            }
        }
        
        if(!EventVO.RecurrenceType.NONE.equals(event.getRecurrenceType()) &&
                !EventVO.RecurrenceType.FOREVER.equals(event.getRecurrenceType()))
        {
            List<EventBean> beans = calculateRecurrences(calendar, null, event, false);
            for(EventBean bean : beans)
            {
                if(bean.getEndDate().after(recurrenceEndDate))
                    recurrenceEndDate = bean.getEndDate();
            }
        }
        else
            recurrenceEndDate = null;

        return recurrenceEndDate;
    }
    
    public static List<EventBean> calculateRecurrences(CalendarVO myCalendar, Date endDate, EventVO event, boolean readable)
    {
        if(log.isDebugEnabled())
            log.debug("calculate recurrences for "+event);
        
        List<EventBean> list = new ArrayList<EventBean>();
     
        long duration = event.getEndDate().getTime()-event.getStartDate().getTime();
        
        Calendar calcStartDate = GregorianCalendar.getInstance();
        calcStartDate.setTime(event.getStartDate());
        
        int step = event.getRecurrenceCycle();

        EventCalendarVO eventCalendar = getEventCalendar(myCalendar, event);
        
        // handle days
        if(EventVO.RecurrenceUnit.DAY.equals(event.getRecurrenceCycleUnit()))
        {
            if(EventVO.RecurrenceType.NUMBER_OF_TIMES.equals(event.getRecurrenceType()))
            {
                for(int i=1; i<event.getRecurrenceNumberOfTimes(); i++)
                {
                    calcStartDate.add(Calendar.DAY_OF_MONTH, step);
                    list.add(new EventBean(calcStartDate.getTime(), duration, list.size()+1, event, readable, eventCalendar.getParticipiantType(), eventCalendar.getInvitationStatus()));
                }
            }
            else
            {
                Date recurrenceEndDate = endDate;
                if(EventVO.RecurrenceType.UNTIL_DATE.equals(event.getRecurrenceType()))
                    recurrenceEndDate = event.getRecurrenceUntilDate();
                
                boolean searching = true;
                while(searching) 
                {
                    calcStartDate.add(Calendar.DAY_OF_MONTH, step);
                    if(calcStartDate.getTime().before(recurrenceEndDate))
                        list.add(new EventBean(calcStartDate.getTime(), duration, list.size()+1, event, readable, eventCalendar.getParticipiantType(), eventCalendar.getInvitationStatus()));
                    else
                        searching = false;
                        
                }
            }
        }
        else if(EventVO.RecurrenceUnit.WEEK.equals(event.getRecurrenceCycleUnit()))
        {
            if(log.isDebugEnabled())
                log.debug("recurrence unit is "+EventVO.RecurrenceUnit.WEEK);

            int[] day = new int[event.getRecurrenceInWeek().size()];
            int[] dayAdd = new int[event.getRecurrenceInWeek().size()];
            
            int week = DateUtility.getCalendar(event.getStartDate(), myCalendar.getFirstDayOfWeek().getDay()).get(Calendar.WEEK_OF_YEAR);
            
            Calendar calcDateWeek = GregorianCalendar.getInstance();
            calcDateWeek.setTime(event.getStartDate());
            calcDateWeek.add(Calendar.DAY_OF_WEEK, 1);

            // check days between recurrences (max 7 day in sum (-> week))
            int pos = 0;
            for(int i=0; i<7; i++)
            {
                RecurrenceInWeek inWeek = RecurrenceInWeek.getForDate(calcDateWeek.getTime());
                
                dayAdd[pos]++;
                day[pos] = inWeek.getCalendarValue();

                if(event.getRecurrenceInWeek().contains(inWeek)) {
                    pos++; // go to next position
                }
                if(pos >= dayAdd.length) {
                    pos = 0; // go to zero position
                }
                calcDateWeek.roll(Calendar.DAY_OF_WEEK, 1);
            }
            
            if(log.isDebugEnabled())
            {
                StringBuilder builder = new StringBuilder();
                for(int i=0; i<day.length; i++) {
                    if(i > 0)
                        builder.append(", ");
                    builder.append("[day:"+day[i]+"-add:"+dayAdd[i]+"]");
                }
                
                log.debug("day adds: size="+dayAdd.length+" / "+builder.toString());
            }
            
            if(EventVO.RecurrenceType.NUMBER_OF_TIMES.equals(event.getRecurrenceType()))
            { 
                int run = 0;
                for(int i=1; i<event.getRecurrenceNumberOfTimes(); i++)
                {                    
                    if(run < dayAdd.length)
                    {
                        calcStartDate.add(Calendar.DAY_OF_WEEK, dayAdd[run]);
                        
                        run++;
                    }

                    if(step > 1) {
                        int newWeek = DateUtility.getCalendar(calcStartDate.getTime(), myCalendar.getFirstDayOfWeek().getDay()).get(Calendar.WEEK_OF_YEAR);
                        
                        if(newWeek != week)
                            calcStartDate.add(Calendar.DAY_OF_WEEK, 7*(step-1));
                        
                        week = DateUtility.getCalendar(calcStartDate.getTime(), myCalendar.getFirstDayOfWeek().getDay()).get(Calendar.WEEK_OF_YEAR);
                    }
                    
                    if(run >= dayAdd.length) {
                        run = 0;
                    }

                    list.add(new EventBean(calcStartDate.getTime(), duration, list.size()+1, event, readable, eventCalendar.getParticipiantType(), eventCalendar.getInvitationStatus()));
                }
            }
            else
            {
                Date recurrenceEndDate = endDate;
                if(EventVO.RecurrenceType.UNTIL_DATE.equals(event.getRecurrenceType()))
                    recurrenceEndDate = event.getRecurrenceUntilDate();
                
                int run = 0;
                boolean searching = true;
                
                while(searching) 
                {
                    if(run < dayAdd.length)
                    {
                        calcStartDate.add(Calendar.DAY_OF_WEEK, dayAdd[run]);
                        run++;
                    }

                    if(step > 1) {
                        int newWeek = DateUtility.getCalendar(calcStartDate.getTime(), myCalendar.getFirstDayOfWeek().getDay()).get(Calendar.WEEK_OF_YEAR);
                        
                        if(newWeek != week)
                            calcStartDate.add(Calendar.DAY_OF_WEEK, 7*(step-1));
                        
                        week = DateUtility.getCalendar(calcStartDate.getTime(), myCalendar.getFirstDayOfWeek().getDay()).get(Calendar.WEEK_OF_YEAR);
                    }
                    
                    if(run >= dayAdd.length) {
                        run = 0;
                    }
                    
                    if(calcStartDate.getTime().before(recurrenceEndDate))
                        list.add(new EventBean(calcStartDate.getTime(), duration, list.size()+1, event, readable, eventCalendar.getParticipiantType(), eventCalendar.getInvitationStatus()));
                    else
                        searching = false;
                        
                }
            }
        }
        else if(EventVO.RecurrenceUnit.MONTH.equals(event.getRecurrenceCycleUnit()))
        {
            if(EventVO.RecurrenceType.NUMBER_OF_TIMES.equals(event.getRecurrenceType()))
            {
                for(int i=1; i<event.getRecurrenceNumberOfTimes(); i++)
                {
                    if(EventVO.RecurrenceInMonth.DAY_OF_MONTH.equals(event.getRecurrenceInMonth()))
                        calcStartDate.add(Calendar.MONTH, step);
                    else
                    {                      
                        int dayOfWeek = calcStartDate.get(Calendar.DAY_OF_WEEK);
                        int dayOfWeekInMonth = calcStartDate.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                        
                        calcStartDate.add(Calendar.MONTH, step);
                        
                        calcStartDate.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                        calcStartDate.set(Calendar.DAY_OF_WEEK_IN_MONTH, dayOfWeekInMonth);
                    }
                    
                    list.add(new EventBean(calcStartDate.getTime(), duration, list.size()+1, event, readable, eventCalendar.getParticipiantType(), eventCalendar.getInvitationStatus()));
                }
            }
            else
            {
                Date recurrenceEndDate = endDate;
                if(EventVO.RecurrenceType.UNTIL_DATE.equals(event.getRecurrenceType()))
                    recurrenceEndDate = event.getRecurrenceUntilDate();
                
                boolean searching = true;
                while(searching) 
                {
                    if(EventVO.RecurrenceInMonth.DAY_OF_MONTH.equals(event.getRecurrenceInMonth()))
                        calcStartDate.add(Calendar.MONTH, step);
                    else
                    {                      
                        int dayOfWeek = calcStartDate.get(Calendar.DAY_OF_WEEK);
                        int dayOfWeekInMonth = calcStartDate.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                        
                        calcStartDate.add(Calendar.MONTH, step);
                        
                        calcStartDate.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                        calcStartDate.set(Calendar.DAY_OF_WEEK_IN_MONTH, dayOfWeekInMonth);
                    }
                    
                    if(calcStartDate.getTime().before(recurrenceEndDate))
                        list.add(new EventBean(calcStartDate.getTime(), duration, list.size()+1, event, readable, eventCalendar.getParticipiantType(), eventCalendar.getInvitationStatus()));
                    else
                        searching = false;
                        
                }
            }
        }
        else if(EventVO.RecurrenceUnit.YEAR.equals(event.getRecurrenceCycleUnit()))
        {
            if(EventVO.RecurrenceType.NUMBER_OF_TIMES.equals(event.getRecurrenceType()))
            {
                for(int i=1; i<event.getRecurrenceNumberOfTimes(); i++)
                {
                    calcStartDate.add(Calendar.YEAR, step);
                    list.add(new EventBean(calcStartDate.getTime(), duration, list.size()+1, event, readable, eventCalendar.getParticipiantType(), eventCalendar.getInvitationStatus()));
                }
            }
            else
            {
                Date recurrenceEndDate = endDate;
                if(EventVO.RecurrenceType.UNTIL_DATE.equals(event.getRecurrenceType()))
                    recurrenceEndDate = event.getRecurrenceUntilDate();
                
                boolean searching = true;
                while(searching) 
                {
                    calcStartDate.add(Calendar.YEAR, step);
                    
                    if(calcStartDate.getTime().before(recurrenceEndDate))
                        list.add(new EventBean(calcStartDate.getTime(), duration, list.size()+1, event, readable, eventCalendar.getParticipiantType(), eventCalendar.getInvitationStatus()));
                    else
                        searching = false;
                        
                }
            }
        }
        
        //berechnung der Alarm Zeit   fbreske
        if(event.getReminderDate() != null)
        {
            long alarmOffset = event.getStartDate().getTime() - event.getReminderDate().getTime();
            for(EventBean eventBean : list)
            {
                eventBean.setReminderDate(new Date(eventBean.getStartDate().getTime() - alarmOffset));
            }
        }
        
        
        if(log.isDebugEnabled()) 
        {
            for(EventBean bean : list)
            {
                log.debug("recurrence: "+bean);
            }
        }
        
        return list;
    }
    
    private static EventCalendarVO getEventCalendar(CalendarVO calendar, EventVO event) {
        EventCalendarVO eventCalendarVO = null;
        
        for(EventCalendarVO vo : event.getEventCalendars()) {
            if(vo.getCalendar().equals(calendar)) {
                eventCalendarVO = vo;
                break;
            }
        }
        
        return eventCalendarVO;
    }
}
