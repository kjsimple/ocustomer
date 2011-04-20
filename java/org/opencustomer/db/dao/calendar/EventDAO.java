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

package org.opencustomer.db.dao.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.calendar.EventVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.VersionedDAO;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class EventDAO extends VersionedDAO<EventVO>
{
    private static final Logger log = Logger.getLogger(EventDAO.class);

    public EventDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return EventVO.class;
    }
    
    public List<EventVO> getByTimePeriod(CalendarVO calendar, Date periodStart, Date periodEnd) {
        return getByTimePeriod(calendar, periodStart, periodEnd, null);
    }
    
    public List<EventVO> getByTimePeriod(CalendarVO calendar, Date periodStart, Date periodEnd, UserVO user)
    {
        return getByTimePeriod(calendar, periodStart, periodEnd, user, false);
    }

    /*
     * added reminder Date support
     * fbreske
     */
    public List<EventVO> getByTimePeriod(CalendarVO calendar, Date periodStart, Date periodEnd, UserVO user, boolean withReminderDate)
    {
        List<EventVO> events = new ArrayList<EventVO>();

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" select e ");
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" left join e.eventCalendars ec ");
            hql.append(" WHERE ec.calendar.id = :calendarId ");
            hql.append(" AND ec.invitationStatus <> '").append(EventCalendarVO.InvitationStatus.DELETED).append("' ");
            
            hql.append(" AND (((e.startDate BETWEEN :startDate AND :endDate ");
            hql.append(" OR e.endDate BETWEEN :startDate AND :endDate ");
            hql.append(" OR (e.startDate < :startDate AND e.endDate > :endDate)) ");
            hql.append(" AND e.recurrenceType = '"+EventVO.RecurrenceType.NONE+"') ");
            
            if(withReminderDate)
                hql.append(" OR (e.reminderDate BETWEEN :startDate AND :endDate )");

            hql.append(" OR ((e.recurrenceStartDate BETWEEN :startDate AND :endDate ");
            hql.append(" OR e.recurrenceEndDate BETWEEN :startDate AND :endDate ");
            hql.append(" OR (e.recurrenceStartDate < :startDate AND e.recurrenceEndDate > :endDate)) ");
            hql.append(" AND e.recurrenceType <> '"+EventVO.RecurrenceType.NONE+"') ");

            hql.append(" OR (e.recurrenceStartDate < :endDate AND e.recurrenceEndDate is null ");
            hql.append(" AND e.recurrenceType <> '"+EventVO.RecurrenceType.NONE+"')) ");

            if(user != null) {
                hql.append(" AND (e.occupied = 1 ");
                hql.append(" OR (ec.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (ec.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = ec.ownerGroup)) ");
                hql.append(" OR (ec.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND ec.ownerUser = :userId)) ");
                hql.append(" ) ");
            }
            
            hql.append(" ORDER BY e.startDate, e.endDate");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            query.setInteger("calendarId", calendar.getId());
            query.setTimestamp("startDate", periodStart);
            query.setTimestamp("endDate", periodEnd);
            
            if(user != null) 
                query.setInteger("userId", user.getId());
            
            events = toTypeSafeList(query.list());

            if (log.isDebugEnabled())
                log.debug("found " + events.size() + " events in time period from " + periodStart + " to " + periodEnd);
        }
        catch (HibernateException e)
        {
            log.error("Could not read events in time period from " + periodStart + " to " + periodEnd, e);
            throw e;
        }

        return events;
    }
}
