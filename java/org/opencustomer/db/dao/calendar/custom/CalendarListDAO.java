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

package org.opencustomer.db.dao.calendar.custom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.opencustomer.db.vo.calendar.CalendarVO;
import org.opencustomer.db.vo.calendar.custom.CalendarListVO;
import org.opencustomer.db.vo.system.UserVO;
import org.opencustomer.framework.db.HibernateContext;
import org.opencustomer.framework.db.dao.AbstractDAO;
import org.opencustomer.framework.db.util.Page;
import org.opencustomer.framework.db.util.Sort;
import org.opencustomer.framework.db.vo.EntityAccess;

public final class CalendarListDAO extends AbstractDAO
{
    private static final Logger log = Logger.getLogger(CalendarListDAO.class);

    public final static int SORT_USERNAME = 1;

    public final static int SORT_FIRSTNAME = 2;

    public final static int SORT_LASTNAME = 3;
    
    public final static int SORT_NAME = 4;
    
    /**
     * @throws DatabaseException
     */
    public CalendarListDAO() throws HibernateException
    {
        super();
    }

    public List<CalendarListVO> getList(String name, String userName, String firstName, String lastName, List<CalendarVO> excludedCalendars, Sort sort, Page page, UserVO user)
    {
        String _userName = toLower(adjustWildcards(userName));
        String _firstName = toLower(adjustWildcards(firstName));
        String _lastName = toLower(adjustWildcards(lastName));
        String _name = toLower(adjustWildcards(name));

        List<CalendarListVO> list = new ArrayList<CalendarListVO>();

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT e.id, e.name, u.userName, p.firstName, p.lastName, e.id ");
            hql.append(" FROM ").append(CalendarVO.class.getName()).append(" e ");
            hql.append(" left join e.user u ");
            hql.append(" left join u.profile.role r ");
            hql.append(" left join u.person p ");
            hql.append(" WHERE r.admin = 0 ");
            if (name != null)
                hql.append(" AND lower(e.name) like :name ");
            if (userName != null)
                hql.append(" AND lower(u.userName) like :userName ");
            if (firstName != null)
                hql.append(" AND lower(p.firstName) like :firstName ");
            if (lastName != null)
                hql.append(" AND lower(p.lastName) like :lastName ");
            if (user != null)
            {
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }
            if (excludedCalendars != null)
            {
                StringBuilder calendarIds = new StringBuilder();
                for(CalendarVO calendar : excludedCalendars)
                {
                    if(calendar != null) {
                        if (calendarIds.length() > 0)
                            calendarIds.append(", ");
                        calendarIds.append(calendar.getId());
                    }
                }

                hql.append(" AND LOWER(e.id) not in ( ").append(calendarIds).append(")");
            }

            if (sort != null)
                hql.append(" order by " + getSortString(sort));

            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (userName != null)
                query.setString("userName", _userName);
            if (firstName != null)
                query.setString("firstName", _firstName);
            if (lastName != null)
                query.setString("lastName", _lastName);
            if (name != null)
                query.setString("name", _name);
            if (user != null)
                query.setInteger("userId", user.getId());

            if (page != null)
            {
                query.setFirstResult(getFirstResult(page));
                query.setMaxResults(getMaxResults(page));
            }

            Set<Integer> userCalendarIds = new HashSet<Integer>();
            for(CalendarVO calendar : user.getCalendars()) {
                userCalendarIds.add(calendar.getId());
            }
            
            Iterator it = query.list().iterator();
            while (it.hasNext())
                addEntity(list, (Object[]) it.next(), userCalendarIds);

            if (log.isDebugEnabled())
                log.debug("found " + list.size() + " users");
        }
        catch (HibernateException e)
        {
            log.error("Could not find users", e);
            throw e;
        }

        return list;
    }

    public long countList(String name, String userName, String firstName, String lastName, List<CalendarVO> excludedCalendars, UserVO user)
    {
        String _userName = toLower(adjustWildcards(userName));
        String _firstName = toLower(adjustWildcards(firstName));
        String _lastName = toLower(adjustWildcards(lastName));
        String _name = toLower(adjustWildcards(name));

        long count = 0;

        try
        {
            StringBuilder hql = new StringBuilder();
            hql.append(" SELECT count(e.id) ");
            hql.append(" FROM ").append(CalendarVO.class.getName()).append(" e ");
            hql.append(" left join e.user u ");
            hql.append(" left join u.profile.role r ");
            hql.append(" left join u.person p ");
            hql.append(" WHERE r.admin = 0 ");
            if (name != null)
                hql.append(" AND lower(e.name) like :name ");
            if (userName != null)
                hql.append(" AND lower(u.userName) like :userName ");
            if (firstName != null)
                hql.append(" AND lower(p.firstName) like :firstName ");
            if (lastName != null)
                hql.append(" AND lower(p.lastName) like :lastName ");
            if (user != null)
            {
                hql.append(" AND (e.accessGlobal != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" OR (e.accessGroup != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND exists (from ").append(UserVO.class.getName()).append(" as u where u.id = :userId and u.profile.usergroups.id = e.ownerGroup)) ");
                hql.append(" OR (e.accessUser != '"+EntityAccess.Access.NONE+"' ");
                hql.append(" AND e.ownerUser = :userId)) ");
            }
            if (excludedCalendars != null)
            {
                StringBuilder calendarIds = new StringBuilder();
                for(CalendarVO calendar : excludedCalendars)
                {
                    if(calendar != null) {
                        if (calendarIds.length() > 0)
                            calendarIds.append(", ");
                        calendarIds.append(calendar.getId());
                    }
                }

                hql.append(" AND LOWER(e.id) not in ( ").append(calendarIds).append(")");
            }
            Query query = HibernateContext.getSession().createQuery(hql.toString());
            if (userName != null)
                query.setString("userName", _userName);
            if (firstName != null)
                query.setString("firstName", _firstName);
            if (lastName != null)
                query.setString("lastName", _lastName);
            if (name != null)
                query.setString("name", _name);
            if (user != null)
                query.setInteger("userId", user.getId());

            count = (Long) query.uniqueResult();

            if (log.isDebugEnabled())
                log.debug("count " + count + " calendars");
        }
        catch (HibernateException e)
        {
            log.error("Could not find calendarList", e);
            throw e;
        }

        return count;
    }
    
    private void addEntity(List<CalendarListVO> list, Object[] columns, Set<Integer> userCalendarIds)
    {
        CalendarListVO vo = new CalendarListVO();

        vo.setCalendarId((Integer) columns[0]);
        vo.setName((String) columns[1]);
        vo.setUserName((String) columns[2]);
        vo.setFirstName((String) columns[3]);
        vo.setLastName((String) columns[4]);
        
        if(userCalendarIds.contains(vo.getCalendarId()))
            vo.setAssigned(true);
        
        list.add(vo);
    }
    
    protected final String getSortString(Sort sort)
    {
        String orderField = "";

        switch (sort.getField())
        {
            case SORT_USERNAME:
            default:
                orderField = "u.userName";
                break;
            case SORT_FIRSTNAME:
                orderField = "p.firstName";
                break;
            case SORT_LASTNAME:
                orderField = "p.lastName";
                break;
            case SORT_NAME:
                orderField = "e.name";
                break;
        }

        if (!sort.isAscending())
            return orderField + " DESC";
        else
            return orderField + " ASC";
    } 


}
