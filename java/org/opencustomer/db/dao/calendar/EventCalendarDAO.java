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

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.EventCalendarVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.VersionedDAO;

public final class EventCalendarDAO extends VersionedDAO<EventCalendarVO>
{
    private static final Logger log = Logger.getLogger(EventCalendarDAO.class);

    /**
     * @throws DatabaseException
     */
    public EventCalendarDAO() throws HibernateException
    {
        super();
    }

    protected Class getEntityClass()
    {
        return EventCalendarVO.class;
    }

    public List<EventCalendarVO> getByCalendar(CalendarVO calendar, boolean readonly) throws HibernateException
    {
        List<EventCalendarVO> l = null;
        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" FROM ").append(getEntityClass().getName()).append(" e ");
            hql.append(" WHERE e.calendar = :calendar ");

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            query.setReadOnly(readonly);
            query.setEntity("calendar", calendar);
            
            l = toTypeSafeList(query.list());
        }
        catch (HibernateException e)
        {
            log.error("Could eventCalendars by calendar", e);
            throw e;
        }

        return l;
    }
}
