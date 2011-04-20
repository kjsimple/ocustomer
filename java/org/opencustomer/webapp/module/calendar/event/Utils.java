package org.opencustomer.webapp.module.calendar.event;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventPersonVO;

public class Utils
{
    private static EventCalendarComparator eventCalendarComparator = new EventCalendarComparator();
    
    private static EventPersonComparator eventPersonComparator = new EventPersonComparator();
    
    public static Comparator<EventCalendarVO> getComparatorForEventCalendar()
    {
        return eventCalendarComparator;
    }
    
    public static Comparator<EventPersonVO> getComparatorForEventPerson()
    {
        return eventPersonComparator;
    }

    private static class EventCalendarComparator implements Comparator<EventCalendarVO>
    {
        public int compare(EventCalendarVO o1, EventCalendarVO o2)
        {
            CompareToBuilder builder = new CompareToBuilder();
            
            builder.append(o1.getParticipiantType(), o2.getParticipiantType());
            builder.append(o1.getCalendar().getUser().getUserName(), o2.getCalendar().getUser().getUserName());
            
            return builder.toComparison();
        }
    }

    private static class EventPersonComparator implements Comparator<EventPersonVO>
    {
        public int compare(EventPersonVO o1, EventPersonVO o2)
        {
            CompareToBuilder builder = new CompareToBuilder();
            
            builder.append(o1.getPerson().getLastName(), o2.getPerson().getLastName());
            builder.append(o1.getPerson().getFirstName(), o2.getPerson().getFirstName());
            
            return builder.toComparison();
        }
    }
}
