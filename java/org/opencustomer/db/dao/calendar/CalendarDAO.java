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
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.UndeletableDAO;
import org.opencustomer.framework.db.vo.EntityAccess;

/**
 * @author chaenderl
 * 
 */
public final class CalendarDAO extends UndeletableDAO<CalendarVO>
{
    private static final Logger log = Logger.getLogger(CalendarDAO.class);

    /**
     * @throws DatabaseException
     */
    public CalendarDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return CalendarVO.class;
    }

    
    public void delete(CalendarVO entity, Integer userId) throws HibernateException
    {
        EventCalendarDAO dao = new EventCalendarDAO();
        
        List<EventCalendarVO> eventCalendars = new EventCalendarDAO().getByCalendar(entity, false);
        for(EventCalendarVO eventCalendar : eventCalendars) {
            eventCalendar.setInvitationStatus(EventCalendarVO.InvitationStatus.DELETED);
            dao.update(eventCalendar);
        }
        
        super.delete(entity, userId);
    }

    public CalendarVO getForUser(UserVO user)
    {
        CalendarVO calendar = null;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append("FROM ").append(getEntityClass().getName()).append(" c ");
            hql.append(" WHERE c.user = :user ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            query.setEntity("user", user);
            
            calendar = (CalendarVO)query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("found calendar: " + calendar);
        }
        catch (HibernateException e)
        {
            log.error("Could not read calendar", e);
            throw e;
        }

        return calendar;
    }
    
    public List<CalendarVO> getAvailableCalendars(UserVO user)
    {
        List<CalendarVO> list = new ArrayList<CalendarVO>();

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(CalendarVO.class.getName()).append(" e ");
            hql.append(" WHERE (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
            hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
            hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
            hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
            hql.append(" AND e.ownerUser = :userId)) ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            query.setInteger("userId", user.getId());
            
            list = toTypeSafeList(query.list());
           
            if (log.isDebugEnabled())
                log.debug("found " + list.size() + " calendars");
        }
        catch (HibernateException e)
        {
            log.error("Could not find calendars", e);
            throw e;
        }

        return list;
    }
   
}
